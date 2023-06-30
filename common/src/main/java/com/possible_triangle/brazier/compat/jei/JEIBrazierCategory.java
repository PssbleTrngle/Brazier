package com.possible_triangle.brazier.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.LightOnBrazierRecipe;
import com.possible_triangle.brazier.compat.DisplayConstants;
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
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.List;

import static com.possible_triangle.brazier.compat.DisplayConstants.HEIGHT;
import static com.possible_triangle.brazier.compat.DisplayConstants.WIDTH;

public class JEIBrazierCategory implements IRecipeCategory<LightOnBrazierRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(Brazier.MOD_ID, "light_on_brazier");
    public static final RecipeType<LightOnBrazierRecipe> TYPE = new RecipeType<>(UID, LightOnBrazierRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slot;
    private final int iconX = WIDTH / 2 - 9;
    private final int iconY = HEIGHT / 2 - 9;

    public JEIBrazierCategory(IGuiHelper guiHelper) {
        background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Content.ICON.get()));
        slot = guiHelper.getSlotDrawable();
    }

    @Override
    public Component getTitle() {
        return DisplayConstants.TITLE;
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
    public RecipeType<LightOnBrazierRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, LightOnBrazierRecipe recipe, IFocusGroup focuses) {
        var items = Arrays.asList(recipe.input().getItems());

        builder.addSlot(RecipeIngredientRole.INPUT, 10, HEIGHT / 2 - 9)
                .addItemStacks(items);

        builder.addSlot(RecipeIngredientRole.OUTPUT, WIDTH - 25, HEIGHT / 2 - 9)
                .addItemStack(recipe.output());
    }

    @Override
    public void draw(LightOnBrazierRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        icon.draw(stack, iconX, iconY);
        slot.draw(stack, 10, HEIGHT / 2 - 9);
        slot.draw(stack, WIDTH - 25, HEIGHT / 2 - 9);
    }

    private boolean isOverIcon(double mouseX, double mouseY) {
        return (mouseX >= iconX && mouseX <= (iconX + icon.getWidth()))
                && mouseY >= iconY && mouseY <= (iconY + icon.getHeight());
    }

    @Override
    public List<Component> getTooltipStrings(LightOnBrazierRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (isOverIcon(mouseX, mouseY)) return List.of(DisplayConstants.TOOLTIP);
        return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }
}
