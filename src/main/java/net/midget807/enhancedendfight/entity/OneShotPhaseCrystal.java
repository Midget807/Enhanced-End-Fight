package net.midget807.enhancedendfight.entity;

import net.midget807.enhancedendfight.registry.ModEntities;
import net.midget807.enhancedendfight.util.injector.OneShotPhaseCrystals;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.gameevent.GameEvent;

public class OneShotPhaseCrystal extends EndCrystal {
    private boolean shouldReplaceOnDeath = true;

    public OneShotPhaseCrystal(EntityType<? extends EndCrystal> entityType, Level level) {
        super(entityType, level);
    }

    public OneShotPhaseCrystal(Level level, BlockPos pos) {
        this(ModEntities.ONE_SHOT_CRYSTAL.get(), level);
        this.setPos(pos.getX() + 0.5d, pos.getY() + 1.0d, pos.getZ() + 0.5d);
    }

    @Override
    public void kill() {
        this.remove(Entity.RemovalReason.KILLED);
        this.gameEvent(GameEvent.ENTITY_DIE);
        if (this.shouldReplaceOnDeath) {
            BlockPos blockpos = this.blockPosition();
            EndCrystal endCrystal = new EndCrystal(this.level(), blockpos.getX() + 0.5, blockpos.getY(), blockpos.getZ() + 0.5);
            endCrystal.setShowBottom(true);
            this.level().addFreshEntity(endCrystal);
        }
        this.onOneShotCrystalDestroyed(this.damageSources().generic());
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (source.getEntity() instanceof EnderDragon) {
            return false;
        } else {
            if (!this.isRemoved() && !this.level().isClientSide) {
                this.remove(Entity.RemovalReason.KILLED);
                if (!source.is(DamageTypeTags.IS_EXPLOSION)) {
                    DamageSource damagesource = source.getEntity() != null ? this.damageSources().explosion(this, source.getEntity()) : null;
                    this.level().explode(this, damagesource, null, this.getX(), this.getY(), this.getZ(), 6.0F, false, Level.ExplosionInteraction.BLOCK);
                }
                if (this.shouldReplaceOnDeath) {
                    BlockPos blockpos = this.blockPosition();
                    EndCrystal endCrystal = new EndCrystal(this.level(), blockpos.getX() + 0.5, blockpos.getY(), blockpos.getZ() + 0.5);
                    endCrystal.setShowBottom(true);
                    this.level().addFreshEntity(endCrystal);
                }
                this.onOneShotCrystalDestroyed(source);
            }

            return true;
        }
    }

    private void onOneShotCrystalDestroyed(DamageSource source) {
        if (this.level() instanceof ServerLevel) {
            EndDragonFight enddragonfight = ((ServerLevel)this.level()).getDragonFight();
            if (enddragonfight != null) {
                enddragonfight.onCrystalDestroyed(this, source);
                ((OneShotPhaseCrystals) enddragonfight).updateOneShotPhaseCrystals();
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("shouldReplaceOnDeath", this.shouldReplaceOnDeath);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        if (compound.contains("shouldReplaceOnDeath")) {
            this.shouldReplaceOnDeath = compound.getBoolean("shouldReplaceOnDeath");
        }
    }
    public void setShouldReplaceOnDeath(boolean shouldReplaceOnDeath) {
        this.shouldReplaceOnDeath = shouldReplaceOnDeath;
    }
    public boolean shouldReplaceOnDeath() {
        return this.shouldReplaceOnDeath;
    }
}
