package com.possible_triangle.brazier.forge.data.providers;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.stream.Stream;

public class Items extends ItemModelProvider {

    public Items(DataGenerator generator, ExistingFileHelper fileHelper) {
        super(generator, Brazier.MOD_ID, fileHelper);
    }

    @Override
    protected void registerModels() {

        Content.BRAZIER.toOptional().map(ForgeRegistryEntry::getRegistryName)
                .map(ResourceLocation::getPath)
                .ifPresent(b -> this.withExistingParent(b, modLoc("block/" + b)));

        Stream.of(Content.LIVING_FLAME, Content.SPAWN_POWDER, Content.WARPED_NETHERWART, Content.ASH, Content.LIVING_LANTERN)
                .map(RegistrySupplier::toOptional)
                .forEach(item -> item
                        .map(ForgeRegistryEntry::getRegistryName)
                        .map(ResourceLocation::getPath)
                        .ifPresent(i -> singleTexture(i, mcLoc("item/generated"), "layer0", modLoc("item/" + i)))
                );

        Content.LIVING_TORCH.toOptional()
                .map(ForgeRegistryEntry::getRegistryName)
                .map(ResourceLocation::getPath)
                .ifPresent(i -> singleTexture(i, mcLoc("item/generated"), "layer0", modLoc("block/" + i)));


        Content.CRAZED_SPAWN_EGG.toOptional()
                .map(ForgeRegistryEntry::getRegistryName)
                .map(ResourceLocation::getPath)
                .ifPresent(i -> withExistingParent(i, mcLoc("item/template_spawn_egg")));

    }
}
