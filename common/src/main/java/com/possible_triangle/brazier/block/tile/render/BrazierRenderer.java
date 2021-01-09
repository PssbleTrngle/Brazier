package com.possible_triangle.brazier.block.tile.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.block.tile.BrazierTile;
import com.possible_triangle.brazier.config.BrazierConfig;
import com.possible_triangle.brazier.entity.render.CrazedFlameRenderer;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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
    public static final RenderType RENDER_TYPE;

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
            event.addSprite(new ResourceLocation(Brazier.MODID, "block/brazier_runes"));
        }
    }

    @SubscribeEvent
    public static void atlasStitch(TextureStitchEvent.Post event) {
        if (event.getMap().getTextureLocation().equals(PlayerContainer.LOCATION_BLOCKS_TEXTURE)) {
            RUNES = event.getMap().getSprite(new ResourceLocation(Brazier.MODID, "block/brazier_runes"));
        }
    }

    public BrazierRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    private void renderTop(MatrixStack matrizes, float alpha, IRenderTypeBuffer buffer, Vector3f vvvv) {
        matrizes.push();
        matrizes.translate(0, 0.05F, 0);
        Matrix4f matrix = matrizes.getLast().getMatrix();
        IVertexBuilder vertex = buffer.getBuffer(RENDER_TYPE);
        for (int i = 0; i < 4; i++) {
            float v = RUNES.getMaxU() - RUNES.getMinU();
            float start = v / 9F * i;
            float minU = RUNES.getMinU() + start;
            float maxU= minU + v / 9F * 1.5F;
            matrizes.rotate(Vector3f.YN.rotationDegrees(90F));
            vertex.pos(matrix, 1.0F, 0, -0.25F).color(1F, 1F, 1F, alpha).tex(maxU, RUNES.getMinV()).lightmap(0xF000F0).endVertex();
            vertex.pos(matrix, 1.0F, 0, +0.25F).color(1F, 1F, 1F, alpha).tex(maxU, RUNES.getMaxV()).lightmap(0xF000F0).endVertex();
            vertex.pos(matrix, 2.5F, 0, +0.25F).color(1F, 1F, 1F, alpha).tex(minU, RUNES.getMaxV()).lightmap(0xF000F0).endVertex();
            vertex.pos(matrix, 2.5F, 0, -0.25F).color(1F, 1F, 1F, alpha).tex(minU, RUNES.getMinV()).lightmap(0xF000F0).endVertex();
        }
        matrizes.pop();
    }

    private void renderSide(MatrixStack matrizes, float alpha, IRenderTypeBuffer buffer, Vector3f v, int height) {
        float segment = Math.min(9, height) - 0.25F;
        matrizes.push();
        matrizes.rotate(v.rotationDegrees(90F));
        if (v.getZ() == 0) matrizes.rotate(Vector3f.YP.rotationDegrees(90F));
        matrizes.translate(v.getX() + v.getZ() > 0 ? -segment : 0, 2.55F, 0);
        Matrix4f matrix = matrizes.getLast().getMatrix();
        IVertexBuilder vertex = buffer.getBuffer(RENDER_TYPE);
        float maxU = RUNES.getMinU() + segment * ((RUNES.getMaxU() - RUNES.getMinU()) / 9F);
        vertex.pos(matrix, 0, 0, -0.25F).color(1F, 1F, 1F, alpha).tex(maxU, RUNES.getMinV()).lightmap(0xF000F0).endVertex();
        vertex.pos(matrix, 0, 0, +0.25F).color(1F, 1F, 1F, alpha).tex(maxU, RUNES.getMaxV()).lightmap(0xF000F0).endVertex();
        vertex.pos(matrix, segment, 0, +0.25F).color(1F, 1F, 1F, alpha).tex(RUNES.getMinU(), RUNES.getMaxV()).lightmap(0xF000F0).endVertex();
        vertex.pos(matrix, segment, 0, -0.25F).color(1F, 1F, 1F, alpha).tex(RUNES.getMinU(), RUNES.getMinV()).lightmap(0xF000F0).endVertex();
        matrizes.pop();
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

            if(BrazierConfig.CLIENT.RENDER_RUNES.get()) {
                Stream.of(Vector3f.XP, Vector3f.XN, Vector3f.ZN, Vector3f.ZP).forEach(v -> {
                    renderSide(matrizes, 1F, buffer, v, height);
                    renderTop(matrizes, 1F, buffer, v);
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
