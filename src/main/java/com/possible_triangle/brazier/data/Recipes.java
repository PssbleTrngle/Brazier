package com.possible_triangle.brazier.data;

import com.possible_triangle.brazier.Content;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
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
    }
}
