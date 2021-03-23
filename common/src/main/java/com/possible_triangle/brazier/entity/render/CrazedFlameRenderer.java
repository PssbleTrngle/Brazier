package com.possible_triangle.brazier.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.entity.CrazedFlame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class CrazedFlameRenderer extends EntityRenderer<CrazedFlame> {

    public static final ModelResourceLocation MODEL = new ModelResourceLocation(new ResourceLocation(Brazier.MOD_ID, "living_flame"), "inventory");

    public CrazedFlameRenderer(EntityRenderDispatcher entityRendererManager) {
        super(entityRendererManager);
    }

    @Override
    protected int getBlockLightLevel(CrazedFlame entity, BlockPos partialTicks) {
        return 10;
    }

    @Override
    public ResourceLocation getTextureLocation(CrazedFlame entity) {
        return new ResourceLocation(Brazier.MOD_ID, "item/living_flame");
    }

    @Override
    public void render(CrazedFlame entity, float entityYaw, float partialTicks, PoseStack matrizes, MultiBufferSource buffer, int packedLightIn) {
        matrizes.pushPose();
        matrizes.translate(0, 0.5F, 0);
        float scale = 1.5F;
        matrizes.scale(scale, scale, scale);
        renderFlame(matrizes, this.entityRenderDispatcher, buffer, packedLightIn);
        matrizes.popPose();
        super.render(entity, entityYaw, partialTicks, matrizes, buffer, packedLightIn);
    }

    private static void vertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, int i, float f, int j, int k, int l) {
        vertexConsumer.vertex(matrix4f, f - 0.5F, (float)j - 0.25F, 0.0F).color(255, 255, 255, 255).uv((float)k, (float)l).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(i).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public static void renderFlame(PoseStack matrizes, EntityRenderDispatcher rendererManager, MultiBufferSource buffer, int packedLightIn) {
        Minecraft mc = Minecraft.getInstance();
        BlockRenderDispatcher dispatcher = mc.getBlockRenderer();
        ModelManager modelManager = mc.getModelManager();
        float scale = 0.6F;

        rendererManager.textureManager.bind(TextureAtlas.LOCATION_BLOCKS);

        matrizes.pushPose();
        matrizes.scale(scale, scale, scale);
        matrizes.mulPose(rendererManager.cameraOrientation());
        matrizes.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        matrizes.translate(-0.5F, -0.5F, -0.5F);
        dispatcher.getModelRenderer().renderModel(
                matrizes.last(), buffer.getBuffer(RenderType.cutout()),
                null, modelManager.getModel(MODEL), 1.0F, 1.0F, 1.0F,
                packedLightIn, OverlayTexture.NO_OVERLAY
        );
        matrizes.popPose();
    }

}
