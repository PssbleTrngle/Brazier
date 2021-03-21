package com.possible_triangle.brazier.compat.jei;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import me.shedaniel.architectury.registry.RegistrySupplier;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Brazier.MOD_ID, "plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IIngredientManager ingredientManager = registration.getIngredientManager();
        IIngredientType<ItemStack> items = ingredientManager.getIngredientType(ItemStack.class);

        Stream.of(Content.BRAZIER, Content.LIVING_TORCH).map(RegistrySupplier::toOptional)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ItemStack::new)
                .forEach(item -> registration.addIngredientInfo(item, items, "description.brazier.brazier-1", "description.brazier.brazier-2"));
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jei) {
        IIngredientManager ingredientManager = jei.getIngredientManager();
        IIngredientType<ItemStack> items = ingredientManager.getIngredientType(ItemStack.class);

        if (!Brazier.SERVER_CONFIG.get().DECORATION) {
            List<ItemStack> hidden = Stream.of(Content.LIVING_LANTERN, Content.LIVING_TORCH)
                    .filter(RegistrySupplier::isPresent)
                    .map(RegistrySupplier::get)
                    .map(ItemStack::new).collect(Collectors.toList());

            ingredientManager.removeIngredientsAtRuntime(items, hidden);
        }

        if (!Brazier.SERVER_CONFIG.get().SPAWN_POWDER) Content.SPAWN_POWDER.ifPresent(powder ->
                ingredientManager.removeIngredientsAtRuntime(items, Collections.singleton(new ItemStack(powder)))
        );

    }
}
