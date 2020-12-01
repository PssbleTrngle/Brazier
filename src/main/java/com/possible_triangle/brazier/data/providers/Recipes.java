package com.possible_triangle.brazier.data.providers;

import com.possible_triangle.brazier.Content;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ForgeItemTagsProvider;

import java.util.function.Consumer;

import static com.possible_triangle.brazier.Brazier.MODID;

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
                        .key('S', Blocks.BLACKSTONE)
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
                        .build(consumer)
        );

        ShapedRecipeBuilder.shapedRecipe(Blocks.WARPED_WART_BLOCK)
                .patternLine("xxx")
                .patternLine("xxx")
                .patternLine("xxx")
                .key('x', Content.WARPED_WART_TAG)
                .addCriterion("collected_wart", hasItem(Content.WARPED_WART_TAG))
                .build(consumer, new ResourceLocation(MODID, "warped_warp_block"));

        Content.LIVING_LANTERN.ifPresent(lantern -> Content.LIVING_TORCH.ifPresent(torch ->
                ShapedRecipeBuilder.shapedRecipe(lantern)
                        .patternLine("xxx")
                        .patternLine("xtx")
                        .patternLine("xxx")
                        .key('x', Tags.Items.NUGGETS_IRON)
                        .key('t', torch)
                        .addCriterion("crafted_torch", hasItem(torch))
                        .build(consumer)
        ));
    }
}
