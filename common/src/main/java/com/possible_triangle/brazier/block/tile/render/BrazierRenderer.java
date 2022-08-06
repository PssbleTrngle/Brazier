package com.possible_triangle.brazier.block.tile.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.block.tile.BrazierTile;
import com.possible_triangle.brazier.entity.render.CrazedFlameRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BrazierRenderer implements BlockEntityRenderer<BrazierTile> {

    public static final ResourceLocation TEXTURE_KEY = new ResourceLocation(Brazier.MOD_ID, "textures/block/brazier_runes.png");

    private static final RenderType.CompositeState glState = RenderType.CompositeState.builder()
            .setShaderState(RenderType.RENDERTYPE_LIGHTNING_SHADER)
            .setTextureState(new RenderStateShard.TextureStateShard(TEXTURE_KEY, false, false))
            .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
            .setCullState(RenderStateShard.NO_CULL)
            .setLightmapState(RenderStateShard.LIGHTMAP)
            .setOverlayState(RenderStateShard.OVERLAY)
            .createCompositeState(false);

    public static final RenderType RENDER_TYPE = RenderType.create(
            Brazier.MOD_ID + ":runes",
            DefaultVertexFormat.POSITION,
            VertexFormat.Mode.QUADS,
            256, false, false, glState
    );

    private static final int TEXTURE_HEIGHT = 9;

    private void renderTop(Matrix4f matrix, VertexConsumer vertex) {
        vertex.vertex(matrix, 1.0F, 0.02F, -0.25F).endVertex();
        vertex.vertex(matrix, 1.0F, 0.02F, +0.25F).endVertex();
        vertex.vertex(matrix, 2.5F, 0.02F, +0.25F).endVertex();
        vertex.vertex(matrix, 2.5F, 0.02F, -0.25F).endVertex();
    }

    private void renderSide(Matrix4f matrix, VertexConsumer vertex, int height) {
        int times = height / TEXTURE_HEIGHT;

        for (int i = 0; i <= times; i++) {
            float segment = Math.min(TEXTURE_HEIGHT, height - i * TEXTURE_HEIGHT);
            float offset = i * TEXTURE_HEIGHT * 1F;

            vertex.vertex(matrix, 2.52F, offset, -0.25F).endVertex();
            vertex.vertex(matrix, 2.52F, offset, +0.25F).endVertex();
            vertex.vertex(matrix, 2.52F, offset - segment, +0.25F).endVertex();
            vertex.vertex(matrix, 2.52F, offset - segment, -0.25F).endVertex();
        }
    }

    private void renderFlame(PoseStack matrizes, MultiBufferSource buffer, int light) {
        CrazedFlameRenderer.renderFlame(matrizes, Minecraft.getInstance().getEntityRenderDispatcher(), buffer, light);
    }

    @Override
    public void render(BrazierTile tile, float partialTicks, @NotNull PoseStack matrizes, @NotNull MultiBufferSource buffer, int light, int overlay) {
        int height = tile.getHeight();
        if (height <= 0) return;

        matrizes.pushPose();
        matrizes.translate(0.5, 0, 0.5);

        var matrix = matrizes.last().pose();
        var vertex = buffer.getBuffer(renderType());

        if (Brazier.clientConfig().RENDER_RUNES) {
            for (int quarter = 0; quarter < 4; quarter++) {
                matrizes.mulPose(Vector3f.YN.rotationDegrees(90F));
                renderSide(matrix, vertex, height);
                renderTop(matrix, vertex);
            }
        }

        matrizes.translate(0, 1.4F, 0);
        renderFlame(matrizes, buffer, light);

        matrizes.popPose();
    }

    public RenderType renderType() {
        return RenderType.endPortal();
        //return RENDER_TYPE;
    }

}
