package net.midget807.enhancedendfight.entity;

import net.midget807.enhancedendfight.registry.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class OneShotTargetEntity extends Entity {
    public OneShotTargetEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    public OneShotTargetEntity(Level level, LivingEntity livingEntity) {
        this(ModEntities.ONE_SHOT_TARGET.get(), level);
        this.setPos(livingEntity.position());
        this.startRiding(livingEntity, true);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {

    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }
}
