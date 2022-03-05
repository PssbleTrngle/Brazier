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
import org.jetbrains.annotations.NotNull;

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
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockState below = world.getBlockState(pos.below());
        return below.isFaceSturdy(world, pos.below(), Direction.UP);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighborPos, boolean bl) {
        if (!level.isClientSide && !state.canSurvive(level, pos)) {
            dropResources(state, level, pos);
            level.removeBlock(pos, false);
        }
    }

}
