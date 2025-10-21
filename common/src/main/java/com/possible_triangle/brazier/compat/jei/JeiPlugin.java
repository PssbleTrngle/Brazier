package com.possible_triangle.brazier.compat.jei;

import com.possible_triangle.brazier.BrazierConstants;
import com.possible_triangle.brazier.data.LightOnBrazierRecipe;
import com.possible_triangle.brazier.index.BrazierBlocks;
import com.tterrag.registrate.util.entry.RegistryEntry;
import java.util.stream.Stream;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {

    @Override
    @NotNull
    public ResourceLocation getPluginUid() {
        return BrazierConstants.createId("plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Stream.of(BrazierBlocks.BRAZIER, BrazierBlocks.LIVING_TORCH)
                .filter(RegistryEntry::isPresent)
                .map(RegistryEntry::get).map(ItemStack::new)
                .forEach(item -> registration.addIngredientInfo(item, VanillaTypes.ITEM_STACK, Component.translatable("description.brazier.brazier-1"), Component.translatable("description.brazier.brazier-2")));

        registration.addRecipes(JEIBrazierCategory.TYPE, LightOnBrazierRecipe.all().toList());
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new JEIBrazierCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(BrazierBlocks.BRAZIER.get()), JEIBrazierCategory.TYPE);
    }
}
