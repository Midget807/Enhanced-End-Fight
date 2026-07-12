package net.midget807.enhancedendfight.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.midget807.enhancedendfight.EnhancedEndFightMain;
import net.midget807.enhancedendfight.entity.client.ClientTenacityDataHolder;
import net.midget807.enhancedendfight.mixin.access.EndDragonFightAccessor;
import net.midget807.enhancedendfight.network.s2c.packet.TenacityBossBarProgressPacket;
import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.midget807.enhancedendfight.util.injector.TenacityData;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhaseManager;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(EnderDragon.class)
public abstract class EnderDragonMixin extends Mob implements Enemy, TenacityData {
    @Unique
    private static final ResourceLocation MAX_HEALTH_MODIFIER_ID = EnhancedEndFightMain.id("tenacity_damage");
    private static final EntityDataAccessor<Float> TENACITY = SynchedEntityData.defineId(EnderDragon.class, EntityDataSerializers.FLOAT);

    @Override
    public void setTenacityData(float value) {
        this.getEntityData().set(TENACITY, value);
        if (this.getDragonFight() != null) {
            for (ServerPlayer player : ((EndDragonFightAccessor) this.getDragonFight()).getDragonEvent().getPlayers()) {
                PacketDistributor.sendToPlayer(player, new TenacityBossBarProgressPacket(((EndDragonFightAccessor) this.getDragonFight()).getDragonEvent().getId().toString(), this.getTenacityData()));
            }
        }
    }

    @Override
    public float getTenacityData() {
        return this.getEntityData().get(TENACITY);
    }

    @Inject(method = "defineSynchedData", at = @At("HEAD"))
    private void enhancedEndFight$addCustomSyncedData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(TENACITY, 0.0f);
    }

    @Shadow
    @Final
    private EnderDragonPhaseManager phaseManager;

    @Shadow
    @Nullable
    public abstract EndDragonFight getDragonFight();

    @Shadow
    public abstract EnderDragonPhaseManager getPhaseManager();

    protected EnderDragonMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isDeadOrDying() {
        return this.getAttribute(Attributes.MAX_HEALTH).getValue() <= 2.0;
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void enhancedEndFight$tickCustomLogic(CallbackInfo ci) {
        if (this.getAttribute(Attributes.MAX_HEALTH).hasModifier(MAX_HEALTH_MODIFIER_ID)) {
            this.setTenacityData((float) (this.getAttributeValue(Attributes.MAX_HEALTH) / this.getAttributeBaseValue(Attributes.MAX_HEALTH)));
        }
        this.updateTenacity();
    }

    @Unique
    private void updateTenacity() {
        this.setTenacityData((float) (this.getAttributeValue(Attributes.MAX_HEALTH) / this.getAttributeBaseValue(Attributes.MAX_HEALTH)));
    }

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/boss/enderdragon/phases/DragonPhaseInstance;doServerTick()V", ordinal = 0))
    private void enhancedEndFight$injectCustomPhases(CallbackInfo ci, @Local DragonPhaseInstance dragonPhaseInstance) {
        if (this.getHealth() <= 0 && dragonPhaseInstance != ModEnderDragonPhases.STUNNED) {
            this.phaseManager.setPhase(ModEnderDragonPhases.STUNNED);
        }
    }

    @WrapOperation(method = "hurt(Lnet/minecraft/world/entity/boss/EnderDragonPart;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;reallyHurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean enhancedEndFight$tenacityDamage(EnderDragon instance, DamageSource damageSource, float amount, Operation<Boolean> original) {
        if (this.phaseManager.getCurrentPhase().getPhase() == ModEnderDragonPhases.TENACITY) {
            return this.tenacityHurt(damageSource, amount);
        }

        this.updateTenacity();
        return original.call(instance, damageSource, amount);
    }

    @Unique
    private boolean tenacityHurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (this.level().isClientSide) {
            return false;
        } else if (this.isDeadOrDying()) {
            return false;
        } else if (source.is(DamageTypeTags.IS_FIRE) && this.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            return false;
        } else {
            AttributeInstance health = this.getAttribute(Attributes.MAX_HEALTH);
            if (health.getValue() - amount < 0) {
                amount = (float) health.getValue();
            }
            if (health.hasModifier(MAX_HEALTH_MODIFIER_ID)) {
                double current = health.getBaseValue() - health.getValue();
                amount += (float) current;
            }
            health.addOrReplacePermanentModifier(new AttributeModifier(MAX_HEALTH_MODIFIER_ID, -amount, AttributeModifier.Operation.ADD_VALUE));
            if (this.getHealth() >= this.getMaxHealth()) {
                this.setHealth(this.getMaxHealth());
            }
            animateHurt(this.getYRot());
            this.level().broadcastDamageEvent(this, source);
            this.markHurt();
            this.playHurtSound(source);
            return true;
        }
    }

    @WrapOperation(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;getDeltaMovement()Lnet/minecraft/world/phys/Vec3;", ordinal = 6))
    private Vec3 enhancedEndFight$injectCustomPhaseMovement(EnderDragon instance, Operation<Vec3> original, @Local DragonPhaseInstance dragonPhaseInstance) {
        if (dragonPhaseInstance.getPhase() == ModEnderDragonPhases.STUNNED) {
            if (this.blockPosition().closerThan(
                    new Vec3i((int) dragonPhaseInstance.getFlyTargetLocation().x, (int) dragonPhaseInstance.getFlyTargetLocation().y, (int) dragonPhaseInstance.getFlyTargetLocation().z),
                    5
            )) {
                return this.getDeltaMovement().multiply(0, 0.8, 0);
            }
            return this.getDeltaMovement().multiply(0, 1.09, 0);
        }
        return original.call(instance);
    }
}
