package com.possible_triangle.brazier.platform.services;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;

public interface IDatagen {

    void brazierBlockState(DataGenContext<Block, ? extends Block> context, RegistrateBlockstateProvider provider);

    void wallTorchBlockstate(DataGenContext<Block, ? extends WallTorchBlock> context, RegistrateBlockstateProvider provider);

    void lanternBlockstate(DataGenContext<Block, ? extends LanternBlock> context, RegistrateBlockstateProvider provider);

    void torchBlockState(DataGenContext<Block, ? extends TorchBlock> context, RegistrateBlockstateProvider provider);

    void existingModel(DataGenContext<Block, ? extends Block> context, RegistrateBlockstateProvider provider);

}
