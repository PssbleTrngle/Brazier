package com.possible_triangle.brazier.data;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Objects;

public class Items extends ItemModelProvider {

    public Items(DataGenerator generator, ExistingFileHelper fileHelper) {
        super(generator, Brazier.MODID, fileHelper);
    }

    @Override
    protected void registerModels() {

        Content.BRAZIER.map(ForgeRegistryEntry::getRegistryName)
                .map(ResourceLocation::getPath)
                .ifPresent(b -> this.withExistingParent(b, modLoc("block/" + b)));

        Content.LIVING_FLAME
                .map(ForgeRegistryEntry::getRegistryName)
                .map(ResourceLocation::getPath)
                .ifPresent(i -> singleTexture(i, mcLoc("item/generated"), "layer0", modLoc("item/" + i)));

    }
}
