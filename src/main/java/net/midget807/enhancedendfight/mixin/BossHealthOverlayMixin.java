package net.midget807.enhancedendfight.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.midget807.enhancedendfight.util.injector.InvertedBar;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.BossEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(BossHealthOverlay.class)
public abstract class BossHealthOverlayMixin {
    @ModifyArgs(method = "drawBar(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/world/BossEvent;I[Lnet/minecraft/resources/ResourceLocation;[Lnet/minecraft/resources/ResourceLocation;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIIIIIII)V", ordinal = 0))
    private void enhancedEndFight$invertBossBar(Args args, @Local(argsOnly = true) BossEvent bossEvent, @Local(argsOnly = true, ordinal = 2) int progress) {
        if (((InvertedBar) bossEvent).isInverted()) {
            args.set(3, 182 - progress);
        }
    }
    @ModifyArgs(method = "drawBar(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/world/BossEvent;I[Lnet/minecraft/resources/ResourceLocation;[Lnet/minecraft/resources/ResourceLocation;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIIIIIII)V", ordinal = 1))
    private void enhancedEndFight$invertBossBar2(Args args, @Local(argsOnly = true) BossEvent bossEvent, @Local(argsOnly = true, ordinal = 2) int progress) {
        if (((InvertedBar) bossEvent).isInverted()) {
            args.set(3, 182 - progress);
        }
    }

    @WrapOperation(method = "drawBar(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/world/BossEvent;I[Lnet/minecraft/resources/ResourceLocation;[Lnet/minecraft/resources/ResourceLocation;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIIIIIII)V", ordinal = 1))
    private void enhancedEndFight$invertBossBar3(GuiGraphics instance, ResourceLocation sprite, int textureWidth, int textureHeight, int uPosition, int vPosition, int x, int y, int uWidth, int vHeight, Operation<Void> original, @Local(argsOnly = true) BossEvent bossEvent, @Local(argsOnly = true, ordinal = 2) int progress, @Local(argsOnly = true, ordinal = 1) ResourceLocation[] overlayProgressSprites) {
        if (((InvertedBar) bossEvent).isInverted()) {
            instance.blitSprite(overlayProgressSprites[bossEvent.getOverlay().ordinal() - 1], 182, 5, 182 - progress, 0, x, y, progress, 5);
        } else {
            original.call(instance, sprite, textureWidth, textureHeight, uPosition, vPosition, x, y, uWidth, vHeight);
        }
    }
}
