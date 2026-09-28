package com.possible_triangle.brazier.datagen.services;

import static com.possible_triangle.brazier.BrazierConstants.createId;
import static com.tterrag.registrate.providers.RegistrateRecipeProvider.has;

import com.possible_triangle.brazier.index.BrazierTags;
import com.possible_triangle.brazier.platform.services.IDatagen;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

public class DatagenImpl implements IDatagen {

    @Override
    public void brazierBlockState(DataGenContext<Block, ? extends Block> context, RegistrateBlockstateProvider provider) {
        provider.getVariantBuilder(context.get()).forAllStatesExcept(state -> {
            var lit = state.getValue(BlockStateProperties.LIT);
            var suffix = lit ? "_lit" : "";
            var model = provider.models().getExistingFile(createId(context.getName() + suffix));
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .build();
        });
    }

    @Override
    public void wallTorchBlockstate(DataGenContext<Block, ? extends WallTorchBlock> context, RegistrateBlockstateProvider provider) {
        var model = provider.models().torchWall(context.getName(), createId("block/living_torch"));
        provider.horizontalBlock(context.get(), model);
    }

    @Override
    public void lanternBlockstate(DataGenContext<Block, ? extends LanternBlock> context, RegistrateBlockstateProvider provider) {
        provider.getVariantBuilder(context.get()).forAllStatesExcept(state -> {
            var hanging = state.getValue(LanternBlock.HANGING);
            var suffix = hanging ? "_hanging" : "";
            var parent = "template" + suffix + "_lantern";
            var model = provider.models().withExistingParent(context.getName() + suffix, parent)
                    .texture("lantern", provider.blockTexture(context.get()));
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .build();
        }, BlockStateProperties.WATERLOGGED);
    }

    @Override
    public void torchBlockState(DataGenContext<Block, ? extends TorchBlock> context, RegistrateBlockstateProvider provider) {
        provider.simpleBlock(context.get(), provider.models().torch(context.getName(), provider.blockTexture(context.get())));
    }

    @Override
    public void existingModel(DataGenContext<Block, ? extends Block> context, RegistrateBlockstateProvider provider) {
        provider.simpleBlock(context.get(), provider.models().getExistingFile(provider.blockTexture(context.get())));
    }

    @Override
    public void livingTorch(DataGenContext<Item, StandingAndWallBlockItem> context, RegistrateRecipeProvider provider) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, context.get(), 2)
                .requires(context.get())
                .requires(Ingredient.of(BrazierTags.TORCHES))
                .unlockedBy("has_living_torch", has(context.get()))
                .save(provider, createId("living_torch_duplication"));

        new ItemApplicationRecipe.Builder<>(DeployerApplicationRecipe::new, createId(context.getName()))
                .toolNotConsumed()
                .require(BrazierTags.TORCHES)
                .require(context.get())
                .output(context.get(), 2)
                .build(provider);
    }

}
