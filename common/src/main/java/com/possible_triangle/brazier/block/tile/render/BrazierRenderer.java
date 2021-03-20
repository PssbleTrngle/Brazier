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
import org.lwjgl.opengl.GL11;

import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BrazierRenderer extends BlockEntityRenderer<BrazierTile> {

    public static TextureAtlasSprite RUNES;
    public static final RenderType RENDER_TYPE;

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
        RENDER_TYPE = RenderType.create(Brazier.MODID + ":runes", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, GL11.GL_QUADS, 128, glState);
    }

    /*
    @SubscribeEvent
    public static void atlasStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation().equals(PlayerContainer.LOCATION_BLOCKS_TEXTURE)) {
            event.addSprite(new ResourceLocation(Brazier.MODID, "block/brazier_runes"));
        }
    }

    @SubscribeEvent
    public static void atlasStitch(TextureStitchEvent.vertext event) {
        if (event.getMap().getTextureLocation().equals(PlayerContainer.LOCATION_BLOCKS_TEXTURE)) {
            RUNES = event.getMap().getSprite(new ResourceLocation(Brazier.MODID, "block/brazier_runes"));
        }
    }
    */

    private void renderTop(PoseStack matrizes, float alpha, MultiBufferSource buffer, Vector3f vvvv) {
        matrizes.pushPose();
        matrizes.translate(0, 0.05F, 0);
        Matrix4f matrix = matrizes.last().pose();
        VertexConsumer vertex = buffer.getBuffer(RENDER_TYPE);
        for (int i = 0; i < 4; i++) {
            float v = RUNES.getU1() - RUNES.getU0();
            float start = v / 9F * i;
            float minU = RUNES.getU0() + start;
            float maxU= minU + v / 9F * 1.5F;
            matrizes.mulPose(Vector3f.YN.rotationDegrees(90F));
            vertex.vertex(matrix, 1.0F, 0, -0.25F).color(1F, 1F, 1F, alpha).uv(maxU, RUNES.getV0()).uv2(0xF000F0).endVertex();
            vertex.vertex(matrix, 1.0F, 0, +0.25F).color(1F, 1F, 1F, alpha).uv(maxU, RUNES.getV1()).uv2(0xF000F0).endVertex();
            vertex.vertex(matrix, 2.5F, 0, +0.25F).color(1F, 1F, 1F, alpha).uv(minU, RUNES.getV1()).uv2(0xF000F0).endVertex();
            vertex.vertex(matrix, 2.5F, 0, -0.25F).color(1F, 1F, 1F, alpha).uv(minU, RUNES.getV0()).uv2(0xF000F0).endVertex();
        }
        matrizes.popPose();
    }

    private void renderSide(PoseStack matrizes, float alpha, MultiBufferSource buffer, Vector3f v, int height) {
        float segment = Math.min(9, height) - 0.25F;
        matrizes.pushPose();
        matrizes.mulPose(v.rotationDegrees(90F));
        if (v.z() == 0) matrizes.mulPose(Vector3f.YP.rotationDegrees(90F));
        matrizes.translate(v.x() + v.z() > 0 ? -segment : 0, 2.55F, 0);
        Matrix4f matrix = matrizes.last().pose();
        VertexConsumer vertex = buffer.getBuffer(RENDER_TYPE);
        float maxU = RUNES.getU0() + segment * ((RUNES.getU1() - RUNES.getU0()) / 9F);
        vertex.vertex(matrix, 0, 0, -0.25F).color(1F, 1F, 1F, alpha).uv(maxU, RUNES.getV0()).uv2(0xF000F0).endVertex();
        vertex.vertex(matrix, 0, 0, +0.25F).color(1F, 1F, 1F, alpha).uv(maxU, RUNES.getV1()).uv2(0xF000F0).endVertex();
        vertex.vertex(matrix, segment, 0, +0.25F).color(1F, 1F, 1F, alpha).uv(RUNES.getU1(), RUNES.getV1()).uv2(0xF000F0).endVertex();
        vertex.vertex(matrix, segment, 0, -0.25F).color(1F, 1F, 1F, alpha).uv(RUNES.getU0(), RUNES.getV0()).uv2(0xF000F0).endVertex();
        matrizes.popPose();
    }

    private void renderFlame(PoseStack matrizes, MultiBufferSource buffer, int light) {
        CrazedFlameRenderer.renderFlame(matrizes, Minecraft.getInstance().getEntityRenderDispatcher(), buffer, light);
    }

    @Override
    public void render(BrazierTile tile, float partialTicks, PoseStack matrizes, MultiBufferSource buffer, int light, int overlay) {
        int height = tile.getHeight();
        if (height > 0) {

            matrizes.pushPose();
            matrizes.translate(0.5, 0, 0.5);

            if(Brazier.CLIENT_CONFIG.get().RENDER_RUNES) {
                Stream.of(Vector3f.XP, Vector3f.XN, Vector3f.ZN, Vector3f.ZP).forEach(v -> {
                    renderSide(matrizes, 1F, buffer, v, height);
                    renderTop(matrizes, 1F, buffer, v);
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
