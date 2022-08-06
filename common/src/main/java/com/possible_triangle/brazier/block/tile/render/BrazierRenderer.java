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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BrazierRenderer implements BlockEntityRenderer<BrazierTile> {

    public static final ResourceLocation TEXTURE_KEY = new ResourceLocation(Brazier.MOD_ID, "textures/block/brazier_runes.png");
    //public static final ResourceLocation TEXTURE_KEY = new ResourceLocation("textures/block/stone.png");

    public static final float SIZE = 0.25F;
    public static final float OFFSET = 0.02F;

    private RenderType createRenderType() {
        var glState = RenderType.CompositeState.builder()
                .setShaderState(RenderType.POSITION_TEX_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(TEXTURE_KEY, false, false))
                //.setTextureState(new RenderStateShard.TextureStateShard(new ResourceLocation("textures/block/diamond_block.png"), false, false))
                //.setTextureState(RenderStateShard.BLOCK_SHEET_MIPPED)
                //.setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                //.setCullState(RenderStateShard.NO_CULL)
                .setLightmapState(RenderStateShard.LIGHTMAP)
                //.setOverlayState(RenderStateShard.OVERLAY)
                .createCompositeState(false);

        return RenderType.create(
                Brazier.MOD_ID + ":runes",
                DefaultVertexFormat.POSITION_TEX,
                VertexFormat.Mode.QUADS,
                256, false, false, glState
        );
    }

    private static final int TEXTURE_HEIGHT = 9;
    private static final int FRAMES = 10;

    private void renderTop(Matrix4f matrix, VertexConsumer vertex, float minV, float maxV) {
        float maxU = 1.5F / TEXTURE_HEIGHT;
        vertex.vertex(matrix, 1.0F, OFFSET, -SIZE).uv(0F, minV).endVertex();
        vertex.vertex(matrix, 1.0F, OFFSET, SIZE).uv(0F, maxV).endVertex();
        vertex.vertex(matrix, 2.5F, OFFSET, SIZE).uv(maxU, maxV).endVertex();
        vertex.vertex(matrix, 2.5F, OFFSET, -SIZE).uv(maxU, minV).endVertex();
    }

    private void renderSide(Matrix4f matrix, VertexConsumer vertex, int height, float minV, float maxV) {
        int times = height / TEXTURE_HEIGHT;

        for (int i = 0; i <= times; i++) {
            float segment = Math.min(TEXTURE_HEIGHT, height - i * TEXTURE_HEIGHT);
            float offset = i * TEXTURE_HEIGHT * 1F;

            float maxU = segment / TEXTURE_HEIGHT;

            vertex.vertex(matrix, 2.50F + OFFSET, offset, -SIZE).uv(0F, minV).endVertex();
            vertex.vertex(matrix, 2.50F + OFFSET, offset, SIZE).uv(0F, maxV).endVertex();
            vertex.vertex(matrix, 2.50F + OFFSET, offset - segment, SIZE).uv(maxU, maxV).endVertex();
            vertex.vertex(matrix, 2.50F + OFFSET, offset - segment, -SIZE).uv(maxU, minV).endVertex();
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
            float frame = (float) ((System.currentTimeMillis() / 100) % FRAMES);
            float minV = frame / FRAMES;
            float maxV = (frame + 1) / FRAMES;

            for (int quarter = 0; quarter < 4; quarter++) {
                matrizes.mulPose(Vector3f.YN.rotationDegrees(90F));
                renderSide(matrix, vertex, height, minV, maxV);
                renderTop(matrix, vertex, minV, maxV);
            }
        }

        matrizes.translate(0, 1.4F, 0);
        renderFlame(matrizes, buffer, light);

        matrizes.popPose();
    }

    public RenderType renderType() {
        //return RenderType.endPortal();
        return createRenderType();
    }

}
