package com.possible_triangle.brazier.block;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SpawnPowder extends Block {

    private static final VoxelShape SHAPE = box(2.0D, 0.0D, 2.0D, 14.0D, 1.0D, 14.0D);

    public SpawnPowder() {
        super(Properties.of(Material.DECORATION)
                .noCollission()
                .instabreak()
                .lightLevel($ -> 1)
                .sound(SoundType.SOUL_SAND)
        );
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockState below = world.getBlockState(pos.below());
        return below.isFaceSturdy(world, pos.below(), Direction.UP);
    }


    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (!level.isClientSide) {
            if (!blockState.canSurvive(level, blockPos)) {
                dropResources(blockState, level, blockPos);
                level.removeBlock(blockPos, false);
            }
        }
    }

}
