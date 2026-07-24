package net.midget807.enhancedendfight.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.midget807.enhancedendfight.registry.ModEnderDragonPhases;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonHoldingPatternPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DragonHoldingPatternPhase.class)
public abstract class DragonHoldingPatternPhaseMixin extends AbstractDragonPhaseInstance {
    @Shadow
    protected abstract void strafePlayer(Player player);

    private static final int STRAFE_CHARGE_MAX = 150;
    private static final int ATTACK_Y_VIEW_RANGE = 300;
    private static final int ATTACK_VIEW_RANGE = 300;
    private int strafeCharge;
    private TargetingConditions scanTargeting;

    public DragonHoldingPatternPhaseMixin(EnderDragon dragon) {
        super(dragon);
    }

    @Inject(method = "doServerTick", at = @At("TAIL"))
    private void enhancedEndFight$strafePlayersMore(CallbackInfo ci) {
        strafeCharge++;
        if (this.scanTargeting == null) {
            this.scanTargeting = TargetingConditions.forCombat().range(ATTACK_VIEW_RANGE).selector(p_352809_ -> Math.abs(p_352809_.getY() - dragon.getY()) <= ATTACK_Y_VIEW_RANGE);
        }
        Player player = this.dragon
                .level()
                .getNearestPlayer(this.scanTargeting, this.dragon, this.dragon.getX(), this.dragon.getY(), this.dragon.getZ());
        if (player != null) {
            if (this.strafeCharge >= STRAFE_CHARGE_MAX) {
                if (this.dragon.getRandom().nextInt(2) == 0) {
                    this.dragon.getPhaseManager().setPhase(ModEnderDragonPhases.CHARGE_PLAYER);
                } else {
                    this.strafePlayer(player);
                }
                this.strafeCharge = 0;
            }
        }
    }

    @Inject(method = "begin", at = @At("TAIL"))
    private void enhancedEndFight$strafePlayersMoreInit(CallbackInfo ci) {
        strafeCharge = 0;

        this.scanTargeting = TargetingConditions.forCombat().range(ATTACK_VIEW_RANGE).selector(p_352809_ -> Math.abs(p_352809_.getY() - dragon.getY()) <= ATTACK_Y_VIEW_RANGE);
    }

    @Inject(method = "findNewTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getNearestPlayer(Lnet/minecraft/world/entity/ai/targeting/TargetingConditions;Lnet/minecraft/world/entity/LivingEntity;DDD)Lnet/minecraft/world/entity/player/Player;"), cancellable = true)
    private void enhancedEndFight$addCustomPhases(CallbackInfo ci, @Local int crystals) {
        if (this.dragon.getRandom().nextInt(crystals + 3) == 0) {
            this.dragon.getPhaseManager().setPhase(ModEnderDragonPhases.ONE_SHOT_APPROACH);
            ci.cancel();
            return;
        }
    }
}
