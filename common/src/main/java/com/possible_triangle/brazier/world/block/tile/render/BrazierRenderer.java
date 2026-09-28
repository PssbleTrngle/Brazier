package com.possible_triangle.brazier.world.block.tile.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.possible_triangle.brazier.BrazierConstants;
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

    private static final RenderType RENDER_TYPE = Services.Client.PLATFORM.createRunesRenderType(BrazierConstants.createId("textures/block/brazier_runes.png"));

    public static final float SIZE = 0.25F;
    public static final float OFFSET = 0.001F;

    private static final float RADIUS = 2.5F;
    private static final int RUNE_COUNT = 9;
    private static final int FRAMES = 10;
    private static final float PIXEL = 1 / 16F;

    @Override
    public void render(BrazierBlockEntity tile, float partialTicks, @NotNull PoseStack matrizes, @NotNull MultiBufferSource buffer, int light, int overlay) {
        int height = tile.getHeight();
        if (height <= 0) return;

        matrizes.pushPose();
        matrizes.translate(0.5, 0, 0.5);

        var matrix = matrizes.last().pose();
        var vertex = buffer.getBuffer(RENDER_TYPE);


        if (Services.CONFIGS.client().renderRunes()) {
            float frame = (float) ((System.currentTimeMillis() / 240) % FRAMES);

            for (int quarter = 0; quarter < 4; quarter++) {
                matrizes.mulPose(Axis.YN.rotationDegrees(90F));
                int offset = quarter * 2;

                renderTop(light, vertex, matrix, offset);
                renderSide(light, vertex, matrix, height, offset + 2);
            }
        }

        matrizes.translate(0, 1.4F, 0);
        renderFlame(matrizes, buffer, light, tile.getLevel());

        matrizes.popPose();
    }

    private void renderFlame(PoseStack matrizes, MultiBufferSource buffer, int light, @Nullable Level level) {
        CrazedFlameRenderer.renderFlame(matrizes, Minecraft.getInstance().getEntityRenderDispatcher(), buffer, light, level);
    }

    private void renderSide(int light, VertexConsumer vertex, Matrix4f matrix, int height, int uOffset) {
        var sprite = Services.Client.PLATFORM.getRuneSprite();
        var color = 0xFFFFFFFF;

        var yFrom = 0;

        do {
            var segmentHeight = Math.min(RUNE_COUNT - uOffset, height);
            int textureEnd = uOffset + segmentHeight;
            float minU = ((float) uOffset) / RUNE_COUNT;
            float maxU = ((float) textureEnd) / RUNE_COUNT;
            var yTo = yFrom - segmentHeight;

            vertex.addVertex(matrix, RADIUS + OFFSET, yFrom, -SIZE).setNormal(0, 0, 0).setColor(color).setUv2(light, light).setUv(sprite.getU(minU), sprite.getV(0));
            vertex.addVertex(matrix, RADIUS + OFFSET, yFrom, SIZE).setNormal(0, 0, 0).setColor(color).setUv2(light, light).setUv(sprite.getU(minU), sprite.getV(1));
            vertex.addVertex(matrix, RADIUS + OFFSET, yTo, SIZE).setNormal(0, 0, 0).setColor(color).setUv2(light, light).setUv(sprite.getU(maxU), sprite.getV(1));
            vertex.addVertex(matrix, RADIUS + OFFSET, yTo, -SIZE).setNormal(0, 0, 0).setColor(color).setUv2(light, light).setUv(sprite.getU(maxU), sprite.getV(0));

            height -= segmentHeight;
            uOffset = 0;
            yFrom = yTo;

        } while (height > 0);

    }

    private static void renderTop(int light, VertexConsumer vertex, Matrix4f matrix, int uOffset) {
        float minU = ((float) uOffset) / RUNE_COUNT;
        float maxU = 1.5F / RUNE_COUNT + minU;

        var sprite = Services.Client.PLATFORM.getRuneSprite();
        var color = 0xFFFFFFFF;
        vertex.addVertex(matrix, 1.0F - PIXEL, OFFSET, -SIZE).setNormal(0, 0, 0).setColor(color).setUv2(light, light).setUv(sprite.getU(minU), sprite.getV(0));
        vertex.addVertex(matrix, 1.0F - PIXEL, OFFSET, SIZE).setNormal(0, 0, 0).setColor(color).setUv2(light, light).setUv(sprite.getU(minU), sprite.getV(1));
        vertex.addVertex(matrix, RADIUS - PIXEL, OFFSET, SIZE).setNormal(0, 0, 0).setColor(color).setUv2(light, light).setUv(sprite.getU(maxU), sprite.getV(1));
        vertex.addVertex(matrix, RADIUS - PIXEL, OFFSET, -SIZE).setNormal(0, 0, 0).setColor(color).setUv2(light, light).setUv(sprite.getU(maxU), sprite.getV(0));
    }

    @Override
    public boolean shouldRenderOffScreen(BrazierBlockEntity tile) {
        return Services.CONFIGS.client().renderRunes();
    }

}
