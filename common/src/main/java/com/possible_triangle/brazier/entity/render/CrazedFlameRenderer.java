package com.possible_triangle.brazier.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.entity.CrazedFlame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrazedFlameRenderer extends EntityRenderer<CrazedFlame> {

    public CrazedFlameRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLightLevel(@NotNull CrazedFlame entity, @NotNull BlockPos partialTicks) {
        return 10;
    }

    @Override
    public ResourceLocation getTextureLocation(@NotNull CrazedFlame entity) {
        return new ResourceLocation(Brazier.MOD_ID, "item/living_flame");
    }

    @Override
    public void render(@NotNull CrazedFlame entity, float entityYaw, float partialTicks, PoseStack matrizes, @NotNull MultiBufferSource buffer, int packedLightIn) {
        matrizes.pushPose();
        matrizes.translate(0, 0.5F, 0);
        float scale = 1.5F;
        matrizes.scale(scale, scale, scale);
        renderFlame(matrizes, this.entityRenderDispatcher, buffer, packedLightIn, entity.level());
        matrizes.popPose();
        super.render(entity, entityYaw, partialTicks, matrizes, buffer, packedLightIn);
    }

    public static void renderFlame(PoseStack matrizes, EntityRenderDispatcher rendererManager, MultiBufferSource buffers, int packedLightIn, @Nullable Level level) {
        matrizes.pushPose();

        var mc = Minecraft.getInstance();
        var renderer = mc.getItemRenderer();
        float scale = 2F;

        matrizes.translate(0F, -0.25F, 0F);
        matrizes.scale(scale, scale, scale);
        matrizes.mulPose(rendererManager.cameraOrientation());
        matrizes.mulPose(Axis.YP.rotationDegrees(180.0F));

        Content.LIVING_FLAME.ifPresent(flame ->
                renderer.renderStatic(new ItemStack(flame), ItemDisplayContext.GROUND, packedLightIn, 0, matrizes, buffers, level, 0)
        );

        matrizes.popPose();
    }

}
