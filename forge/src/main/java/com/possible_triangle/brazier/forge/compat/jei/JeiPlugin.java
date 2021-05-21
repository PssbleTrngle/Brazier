package com.possible_triangle.brazier.forge.compat.jei;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Conditional;
import com.possible_triangle.brazier.Content;
import me.shedaniel.architectury.registry.RegistrySupplier;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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

        Content.LIVING_TORCH.toOptional()
                .map(item -> new LightOnBrazier.Recipe(Ingredient.of(Content.TORCHES), item))
                .map(Collections::singleton)
                .ifPresent(recipes -> registration.addRecipes(recipes, LightOnBrazier.UID));

    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jei) {
        IIngredientManager ingredientManager = jei.getIngredientManager();
        IIngredientType<ItemStack> items = ingredientManager.getIngredientType(ItemStack.class);

        List<ItemStack> hidden = Stream.of(

                Conditional.disabled()
                        .map(ItemStack::new),

                Stream.of(Content.ICON)
                        .filter(RegistrySupplier::isPresent)
                        .map(RegistrySupplier::get)
                        .map(ItemStack::new)

        ).flatMap(Function.identity()).collect(Collectors.toList());

        if (!hidden.isEmpty()) ingredientManager.removeIngredientsAtRuntime(items, hidden);

    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new LightOnBrazier(registration.getJeiHelpers().getGuiHelper()));
    }
}
