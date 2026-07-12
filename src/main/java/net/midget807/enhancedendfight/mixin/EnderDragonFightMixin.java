package net.midget807.enhancedendfight.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndDragonFight.class)
public abstract class EnderDragonFightMixin {
    @Shadow
    private boolean dragonKilled;

    @ModifyExpressionValue(method = "updateDragon", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;getMaxHealth()F"))
    private float enhancedEndFight$getMaxHealthFromBase(float original, @Local(argsOnly = true) EnderDragon dragon) {
        return (float) dragon.getAttributeBaseValue(Attributes.MAX_HEALTH);
    }

    @Inject(method = "updateDragon", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;setProgress(F)V"))
    private void enhancedEndFight$setTenacityProgress(EnderDragon dragon, CallbackInfo ci) {

    }

    @Inject(method = "updatePlayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;addPlayer(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private void enhancedEndFight$addPlayersToCustomBar(CallbackInfo ci, @Local ServerPlayer serverplayer) {

    }
    @Inject(method = "updatePlayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;removePlayer(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private void enhancedEndFight$removePlayersToCustomBar(CallbackInfo ci, @Local ServerPlayer serverplayer) {

    }
    @Inject(method = "setDragonKilled", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;setProgress(F)V"))
    private void enhancedEndFight$resetCustomBar(EnderDragon dragon, CallbackInfo ci) {

    }
    @Inject(method = "tick", at = @At("HEAD"))
    private void enhancedEndFight$tick(CallbackInfo ci) {
    }
}
