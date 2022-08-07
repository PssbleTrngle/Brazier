package com.possible_triangle.brazier.forge.data.providers;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.stream.Stream;

public class Items extends ItemModelProvider {

    public Items(DataGenerator generator, ExistingFileHelper fileHelper) {
        super(generator, Brazier.MOD_ID, fileHelper);
    }

    @Override
    protected void registerModels() {
        Content.BRAZIER.toOptional().map(it -> it.builtInRegistryHolder().key().location())
                .map(ResourceLocation::getPath)
                .ifPresent(b -> this.withExistingParent(b, modLoc("block/" + b)));

        Stream.of(Content.LIVING_FLAME, Content.SPAWN_POWDER, Content.WARPED_NETHERWART, Content.ASH, Content.LIVING_LANTERN)
                .map(RegistrySupplier::toOptional)
                .forEach(item -> item
                        .map(ItemLike::asItem)
                        .map(it -> it.builtInRegistryHolder().key().location())
                        .map(ResourceLocation::getPath)
                        .ifPresent(i -> singleTexture(i, mcLoc("item/generated"), "layer0", modLoc("item/" + i)))
                );

        Content.LIVING_TORCH.toOptional()
                .map(it -> it.builtInRegistryHolder().key().location())
                .map(ResourceLocation::getPath)
                .ifPresent(i -> singleTexture(i, mcLoc("item/generated"), "layer0", modLoc("block/" + i)));

        Content.CRAZED_SPAWN_EGG.toOptional()
                .map(it -> it.builtInRegistryHolder().key().location())
                .map(ResourceLocation::getPath)
                .ifPresent(i -> withExistingParent(i, mcLoc("item/template_spawn_egg")));

        Content.ICON.toOptional()
                .map(it -> it.builtInRegistryHolder().key().location())
                .map(ResourceLocation::getPath)
                .ifPresent(i -> withExistingParent(i, modLoc("block/brazier_lit")));

    }
}
