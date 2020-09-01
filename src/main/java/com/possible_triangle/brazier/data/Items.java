package com.possible_triangle.brazier.data;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.stream.Stream;

public class Items extends ItemModelProvider {

    public Items(DataGenerator generator, ExistingFileHelper fileHelper) {
        super(generator, Brazier.MODID, fileHelper);
    }

    @Override
    protected void registerModels() {

        Content.BRAZIER.map(ForgeRegistryEntry::getRegistryName)
                .map(ResourceLocation::getPath)
                .ifPresent(b -> this.withExistingParent(b, modLoc("block/" + b)));

        Stream.of(Content.LIVING_FLAME).forEach(c -> c
                .map(ForgeRegistryEntry::getRegistryName)
                .map(ResourceLocation::getPath)
                .ifPresent(i -> singleTexture(i, mcLoc("item/generated"), "layer0", modLoc("item/" + i)))
        );

        Content.LIVING_TORCH
                .map(ForgeRegistryEntry::getRegistryName)
                .map(ResourceLocation::getPath)
                .ifPresent(i -> singleTexture(i, mcLoc("item/generated"), "layer0", modLoc("block/" + i)));

        Content.CRAZED_SPAWN_EGG
                .map(ForgeRegistryEntry::getRegistryName)
                .map(ResourceLocation::getPath)
                .ifPresent(i -> withExistingParent(i, mcLoc("item/template_spawn_egg")));

    }
}
