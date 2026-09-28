package com.possible_triangle.brazier.platform.services;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.possible_triangle.brazier.BrazierConstants;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class ForgeClientHelper implements IClientHelper {

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

}
