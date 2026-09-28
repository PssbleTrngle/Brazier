package com.possible_triangle.brazier.platform.services;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.possible_triangle.brazier.BrazierConstants;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public class FabricClientHelper implements IClientHelper {

    @Override
    public RenderType createRunesRenderType(ResourceLocation texture) {
        return RenderType.create(
                BrazierConstants.MOD_ID + ":runes",
                DefaultVertexFormat.POSITION_TEX,
                VertexFormat.Mode.QUADS,
                256, false, false,
                RenderType.CompositeState.builder()
                        .setShaderState(RenderType.POSITION_TEX_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .createCompositeState(false)
        );
    }

    private static TextureAtlasSprite RUNE_SPRITE = null;

    @Override
    public TextureAtlasSprite getRuneSprite() {
        return RUNE_SPRITE;
    }

    public static void onAtlasReload(TextureAtlas atlas) {
        RUNE_SPRITE = atlas.getSprite(RUNE_SPRITE_NAME);
    }

}
