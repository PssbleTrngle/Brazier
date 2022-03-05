package com.possible_triangle.brazier.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.function.Supplier;

public class LazyWallTorchBlock extends WallTorchBlock {

    private final Supplier<? extends ParticleOptions> particle;

    public LazyWallTorchBlock(Supplier<? extends ParticleOptions> particle) {
        super(Properties.copy(Blocks.WALL_TORCH), null);
        this.particle = particle;
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, @NotNull Random rand) {
        Direction direction = state.getValue(FACING);
        Direction opposite = direction.getOpposite();
        var x = pos.getX() + 0.77D * opposite.getStepX();
        var y = pos.getY() + 0.92D;
        var z = pos.getZ() + 0.77D * opposite.getStepZ();
        world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
        world.addParticle(this.particle.get(), x, y, z, 0.0D, 0.0D, 0.0D);
    }

}
