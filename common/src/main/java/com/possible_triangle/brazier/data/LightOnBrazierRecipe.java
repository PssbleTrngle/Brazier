package com.possible_triangle.brazier.data;

import com.possible_triangle.brazier.index.BrazierBlocks;
import com.possible_triangle.brazier.index.BrazierTags;
import java.util.stream.Stream;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record LightOnBrazierRecipe(Ingredient input, ItemStack output) {

    public static Stream<LightOnBrazierRecipe> all() {
        return BrazierBlocks.LIVING_TORCH
                .map(item -> new LightOnBrazierRecipe(Ingredient.of(BrazierTags.TORCHES), new ItemStack(item)))
                .stream();
    }

}