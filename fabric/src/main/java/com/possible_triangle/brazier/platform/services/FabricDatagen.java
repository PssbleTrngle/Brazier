package com.possible_triangle.brazier.platform.services;

import static com.possible_triangle.brazier.BrazierConstants.createId;

import com.possible_triangle.brazier.world.block.BrazierBlock;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.fabricators_of_create.porting_lib.models.generators.ConfiguredModel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class FabricDatagen implements IDatagen {

    @Override
    public void brazierBlockState(DataGenContext<Block, ? extends Block> context, RegistrateBlockstateProvider provider) {
        provider.getVariantBuilder(context.get()).forAllStatesExcept(state -> {
            var lit = state.getValue(BrazierBlock.LIT);
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

}
