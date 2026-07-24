package net.midget807.enhancedendfight.mixin;

import net.midget807.enhancedendfight.registry.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonSittingPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonSittingFlamingPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DragonSittingFlamingPhase.class)
public abstract class DragonSittingFlamingPhaseMixin extends AbstractDragonSittingPhase {
    public DragonSittingFlamingPhaseMixin(EnderDragon p_31196_) {
        super(p_31196_);
    }


    @ModifyArg(method = "doServerTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/AreaEffectCloud;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)V"), index = 0)
    private MobEffectInstance enhancedEndFight$modifyDragonBreathEffect(MobEffectInstance effectInstance) {
        return new MobEffectInstance(ModEffects.DRAGON_MAGIC, 10, 0);
    }
}
