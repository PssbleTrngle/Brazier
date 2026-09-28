package com.possible_triangle.brazier.compat.jei;

import static com.possible_triangle.brazier.compat.DisplayConstants.HEIGHT;
import static com.possible_triangle.brazier.compat.DisplayConstants.WIDTH;

import com.possible_triangle.brazier.BrazierConstants;
import com.possible_triangle.brazier.compat.DisplayConstants;
import com.possible_triangle.brazier.data.LightOnBrazierRecipe;
import com.possible_triangle.brazier.index.BrazierItems;
import java.util.Arrays;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class JEIBrazierCategory implements IRecipeCategory<LightOnBrazierRecipe> {

    public static final ResourceLocation UID = BrazierConstants.createId("light_on_brazier");
    public static final RecipeType<LightOnBrazierRecipe> TYPE = new RecipeType<>(UID, LightOnBrazierRecipe.class);

    private final IDrawable icon;
    private final IDrawable slot;
    private final int iconX = WIDTH / 2 - 9;
    private final int iconY = HEIGHT / 2 - 9;

    public JEIBrazierCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BrazierItems.ICON.get()));
        slot = guiHelper.getSlotDrawable();
    }

    @Override
    public Component getTitle() {
        return DisplayConstants.TITLE;
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

        builder.addSlot(RecipeIngredientRole.INPUT, 11, HEIGHT / 2 - 8)
                .addItemStacks(items);

        builder.addSlot(RecipeIngredientRole.OUTPUT, WIDTH - 24, HEIGHT / 2 - 8)
                .addItemStack(recipe.output());
    }

    @Override
    public void draw(LightOnBrazierRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        icon.draw(graphics, iconX, iconY);
        slot.draw(graphics, 10, HEIGHT / 2 - 9);
        slot.draw(graphics, WIDTH - 25, HEIGHT / 2 - 9);
    }

    private boolean isOverIcon(double mouseX, double mouseY) {
        return (mouseX >= iconX && mouseX <= (iconX + icon.getWidth()))
                && mouseY >= iconY && mouseY <= (iconY + icon.getHeight());
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, LightOnBrazierRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (isOverIcon(mouseX, mouseY)) tooltip.add(DisplayConstants.TOOLTIP);
        IRecipeCategory.super.getTooltip(tooltip, recipe, recipeSlotsView, mouseX, mouseY);
    }
}
