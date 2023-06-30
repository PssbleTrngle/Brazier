package com.possible_triangle.brazier.compat.jei;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Conditional;
import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.LightOnBrazierRecipe;
import dev.architectury.registry.registries.RegistrySupplier;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.stream.Stream;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {

    @Override
    @NotNull
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(Brazier.MOD_ID, "plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IIngredientManager ingredientManager = registration.getIngredientManager();

        Stream.of(Content.BRAZIER, Content.LIVING_TORCH)
                .map(RegistrySupplier::toOptional)
                .filter(Optional::isPresent)
                .map(Optional::get).map(ItemStack::new)
                .forEach(item -> registration.addIngredientInfo(item, VanillaTypes.ITEM_STACK, Component.translatable("description.brazier.brazier-1"), Component.translatable("description.brazier.brazier-2")));

        registration.addRecipes(JEIBrazierCategory.TYPE, LightOnBrazierRecipe.all().toList());

        Conditional.removeHidden(hidden ->
                ingredientManager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, hidden)
        );
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new JEIBrazierCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Content.BRAZIER.get()), JEIBrazierCategory.TYPE);
    }
}