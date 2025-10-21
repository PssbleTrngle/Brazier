package com.possible_triangle.brazier.platform.services;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;

public class ForgeDatagen implements IDatagen {

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

}
