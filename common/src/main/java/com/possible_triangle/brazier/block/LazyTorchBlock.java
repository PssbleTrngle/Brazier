package com.possible_triangle.brazier.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class LazyTorchBlock extends TorchBlock {

    private final Supplier<? extends ParticleOptions> particle;

    public LazyTorchBlock(Supplier<? extends ParticleOptions> particle) {
        super(Properties.copy(Blocks.TORCH), null);
        this.particle = particle;
    }

    @Override
    public void animateTick(@NotNull BlockState state, Level world, BlockPos pos, @NotNull RandomSource random) {
        var x = pos.getX() + 0.5D;
        var y = pos.getY() + 0.7D;
        var z = pos.getZ() + 0.5D;
        world.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0D, 0.0D, 0.0D);
        world.addParticle(this.particle.get(), x, y, z, 0.0D, 0.0D, 0.0D);
    }

}
