package com.possible_triangle.brazier.world.block.tile.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.platform.Services;
import com.possible_triangle.brazier.world.block.tile.BrazierBlockEntity;
import com.possible_triangle.brazier.world.entity.render.CrazedFlameRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class BrazierRenderer implements BlockEntityRenderer<BrazierBlockEntity> {

    private static final RenderType RENDER_TYPE = Services.Client.PLATFORM.createRunesRenderType(Brazier.createId("textures/block/brazier_runes.png"));

    public static final float SIZE = 0.25F;
    public static final float OFFSET = 0.02F;

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
            float offset = i * TEXTURE_HEIGHT * -1F;

            float maxU = segment / TEXTURE_HEIGHT;

            vertex.vertex(matrix, 2.50F + OFFSET, offset, -SIZE).uv(0F, minV).endVertex();
            vertex.vertex(matrix, 2.50F + OFFSET, offset, SIZE).uv(0F, maxV).endVertex();
            vertex.vertex(matrix, 2.50F + OFFSET, offset - segment, SIZE).uv(maxU, maxV).endVertex();
            vertex.vertex(matrix, 2.50F + OFFSET, offset - segment, -SIZE).uv(maxU, minV).endVertex();
        }
    }

    private void renderFlame(PoseStack matrizes, MultiBufferSource buffer, int light, @Nullable Level level) {
        CrazedFlameRenderer.renderFlame(matrizes, Minecraft.getInstance().getEntityRenderDispatcher(), buffer, light, level);
    }

    @Override
    public void render(BrazierBlockEntity tile, float partialTicks, @NotNull PoseStack matrizes, @NotNull MultiBufferSource buffer, int light, int overlay) {
        int height = tile.getHeight();
        if (height <= 0) return;

        matrizes.pushPose();
        matrizes.translate(0.5, 0, 0.5);

        var matrix = matrizes.last().pose();
        var vertex = buffer.getBuffer(RENDER_TYPE);

        if (Services.CONFIGS.client().renderRunes()) {
            float frame = (float) ((System.currentTimeMillis() / 100) % FRAMES);
            float minV = frame / FRAMES;
            float maxV = (frame + 1) / FRAMES;

            for (int quarter = 0; quarter < 4; quarter++) {
                matrizes.mulPose(Axis.YN.rotationDegrees(90F));
                renderSide(matrix, vertex, height, minV, maxV);
                renderTop(matrix, vertex, minV, maxV);
            }
        }

        matrizes.translate(0, 1.4F, 0);
        renderFlame(matrizes, buffer, light, tile.getLevel());

        matrizes.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(BrazierBlockEntity tile) {
        return Services.CONFIGS.client().renderRunes();
    }

}
