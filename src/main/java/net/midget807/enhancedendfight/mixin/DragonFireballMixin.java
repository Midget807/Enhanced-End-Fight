package net.midget807.enhancedendfight.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.midget807.enhancedendfight.registry.ModEffects;
import net.midget807.enhancedendfight.util.injector.ExplosiveAreaEffectCloud;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.DragonFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DragonFireball.class)
public abstract class DragonFireballMixin extends AbstractHurtingProjectile {
    public DragonFireballMixin(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyArg(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/AreaEffectCloud;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)V"), index = 0)
    private MobEffectInstance enhancedEndFight$modifyDragonBreathEffect(MobEffectInstance effectInstance) {
        return new MobEffectInstance(ModEffects.DRAGON_MAGIC, 10, 0);
    }

    @Inject(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/AreaEffectCloud;setRadius(F)V"))
    private void enhancedEndFight$explode(HitResult result, CallbackInfo ci, @Local AreaEffectCloud areaEffectCloud) {
        ((ExplosiveAreaEffectCloud) areaEffectCloud).setIsExplosive(true);
    }

    @Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
    private void enhancedEndFight$dontHitDragon(HitResult result, CallbackInfo ci) {
        if (result.getType() == EntityHitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) result;
            if (entityHitResult.getEntity() instanceof EnderDragon) {
                ci.cancel();
                return;
            }
        }
    }
}
