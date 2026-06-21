package net.midget807.enhancedendfight.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.midget807.enhancedendfight.util.injector.InvertedBar;
import net.midget807.enhancedendfight.util.injector.TenacityBossBar;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndDragonFight.class)
public abstract class EnderDragonFightMixin implements TenacityBossBar {
    @Shadow
    private boolean dragonKilled;
    @Unique
    private final ServerBossEvent tenacityEvent = (ServerBossEvent) new ServerBossEvent(
            Component.empty(), BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS
    );

    @ModifyExpressionValue(method = "updateDragon", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/boss/enderdragon/EnderDragon;getMaxHealth()F"))
    private float enhancedEndFight$getMaxHealthFromBase(float original, @Local(argsOnly = true) EnderDragon dragon) {
        return (float) dragon.getAttributeBaseValue(Attributes.MAX_HEALTH);
    }

    @Inject(method = "updateDragon", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;setProgress(F)V"))
    private void enhancedEndFight$setTenacityProgress(EnderDragon dragon, CallbackInfo ci) {
        this.tenacityEvent.setProgress((float) ((dragon.getAttributeBaseValue(Attributes.MAX_HEALTH) - dragon.getMaxHealth()) / dragon.getAttributeBaseValue(Attributes.MAX_HEALTH)));
        ((InvertedBar) this.tenacityEvent).setInverted(true);
    }

    @Inject(method = "updatePlayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;addPlayer(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private void enhancedEndFight$addPlayersToCustomBar(CallbackInfo ci, @Local ServerPlayer serverplayer) {
        this.tenacityEvent.addPlayer(serverplayer);
    }
    @Inject(method = "updatePlayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;removePlayer(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private void enhancedEndFight$removePlayersToCustomBar(CallbackInfo ci, @Local ServerPlayer serverplayer) {
        this.tenacityEvent.addPlayer(serverplayer);
    }
    @Inject(method = "setDragonKilled", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerBossEvent;setProgress(F)V"))
    private void enhancedEndFight$resetCustomBar(EnderDragon dragon, CallbackInfo ci) {
        this.tenacityEvent.setProgress(0.0f);
        this.tenacityEvent.setVisible(false);
    }
    @Inject(method = "tick", at = @At("HEAD"))
    private void enhancedEndFight$tick(CallbackInfo ci) {
        this.tenacityEvent.setVisible(!this.dragonKilled);
        ((InvertedBar) this.tenacityEvent).setInverted(true);
    }

    @Override
    public ServerBossEvent getTenacityServerBossEvent() {
        return tenacityEvent;
    }
}
