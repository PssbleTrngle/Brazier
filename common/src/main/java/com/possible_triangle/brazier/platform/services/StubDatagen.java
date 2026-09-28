package com.possible_triangle.brazier.platform.services;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;

public class StubDatagen implements IDatagen {

    private static void NOOP() {
        throw new IllegalStateException("datagen can only be executed on fabric");
    }

    @Override
    public void brazierBlockState(DataGenContext<Block, ? extends Block> context, RegistrateBlockstateProvider provider) {
        NOOP();
    }

    @Override
    public void wallTorchBlockstate(DataGenContext<Block, ? extends WallTorchBlock> context, RegistrateBlockstateProvider provider) {
        NOOP();
    }

    @Override
    public void lanternBlockstate(DataGenContext<Block, ? extends LanternBlock> context, RegistrateBlockstateProvider provider) {
        NOOP();
    }

    @Override
    public void torchBlockState(DataGenContext<Block, ? extends TorchBlock> context, RegistrateBlockstateProvider provider) {
        NOOP();
    }

    @Override
    public void existingModel(DataGenContext<Block, ? extends Block> context, RegistrateBlockstateProvider provider) {
        NOOP();
    }

    @Override
    public void livingTorch(DataGenContext<Item, StandingAndWallBlockItem> context, RegistrateRecipeProvider provider) {
        NOOP();
    }

}
