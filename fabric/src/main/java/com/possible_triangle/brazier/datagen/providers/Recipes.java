package com.possible_triangle.brazier.datagen.providers;

import com.possible_triangle.brazier.Content;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

import static com.possible_triangle.brazier.Brazier.MOD_ID;

public class Recipes extends FabricRecipeProvider {

    public Recipes(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        Item flame = Content.LIVING_FLAME.orElse(Items.BLAZE_POWDER);

        Content.BRAZIER.ifPresent(brazier ->
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, brazier)
                        .pattern("BFB")
                        .pattern("SSS")
                        .define('B', Blocks.IRON_BARS)
                        .define('S', Blocks.BLACKSTONE)
                        .define('F', flame)
                        .unlockedBy("collected_flame", has(flame))
                        .save(consumer)
        );

        Content.LIVING_TORCH.ifPresent(torch -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, torch, 2)
                    .requires(torch)
                    .requires(Ingredient.of(Content.TORCHES))
                    .unlockedBy("collected_torch", has(torch))
                    .save(consumer);

            Content.LIVING_LANTERN.ifPresent(lantern ->
                    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, lantern)
                            .pattern("xxx")
                            .pattern("xtx")
                            .pattern("xxx")
                            .define('x', Items.IRON_NUGGET)
                            .define('t', torch)
                            .unlockedBy("collected_torch", has(lantern))
                            .save(consumer)
            );
        });

        Content.SPAWN_POWDER.ifPresent(powder ->
                ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, powder, 6)
                        .requires(Ingredient.of(Content.ASH_TAG), 4)
                        .requires(Items.CHARCOAL, 1)
                        .requires(Content.WARPED_WART_TAG)
                        .unlockedBy("collected_flame", has(flame))
                        .unlockedBy("collected_ash", has(Content.ASH_TAG))
                        .save(consumer)
        );

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.WARPED_WART_BLOCK)
                .pattern("xxx")
                .pattern("xxx")
                .pattern("xxx")
                .define('x', Content.WARPED_WART_TAG)
                .unlockedBy("collected_wart", has(Content.WARPED_WART_TAG))
                .save(consumer, new ResourceLocation(MOD_ID, "warped_warp_block"));

    }
}
