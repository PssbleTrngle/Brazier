package com.possible_triangle.brazier.forge.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.LightOnBrazierRecipe;
import com.possible_triangle.brazier.compat.DisplayConstants;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.possible_triangle.brazier.compat.DisplayConstants.HEIGHT;
import static com.possible_triangle.brazier.compat.DisplayConstants.WIDTH;

@SuppressWarnings("removal")
public class LightOnBrazier implements IRecipeCategory<LightOnBrazierRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(Brazier.MOD_ID, "light_on_brazier");
    private static List<ItemStack> BRAZIER_LIST = null;

    public static List<ItemStack> getBrazier() {
        if (BRAZIER_LIST == null) BRAZIER_LIST = Collections.singletonList(new ItemStack(Content.BRAZIER.get()));
        return BRAZIER_LIST;
    }

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slot;
    private final int iconX = WIDTH / 2 - 9;
    private final int iconY = HEIGHT / 2 - 9;

    public LightOnBrazier(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(Content.ICON.get()));
        slot = guiHelper.getSlotDrawable();
    }

    @Override
    public Component getTitle() {
        return DisplayConstants.TITLE;
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends LightOnBrazierRecipe> getRecipeClass() {
        return LightOnBrazierRecipe.class;
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
    public void setIngredients(LightOnBrazierRecipe recipe, IIngredients ingredients) {
        List<ItemStack> items = Arrays.asList(recipe.input().getItems());
        ingredients.setInputLists(VanillaTypes.ITEM, Stream.of(items, getBrazier()).collect(Collectors.toList()));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.output());
    }

    @Override
    public void setRecipe(IRecipeLayout layout, LightOnBrazierRecipe recipe, IIngredients ingredients) {
        List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);

        layout.getItemStacks().init(0, true, WIDTH / 2 - 9, HEIGHT / 2 - 9);
        layout.getItemStacks().set(0, new ItemStack(Content.ICON.get()));

        layout.getItemStacks().init(1, true, 10, HEIGHT / 2 - 9);
        layout.getItemStacks().set(1, inputs.get(0));

        layout.getItemStacks().init(2, false, WIDTH - 25, HEIGHT / 2 - 9);
        layout.getItemStacks().set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
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
