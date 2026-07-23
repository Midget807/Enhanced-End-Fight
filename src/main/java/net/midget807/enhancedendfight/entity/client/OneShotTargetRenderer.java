package net.midget807.enhancedendfight.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.midget807.enhancedendfight.EnhancedEndFightMain;
import net.midget807.enhancedendfight.entity.OneShotTargetEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class OneShotTargetRenderer<T extends Entity> extends EntityRenderer<OneShotTargetEntity> {
    private static final ResourceLocation TEXTURE = EnhancedEndFightMain.id("textures/entity/one_shot_target.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE);
    public OneShotTargetRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(OneShotTargetEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(OneShotTargetEntity p_entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(p_entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
