package com.possible_triangle.brazier.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class SpawnPowder extends Block {

    private static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 1.0D, 14.0D);

    public SpawnPowder() {
        super(Properties.create(Material.MISCELLANEOUS)
                .doesNotBlockMovement()
                .zeroHardnessAndResistance()
                .func_235838_a_($ -> 1)
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        BlockState below = world.getBlockState(pos.down());
        return below.isSolidSide(world, pos.down(), Direction.UP);
    }
}
