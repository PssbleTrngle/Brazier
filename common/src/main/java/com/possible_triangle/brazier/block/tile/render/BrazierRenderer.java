package com.possible_triangle.brazier.block.tile.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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
import net.minecraft.client.renderer.RenderStateShard.DiffuseLightingStateShard;
import net.minecraft.client.renderer.RenderStateShard.LightmapStateShard;
import net.minecraft.client.renderer.RenderStateShard.TextureStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.function.Consumer;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BrazierRenderer extends BlockEntityRenderer<BrazierTile> {

    public static TextureAtlasSprite RUNES;
    public static final RenderType RENDER_TYPE;
    private static final int TEXTURE_HEIGHT = 9;
    public static String TEXTURE_KEY = "block/brazier_runes";

    public BrazierRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    static {
        RenderType.CompositeState glState;
        glState = RenderType.CompositeState.builder().setTextureState(new TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, true))
                // .setTransparencyState(.getPrivateValue(RenderState.class, null, "field_228515_g_"))
                .setDiffuseLightingState(new DiffuseLightingStateShard(true))
                .setAlphaState(new RenderStateShard.AlphaStateShard(0.004F))
                .setLightmapState(new LightmapStateShard(true))
                .createCompositeState(false);
        RENDER_TYPE = RenderType.create(Brazier.MOD_ID + ":runes", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, GL11.GL_QUADS, 128, glState);
    }

    public static void atlasStitch(TextureAtlas atlas, Consumer<ResourceLocation> sprites) {
        if (atlas.location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            sprites.accept(new ResourceLocation(Brazier.MOD_ID, TEXTURE_KEY));
        }
    }

    public static void atlasStitch(TextureAtlas atlas) {
        if (atlas.location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            RUNES = atlas.getSprite(new ResourceLocation(Brazier.MOD_ID, TEXTURE_KEY));
        }
    }

    private void renderTop(PoseStack matrizes, float alpha, MultiBufferSource buffer) {
        matrizes.pushPose();
        matrizes.translate(0, 0.05F, 0);
        Matrix4f matrix = matrizes.last().pose();
        VertexConsumer vertex = buffer.getBuffer(RENDER_TYPE);
        for (int i = 0; i < 4; i++) {
            float v = RUNES.getU1() - RUNES.getU0();
            float start = v / 9F * i * 2;
            float minU = RUNES.getU0() + start;
            float maxU = minU + v / 9F * 1.5F;

            matrizes.mulPose(Vector3f.YN.rotationDegrees(90F));

            vertex.vertex(matrix, 1.0F, 0, -0.25F).color(1F, 1F, 1F, alpha).uv(maxU, RUNES.getV0()).uv2(0xF000F0).endVertex();
            vertex.vertex(matrix, 1.0F, 0, +0.25F).color(1F, 1F, 1F, alpha).uv(maxU, RUNES.getV1()).uv2(0xF000F0).endVertex();
            vertex.vertex(matrix, 2.5F, 0, +0.25F).color(1F, 1F, 1F, alpha).uv(minU, RUNES.getV1()).uv2(0xF000F0).endVertex();
            vertex.vertex(matrix, 2.5F, 0, -0.25F).color(1F, 1F, 1F, alpha).uv(minU, RUNES.getV0()).uv2(0xF000F0).endVertex();

        }
        matrizes.popPose();
    }

    private void renderSide(PoseStack matrizes, float alpha, MultiBufferSource buffer, int height) {
        matrizes.pushPose();
        int times = height / TEXTURE_HEIGHT;

        for (int quarter = 0; quarter < 4; quarter++) {
            matrizes.mulPose(Vector3f.YN.rotationDegrees(90F));

            for (int i = 0; i <= times; i++) {
                float segment = Math.min(TEXTURE_HEIGHT, height - i * TEXTURE_HEIGHT);
                float offset = i * TEXTURE_HEIGHT;

                matrizes.pushPose();
                matrizes.mulPose(Vector3f.ZN.rotationDegrees(90F));
                matrizes.translate(0, 2.55F, 0);

                Matrix4f matrix = matrizes.last().pose();
                VertexConsumer vertex = buffer.getBuffer(RENDER_TYPE);
                float maxU = RUNES.getU0() + segment * ((RUNES.getU1() - RUNES.getU0()) / ((float) TEXTURE_HEIGHT));

                vertex.vertex(matrix, offset, 0, -0.25F).color(1F, 1F, 1F, alpha).uv(RUNES.getU0(), RUNES.getV0()).uv2(0xF000F0).endVertex();
                vertex.vertex(matrix, offset, 0, +0.25F).color(1F, 1F, 1F, alpha).uv(RUNES.getU0(), RUNES.getV1()).uv2(0xF000F0).endVertex();
                vertex.vertex(matrix, offset + segment, 0, +0.25F).color(1F, 1F, 1F, alpha).uv(maxU, RUNES.getV1()).uv2(0xF000F0).endVertex();
                vertex.vertex(matrix, offset + segment, 0, -0.25F).color(1F, 1F, 1F, alpha).uv(maxU, RUNES.getV0()).uv2(0xF000F0).endVertex();

                matrizes.popPose();
            }

        }

        matrizes.popPose();
    }

    private void renderFlame(PoseStack matrizes, MultiBufferSource buffer, int light) {
        CrazedFlameRenderer.renderFlame(matrizes, Minecraft.getInstance().getEntityRenderDispatcher(), buffer, light);
    }

    @Override
    public void render(BrazierTile tile, float partialTicks, PoseStack matrizes, MultiBufferSource buffer, int light, int overlay) {
        int height = tile.getHeight();
        float alpha = 1.0F;

        if (height > 0) {

            matrizes.pushPose();
            matrizes.translate(0.5, 0, 0.5);

            if (Brazier.CLIENT_CONFIG.get().RENDER_RUNES) {
                Stream.of(Vector3f.XP, Vector3f.XN, Vector3f.ZN, Vector3f.ZP).forEach(v -> {
                    renderSide(matrizes, alpha, buffer, height);
                    renderTop(matrizes, alpha, buffer);
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
