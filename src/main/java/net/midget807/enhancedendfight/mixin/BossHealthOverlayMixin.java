package net.midget807.enhancedendfight.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.midget807.enhancedendfight.EnhancedEndFightMain;
import net.midget807.enhancedendfight.entity.client.ClientTenacityDataHolder;
import net.midget807.enhancedendfight.mixin.access.BossEventAccessor;
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

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;guiWidth()I"))
    private void enhancedEndFight$renderTenacityBar(GuiGraphics guiGraphics, CallbackInfo ci) {
        Map<UUID, LerpingBossEvent> events = this.events;
        int y = 12;
        for (Map.Entry<UUID, LerpingBossEvent> entry : events.entrySet()) {
            UUID uuid = entry.getKey();
            LerpingBossEvent event = entry.getValue();

            float tenacityProgress = ClientTenacityDataHolder.TENACITY.getOrDefault(uuid, 0f);

            int tenacityWidth = BAR_WIDTH - (int) (tenacityProgress * BAR_WIDTH);
            int x = guiGraphics.guiWidth() / 2 + 91;
            RenderSystem.enableBlend();
            guiGraphics.blitSprite(EnhancedEndFightMain.id("boss_bar/tenacity_overlay"), BAR_WIDTH, BAR_HEIGHT, BAR_WIDTH - tenacityWidth, 0, x - tenacityWidth, y, 1, BAR_WIDTH, BAR_HEIGHT);
            RenderSystem.disableBlend();

            y += 20;
        }
    }
}
