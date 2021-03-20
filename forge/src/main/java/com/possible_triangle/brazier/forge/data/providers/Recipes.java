package com.possible_triangle.brazier.forge.data.providers;

import com.possible_triangle.brazier.Content;
import net.minecraft.data.*;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.ItemStackSpawnEggFix;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ForgeRecipeProvider;

import java.util.function.Consumer;

import static com.possible_triangle.brazier.Brazier.MODID;

public class Recipes extends RecipeProvider {

    public Recipes(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<FinishedRecipe> consumer) {
        Item flame = Content.LIVING_FLAME.orElse(net.minecraft.world.item.Items.BLAZE_POWDER);

        Content.BRAZIER.ifPresent(brazier ->
                ShapedRecipeBuilder.shaped(brazier)
                        .pattern("BFB")
                        .pattern("SSS")
                        .define('B', Blocks.IRON_BARS)
                        .define('S', Blocks.BLACKSTONE)
                        .define('F', flame)
                        .unlockedBy("collected_flame", has(flame))
                        .save(consumer)
        );

        Content.LIVING_TORCH.ifPresent(torch ->
                ShapelessRecipeBuilder.shapeless(torch, 2)
                        .requires(torch)
                        .requires(Ingredient.of(Content.TORCHES))
                        .unlockedBy("collected_torch", has(torch))
                        .save(consumer)
        );

        Content.SPAWN_POWDER.ifPresent(powder ->
                ShapelessRecipeBuilder.shapeless(powder, 6)
                        .requires(Ingredient.of(Content.ASH_TAG), 4)
                        .requires(net.minecraft.world.item.Items.CHARCOAL, 1)
                        .requires(Content.WARPED_NETHERWART.orElse(net.minecraft.world.item.Items.NETHER_WART))
                        .unlockedBy("collected_flame", has(flame))
                        .unlockedBy("collected_ash", has(Content.ASH_TAG))
                        .save(consumer)
        );

        ShapedRecipeBuilder.shaped(Blocks.WARPED_WART_BLOCK)
                .pattern("xxx")
                .pattern("xxx")
                .pattern("xxx")
                .define('x', Content.WARPED_WART_TAG)
                .unlockedBy("collected_wart", has(Content.WARPED_WART_TAG))
                .save(consumer, new ResourceLocation(MODID, "warped_warp_block"));

    }
}
