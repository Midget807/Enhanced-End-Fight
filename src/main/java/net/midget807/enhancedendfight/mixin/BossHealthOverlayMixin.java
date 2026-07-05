package net.midget807.enhancedendfight.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.midget807.enhancedendfight.EnhancedEndFightMain;
import net.midget807.enhancedendfight.entity.client.ClientTenacityDataHolder;
import net.midget807.enhancedendfight.util.injector.InvertedBar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.BossEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Map;
import java.util.UUID;

@Mixin(BossHealthOverlay.class)
public abstract class BossHealthOverlayMixin {
    @Shadow
    @Final
    private Map<UUID, LerpingBossEvent> events;

    @Shadow
    @Final
    private static int BAR_WIDTH;

    @Shadow
    protected abstract void drawBar(GuiGraphics guiGraphics, int x, int y, BossEvent bossEvent);

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Final
    private static int BAR_HEIGHT;

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
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;guiWidth()I"))
    private void enhancedEndFight$renderTenacityBar(GuiGraphics guiGraphics, CallbackInfo ci) {
        Map<UUID, LerpingBossEvent> events = this.events;
        int y = 12;
        for (Map.Entry<UUID, LerpingBossEvent> entry : events.entrySet()) {
            UUID uuid = entry.getKey();
            LerpingBossEvent event = entry.getValue();

            float tenacityProgress = ClientTenacityDataHolder.TENACITY.getOrDefault(uuid, 0f);

            int tenacityWidth = (int) (tenacityProgress * BAR_WIDTH);
            int x = guiGraphics.guiWidth() / 2 + 91;
            guiGraphics.blitSprite(EnhancedEndFightMain.id("boss_bar/tenacity_overlay"), BAR_WIDTH, BAR_HEIGHT, BAR_WIDTH - tenacityWidth, 0, x, y, BAR_WIDTH, BAR_HEIGHT);
            /*
            Component component = event.getName();
            int textWidth = this.minecraft.font.width(component);
            int textX = guiGraphics.guiWidth() / 2 - textWidth / 2;
            int textY = y - 9;
            guiGraphics.drawString(this.minecraft.font, component, textX, textY, 16777215);
            */
            y += 20;
        }
    }
}
