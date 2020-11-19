package com.possible_triangle.brazier.data;

import com.possible_triangle.brazier.Content;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        Item flame = Content.LIVING_FLAME.orElse(Items.BLAZE_POWDER);

        Content.BRAZIER.ifPresent(brazier ->
                ShapedRecipeBuilder.shapedRecipe(brazier)
                        .patternLine("BFB")
                        .patternLine("SSS")
                        .key('B', Blocks.IRON_BARS)
                        .key('S', Blocks.field_235406_np_)
                        .key('F', flame)
                        .addCriterion("collected_flame", hasItem(flame))
                        .build(consumer)
        );

        Content.LIVING_TORCH.ifPresent(torch ->
                ShapelessRecipeBuilder.shapelessRecipe(torch, 2)
                        .addIngredient(torch)
                        .addIngredient(Ingredient.fromTag(Content.TORCHES))
                        .addCriterion("collected_torch", hasItem(torch))
                        .build(consumer)
        );

        Content.SPAWN_POWDER.ifPresent(powder ->
                ShapelessRecipeBuilder.shapelessRecipe(powder, 6)
                        .addIngredient(Ingredient.fromTag(Content.ASH_TAG), 4)
                        .addIngredient(Items.CHARCOAL, 1)
                        .addIngredient(Content.WARPED_NETHERWART.orElse(Items.NETHER_WART))
                        .addCriterion("collected_flame", hasItem(flame))
                        .addCriterion("collected_ash", hasItem(Content.ASH_TAG))
        );

        Content.WARPED_NETHERWART.ifPresent(wart ->
                ShapedRecipeBuilder.shapedRecipe(Blocks.field_235374_mn_)
                        .patternLine("xxx")
                        .patternLine("xxx")
                        .patternLine("xxx")
                        .key('x', wart)
                        .addCriterion("collected_wart", hasItem(wart))
        );

    }
}
