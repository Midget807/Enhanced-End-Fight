package net.midget807.enhancedendfight.mixin;

import net.midget807.enhancedendfight.util.injector.ExplosiveAreaEffectCloud;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@SuppressWarnings("DiscouragedShift")
@Mixin(AreaEffectCloud.class)
public abstract class AreaEffectCloudMixin extends Entity implements ExplosiveAreaEffectCloud, TraceableEntity {
    @Shadow
    @Nullable
    private LivingEntity owner;
    @Shadow
    private int duration;
    @Shadow
    private int durationOnUse;
    @Shadow
    private int waitTime;
    private boolean isExplosive = false;

    public AreaEffectCloudMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isExplosive() {
        return isExplosive;
    }

    @Override
    public void setIsExplosive(boolean isExplosive) {
        this.isExplosive = isExplosive;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void enhancedEndFight$saveData(CompoundTag compound, CallbackInfo ci) {
        compound.putBoolean("isExplosive", isExplosive);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void enhancedEndFight$readData(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains("isExplosive")) {
            isExplosive = compound.getBoolean("isExplosive");
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void enhancedEndFight$explodeOnDiscard(CallbackInfo ci) {
        if (this.tickCount == this.duration + this.waitTime) {
            if (this.level().isClientSide) {
                this.level().addAlwaysVisibleParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            } else {
                Vec3 pos = this.position();
                DamageSource damageSource = this.owner != null ? this.damageSources().explosion(this, this.owner) : null;
                this.level().explode(
                        this.owner,
                        damageSource,
                        null,
                        pos.x,
                        pos.y,
                        pos.z,
                        7.0f,
                        false,
                        Level.ExplosionInteraction.NONE,
                        ParticleTypes.EXPLOSION,
                        ParticleTypes.EXPLOSION_EMITTER,
                        SoundEvents.GENERIC_EXPLODE
                );
            }
        }
    }
}
