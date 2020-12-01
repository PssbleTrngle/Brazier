package com.possible_triangle.brazier.block.tile.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.block.tile.BrazierTile;
import com.possible_triangle.brazier.config.BrazierConfig;
import com.possible_triangle.brazier.entity.render.CrazedFlameRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.opengl.GL11;

import java.util.stream.Stream;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BrazierRenderer extends TileEntityRenderer<BrazierTile> {

    public static TextureAtlasSprite RUNES;
    public static String TEXTURE_KEY = "block/brazier_runes_debug";
    public static final RenderType RENDER_TYPE;

    private static final int TEXTURE_HEIGHT = 9;

    static {
        RenderType.State glState = RenderType.State.getBuilder().texture(new RenderState.TextureState(PlayerContainer.LOCATION_BLOCKS_TEXTURE, false, true))
                .transparency(ObfuscationReflectionHelper.getPrivateValue(RenderState.class, null, "field_228515_g_"))
                .diffuseLighting(new RenderState.DiffuseLightingState(true))
                .alpha(new RenderState.AlphaState(0.004F))
                .lightmap(new RenderState.LightmapState(true))
                .build(true);
        RENDER_TYPE = RenderType.makeType(Brazier.MODID + ":runes", DefaultVertexFormats.POSITION_COLOR_TEX_LIGHTMAP, GL11.GL_QUADS, 128, glState);
    }

    @SubscribeEvent
    public static void atlasStitch(TextureStitchEvent.Pre event) {
        if (event.getMap().getTextureLocation().equals(PlayerContainer.LOCATION_BLOCKS_TEXTURE)) {
            event.addSprite(new ResourceLocation(Brazier.MODID, TEXTURE_KEY));
        }
    }

    @SubscribeEvent
    public static void atlasStitch(TextureStitchEvent.Post event) {
        if (event.getMap().getTextureLocation().equals(PlayerContainer.LOCATION_BLOCKS_TEXTURE)) {
            RUNES = event.getMap().getSprite(new ResourceLocation(Brazier.MODID, TEXTURE_KEY));
        }
    }

    public BrazierRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    private void renderTop(MatrixStack matrizes, float alpha, IRenderTypeBuffer buffer) {
        matrizes.push();
        matrizes.translate(0, 0.05F, 0);
        Matrix4f matrix = matrizes.getLast().getMatrix();
        IVertexBuilder vertex = buffer.getBuffer(RENDER_TYPE);
        for (int i = 0; i < 4; i++) {
            float v = RUNES.getMaxU() - RUNES.getMinU();
            float start = v / 9F * i;
            float minU = RUNES.getMinU() + start;
            float maxU = minU + v / 9F * 1.5F;

            matrizes.rotate(Vector3f.YN.rotationDegrees(90F));

            vertex.pos(matrix, 1.0F, 0, -0.25F).color(1F, 1F, 1F, alpha).tex(maxU, RUNES.getMinV()).lightmap(0xF000F0).endVertex();
            vertex.pos(matrix, 1.0F, 0, +0.25F).color(1F, 1F, 1F, alpha).tex(maxU, RUNES.getMaxV()).lightmap(0xF000F0).endVertex();
            vertex.pos(matrix, 2.5F, 0, +0.25F).color(1F, 1F, 1F, alpha).tex(minU, RUNES.getMaxV()).lightmap(0xF000F0).endVertex();
            vertex.pos(matrix, 2.5F, 0, -0.25F).color(1F, 1F, 1F, alpha).tex(minU, RUNES.getMinV()).lightmap(0xF000F0).endVertex();

        }
        matrizes.pop();
    }

    private void renderSide(MatrixStack matrizes, float alpha, IRenderTypeBuffer buffer, Vector3f side, int height) {
        int times = height / TEXTURE_HEIGHT;

        for (int i = 0; i <= times; i++) {
            float segment = Math.min(TEXTURE_HEIGHT, height - i * TEXTURE_HEIGHT);
            float offset = i * TEXTURE_HEIGHT;

            matrizes.push();
            matrizes.rotate(side.rotationDegrees(90F));
            if (side.getZ() == 0) matrizes.rotate(Vector3f.YP.rotationDegrees(90F));
            matrizes.translate(side.getX() + side.getZ() > 0 ? -segment : 0, 2.55F, 0);

            Matrix4f matrix = matrizes.getLast().getMatrix();
            IVertexBuilder vertex = buffer.getBuffer(RENDER_TYPE);
            float maxU = RUNES.getMinU() + segment * ((RUNES.getMaxU() - RUNES.getMinU()) / ((float) TEXTURE_HEIGHT));
            float startU = side.getZ() == 0 ? RUNES.getMinU() : maxU;
            float endU = side.getZ() == 0 ? maxU : RUNES.getMinU();

            vertex.pos(matrix, offset, 0, -0.25F).color(1F, 1F, 1F, alpha).tex(startU, RUNES.getMinV()).lightmap(0xF000F0).endVertex();
            vertex.pos(matrix, offset, 0, +0.25F).color(1F, 1F, 1F, alpha).tex(startU, RUNES.getMaxV()).lightmap(0xF000F0).endVertex();
            vertex.pos(matrix, offset + segment, 0, +0.25F).color(1F, 1F, 1F, alpha).tex(endU, RUNES.getMaxV()).lightmap(0xF000F0).endVertex();
            vertex.pos(matrix, offset + segment, 0, -0.25F).color(1F, 1F, 1F, alpha).tex(endU, RUNES.getMinV()).lightmap(0xF000F0).endVertex();

            matrizes.pop();
        }

    }

    private void renderFlame(MatrixStack matrizes, IRenderTypeBuffer buffer, int light) {
        CrazedFlameRenderer.renderFlame(matrizes, Minecraft.getInstance().getRenderManager(), buffer, light);
    }

    @Override
    public void render(BrazierTile tile, float partialTicks, MatrixStack matrizes, IRenderTypeBuffer buffer, int light, int overlay) {
        int height = tile.getHeight();
        if (height > 0) {

            matrizes.push();
            matrizes.translate(0.5, 0, 0.5);

            if (BrazierConfig.CLIENT.RENDER_RUNES.get()) {
                renderTop(matrizes, 1F, buffer);
                Stream.of(Vector3f.XP, Vector3f.XN, Vector3f.ZN, Vector3f.ZP).forEach(v -> {
                    renderSide(matrizes, 1F, buffer, v, height);
                });
            }

            matrizes.push();
            matrizes.translate(0, 1.4F, 0);
            renderFlame(matrizes, buffer, light);
            matrizes.pop();

            matrizes.pop();

        }
    }
}
