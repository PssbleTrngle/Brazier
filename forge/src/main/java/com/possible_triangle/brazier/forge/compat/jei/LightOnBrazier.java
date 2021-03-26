package com.possible_triangle.brazier.forge.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
import java.util.List;

public class LightOnBrazier implements IRecipeCategory<LightOnBrazier.Recipe> {

    public static final ResourceLocation UID = new ResourceLocation(Brazier.MOD_ID, "light_on_brazier");

    private static final int HEIGHT = 32;
    private static final int WIDTH = 96;

    private final IDrawable background;
    private final IDrawable icon;
    private final String display;

    public LightOnBrazier(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(getBrazierBlock()));
        this.display = I18n.get("category." + UID.getNamespace() + "." + UID.getPath());
    }

    private Block getBrazierBlock() {
        return Content.BRAZIER.toOptional()
                .map(b -> (Block) b)
                .orElse(Blocks.SOUL_CAMPFIRE);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends Recipe> getRecipeClass() {
        return Recipe.class;
    }

    @Override
    public String getTitle() {
        return display;
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
    public void setIngredients(Recipe recipe, IIngredients ingredients) {
        List<ItemStack> items = Arrays.asList(recipe.input.getItems());
        ingredients.setInputs(VanillaTypes.ITEM, items);
        ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(recipe.output));
    }

    @Override
    public void setRecipe(IRecipeLayout layout, Recipe recipe, IIngredients ingredients) {
        layout.getItemStacks().init(0, true, 10, HEIGHT / 2 - 9);
        layout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

        layout.getItemStacks().init(1, false, WIDTH - 25, HEIGHT / 2 -9);
        layout.getItemStacks().set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
    }

    @Override
    public void draw(Recipe recipe, PoseStack matrizes, double mouseX, double mouseY) {
        matrizes.pushPose();
        matrizes.translate(WIDTH / 2F - 9, HEIGHT / 2F - 11, 0);

        matrizes.translate(0, 0, 200);
        matrizes.translate(-6, 19, 0);
        matrizes.mulPose(Vector3f.XP.rotationDegrees(-22.5f));
        matrizes.mulPose(Vector3f.YP.rotationDegrees(90 - 225f));

        Minecraft mc = Minecraft.getInstance();
        BlockRenderDispatcher renderer = mc.getBlockRenderer();
        VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(RenderType.cutout());
        BlockState state = getBrazierBlock().defaultBlockState();
        BakedModel model = renderer.getBlockModel(state);
        mc.textureManager.bind(TextureAtlas.LOCATION_BLOCKS);
        renderer.getModelRenderer().renderModel(matrizes.last(), buffer, state, model, 1F, 1F, 1F, 15, 15);

        matrizes.popPose();
    }

    public static class Recipe {
        public final Ingredient input;
        public final Item output;

        public Recipe(Ingredient input, Item output) {
            this.input = input;
            this.output = output;
        }
    }

}
