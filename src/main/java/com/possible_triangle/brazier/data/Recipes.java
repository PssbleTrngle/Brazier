package com.possible_triangle.brazier.data;

import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.block.BrazierBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        Content.BRAZIER.ifPresent(brazier -> {
            Item flame = Content.LIVING_FLAME.orElse(Items.BLAZE_POWDER);
            ShapedRecipeBuilder.shapedRecipe(brazier)
                    .patternLine("BFB")
                    .patternLine("SSS")
                    .key('B', Blocks.IRON_BARS)
                    .key('S', Blocks.field_235406_np_)
                    .key('F', flame)
                    .addCriterion("collected_flame", hasItem(flame))
                    .build(consumer);
        });

        Content.LIVING_TORCH.ifPresent(torch ->
                ShapelessRecipeBuilder.shapelessRecipe(torch, 2)
                        .addIngredient(torch)
                        .addIngredient(BrazierBlock.TORCH_INPUT)
                        .addCriterion("collected_flame", hasItem(torch))
                        .build(consumer)
        );
    }
}
