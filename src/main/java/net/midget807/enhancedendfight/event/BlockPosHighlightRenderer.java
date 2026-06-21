package net.midget807.enhancedendfight.event;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.midget807.enhancedendfight.EnhancedEndFightMain;
import net.midget807.enhancedendfight.mixin.access.LevelRendererAccessor;
import net.midget807.enhancedendfight.util.BlockPosHighlights;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(modid = EnhancedEndFightMain.MODID, value = Dist.CLIENT)
public class BlockPosHighlightRenderer {
    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
        if (BlockPosHighlights.BLOCKS.isEmpty()) return;

        PoseStack pose = event.getPoseStack();
        Camera camera = event.getCamera();

        double camX = camera.getPosition().x;
        double camY = camera.getPosition().y;
        double camZ = camera.getPosition().z;

        VertexConsumer vertexConsumer = ((LevelRendererAccessor) event.getLevelRenderer()).getRenderBuffers().bufferSource().getBuffer(RenderType.LINES);

        for (BlockPos blockPos : BlockPosHighlights.BLOCKS) {
            AABB box = new AABB(blockPos).move(-camX, -camY, -camZ);
            LevelRenderer.renderLineBox(
                    pose,
                    vertexConsumer,
                    box,
                    1.0f,
                    1.0f,
                    1.0f,
                    1.0f
            );
        }

    }
}
