package com.possible_triangle.brazier.platform.services;

import com.possible_triangle.brazier.BrazierConstants;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public interface IClientHelper {

    ResourceLocation RUNE_SPRITE_NAME = BrazierConstants.createId("block/brazier_runes");

    RenderType createRunesRenderType(ResourceLocation texture);

    TextureAtlasSprite getRuneSprite();
}
