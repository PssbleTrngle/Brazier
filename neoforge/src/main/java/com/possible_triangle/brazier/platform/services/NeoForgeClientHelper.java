package com.possible_triangle.brazier.platform.services;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.possible_triangle.brazier.BrazierConstants;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent;

@EventBusSubscriber(Dist.CLIENT)
public class NeoForgeClientHelper implements IClientHelper {

    @Override
    public RenderType createRunesRenderType(ResourceLocation texture) {
        return RenderType.create(
                BrazierConstants.MOD_ID + ":runes",
                DefaultVertexFormat.BLOCK,
                VertexFormat.Mode.QUADS,
                786432, true, false,
                RenderType.CompositeState.builder()
                        .setShaderState(RenderType.RENDERTYPE_CUTOUT_SHADER)
                        .setTextureState(RenderType.BLOCK_SHEET)
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .createCompositeState(false)
        );
    }

    private static TextureAtlasSprite RUNE_SPRITE = null;

    @SubscribeEvent
    private static void stichAtlas(TextureAtlasStitchedEvent event) {
        if (event.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS)) {
            RUNE_SPRITE = event.getAtlas().getSprite(RUNE_SPRITE_NAME);
        }
    }

    @Override
    public TextureAtlasSprite getRuneSprite() {
        return RUNE_SPRITE;
    }

}
