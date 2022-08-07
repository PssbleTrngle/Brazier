package com.possible_triangle.brazier.forge.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;

public class LightOnBrazier implements IRecipeCategory<LightOnBrazier.Recipe> {

    public static final ResourceLocation UID = new ResourceLocation(Brazier.MOD_ID, "light_on_brazier");
    public static final RecipeType<Recipe> TYPE = new RecipeType<>(UID, Recipe.class);

    private static final int HEIGHT = 32;
    private static final int WIDTH = 96;

    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;

    public LightOnBrazier(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Content.ICON.get()));
        this.title = Component.translatable("category.brazier.light_on_brazier");
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public RecipeType<Recipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, Recipe recipe, IFocusGroup focuses) {
        var items = Arrays.asList(recipe.input.getItems());

        builder.addSlot(RecipeIngredientRole.INPUT, 10, HEIGHT / 2 - 9)
                .addItemStacks(items)
                .addItemStack(new ItemStack(Content.BRAZIER.get()));

        builder.addSlot(RecipeIngredientRole.OUTPUT, WIDTH - 25, HEIGHT / 2 - 9)
                .addItemStack(new ItemStack(recipe.output));
    }

    @Override
    public void draw(Recipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        icon.draw(stack, WIDTH / 2 - 9, HEIGHT / 2 - 9);
    }

    public record Recipe(Ingredient input, Item output) {
    }

}
