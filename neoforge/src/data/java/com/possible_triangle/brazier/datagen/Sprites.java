package com.possible_triangle.brazier.datagen;

import com.possible_triangle.brazier.BrazierConstants;
import com.possible_triangle.brazier.platform.services.IClientHelper;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;

public class Sprites extends SpriteSourceProvider {

    public Sprites(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, BrazierConstants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void gather() {
        atlas(BLOCKS_ATLAS).addSource(new SingleFile(
                IClientHelper.RUNE_SPRITE_NAME,
                Optional.empty()
        ));
    }
}
