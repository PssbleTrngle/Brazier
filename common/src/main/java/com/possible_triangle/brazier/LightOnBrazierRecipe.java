package com.possible_triangle.brazier;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.stream.Stream;

public record LightOnBrazierRecipe(Ingredient input, ItemStack output) {

    public static Stream<LightOnBrazierRecipe> all() {
        return Content.LIVING_TORCH.toOptional()
                .map(item -> new LightOnBrazierRecipe(Ingredient.of(Content.TORCHES), new ItemStack(item)))
                .stream();
    }

}