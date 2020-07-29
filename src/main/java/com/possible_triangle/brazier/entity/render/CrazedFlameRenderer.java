package com.possible_triangle.brazier.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.entity.CrazedFlame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class CrazedFlameRenderer extends EntityRenderer<CrazedFlame> {

    public static final ModelResourceLocation MODEL = new ModelResourceLocation(new ResourceLocation(Brazier.MODID, "living_flame"), "inventory");

    public CrazedFlameRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager);
    }

    @Override
    protected int getBlockLight(CrazedFlame entity, BlockPos partialTicks) {
        return 10;
    }

    @Override
    public ResourceLocation getEntityTexture(CrazedFlame entity) {
        return PlayerContainer.LOCATION_BLOCKS_TEXTURE;
    }

    @Override
    public void render(CrazedFlame entity, float entityYaw, float partialTicks, MatrixStack matrizes, IRenderTypeBuffer buffer, int packedLightIn) {
        renderFlame(matrizes, this.renderManager, buffer, packedLightIn);
    }

    public static void renderFlame(MatrixStack matrizes, EntityRendererManager rendererManager, IRenderTypeBuffer buffer, int packedLightIn) {

        Minecraft mc = Minecraft.getInstance();
        BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();
        ModelManager modelManager = mc.getModelManager();
        float scale = 0.6F;

        matrizes.push();
        matrizes.scale(scale, scale, scale);
        matrizes.rotate(rendererManager.getCameraOrientation());
        matrizes.rotate(Vector3f.YP.rotationDegrees(180.0F));
        matrizes.translate(-0.5F, -0.5F, -0.5F);
        dispatcher.getBlockModelRenderer().renderModelBrightnessColor(
                matrizes.getLast(), buffer.getBuffer(Atlases.getCutoutBlockType()),
                null, modelManager.getModel(MODEL), 1.0F, 1.0F, 1.0F,
                packedLightIn, OverlayTexture.NO_OVERLAY
        );
        matrizes.pop();
    }

}
