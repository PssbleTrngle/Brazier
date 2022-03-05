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
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderStateShard.TextureStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BrazierRenderer implements BlockEntityRenderer<BrazierTile> {

    public BrazierRenderer() {}

    public static final RenderType RENDER_TYPE;
    private static final int TEXTURE_HEIGHT = 9;
    public static final ResourceLocation TEXTURE_KEY = new ResourceLocation(Brazier.MOD_ID, "textures/block/brazier_runes.png");

    static {
        RenderType.CompositeState glState;
        glState = RenderType.CompositeState.builder()
                .setTextureState(new TextureStateShard(TEXTURE_KEY, false, false))
                // .setTransparencyState(.getPrivateValue(RenderState.class, null, "field_228515_g_"))
                // .setDiffuseLightingState(new DiffuseLightingStateShard(true))
                // .setAlphaState(new RenderStateShard.AlphaStateShard(0.004F))
                .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeCutoutShader))
                //.setLightmapState(new LightmapStateShard(true))
                .createCompositeState(false);

        RENDER_TYPE = RenderType.create(Brazier.MOD_ID + ":runes", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 128, false, false, glState);
    }

    private void renderTop(PoseStack matrizes, MultiBufferSource buffer) {
        matrizes.pushPose();
        matrizes.translate(0, 0.05F, 0);
        Matrix4f matrix = matrizes.last().pose();
        VertexConsumer vertex = buffer.getBuffer(RENDER_TYPE);
        for (int i = 0; i < 4; i++) {
            matrizes.mulPose(Vector3f.YN.rotationDegrees(90F));

            vertex.vertex(matrix, 1.0F, 0, -0.25F).endVertex();
            vertex.vertex(matrix, 1.0F, 0, +0.25F).endVertex();
            vertex.vertex(matrix, 2.5F, 0, +0.25F).endVertex();
            vertex.vertex(matrix, 2.5F, 0, -0.25F).endVertex();
        }
        matrizes.popPose();
    }

    private void renderSide(PoseStack matrizes, MultiBufferSource buffer, int height) {
        matrizes.pushPose();
        int times = height / TEXTURE_HEIGHT;

        Matrix4f matrix = matrizes.last().pose();
        VertexConsumer vertex = buffer.getBuffer(RENDER_TYPE);

        for (int quarter = 0; quarter < 4; quarter++) {
            matrizes.mulPose(Vector3f.YN.rotationDegrees(90F));

            for (int i = 0; i <= times; i++) {
                float segment = Math.min(TEXTURE_HEIGHT, height - i * TEXTURE_HEIGHT);
                float offset = i * TEXTURE_HEIGHT * 1F;

                matrizes.pushPose();
                matrizes.mulPose(Vector3f.ZN.rotationDegrees(90F));
                matrizes.translate(0, 2.55F, 0);

                vertex.vertex(matrix, offset, 0.01F, -0.25F).endVertex();
                vertex.vertex(matrix, offset, 0.01F, +0.25F).endVertex();
                vertex.vertex(matrix, offset + segment, 0.01F, +0.25F).endVertex();
                vertex.vertex(matrix, offset + segment, 0.01F, -0.25F).endVertex();

                matrizes.popPose();
            }

        }

        matrizes.popPose();
    }

    private void renderFlame(PoseStack matrizes, MultiBufferSource buffer, int light) {
        CrazedFlameRenderer.renderFlame(matrizes, Minecraft.getInstance().getEntityRenderDispatcher(), buffer, light);
    }

    @Override
    public void render(BrazierTile tile, float partialTicks, @NotNull PoseStack matrizes, @NotNull MultiBufferSource buffer, int light, int overlay) {
        int height = tile.getHeight();

        if (height > 0) {

            matrizes.pushPose();
            matrizes.translate(0.5, 0, 0.5);

            if (Brazier.clientConfig().RENDER_RUNES) {
                Stream.of(Vector3f.XP, Vector3f.XN, Vector3f.ZN, Vector3f.ZP).forEach(v -> {
                    renderSide(matrizes,  buffer, height);
                    renderTop(matrizes, buffer);
                });
            }

            matrizes.pushPose();
            matrizes.translate(0, 1.4F, 0);
            renderFlame(matrizes, buffer, light);
            matrizes.popPose();

            matrizes.popPose();

        }
    }

}
