package com.possible_triangle.brazier.forge.compat.jei;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Conditional;
import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.LightOnBrazierRecipe;
import dev.architectury.registry.registries.RegistrySupplier;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("removal")
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
        IIngredientType<ItemStack> items = ingredientManager.getIngredientType(ItemStack.class);

        Stream.of(Content.BRAZIER, Content.LIVING_TORCH).map(RegistrySupplier::toOptional)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ItemStack::new)
                .forEach(item -> registration.addIngredientInfo(item, items, new TranslatableComponent("description.brazier.brazier-1"), new TranslatableComponent("description.brazier.brazier-2")));

        registration.addRecipes(LightOnBrazierRecipe.all().toList(), LightOnBrazier.UID);

    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jei) {
        IIngredientManager ingredientManager = jei.getIngredientManager();
        IIngredientType<ItemStack> items = ingredientManager.getIngredientType(ItemStack.class);

        Conditional.removeHidden(hidden -> {
            ingredientManager.removeIngredientsAtRuntime(items, hidden);
        });
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new LightOnBrazier(registration.getJeiHelpers().getGuiHelper()));
    }
}
