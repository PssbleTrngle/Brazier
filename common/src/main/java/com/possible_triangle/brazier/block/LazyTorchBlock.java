package com.possible_triangle.brazier.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;
import java.util.function.Supplier;

public class LazyTorchBlock extends TorchBlock {

    private final Supplier<? extends ParticleOptions> particle;

    public LazyTorchBlock(Supplier<? extends ParticleOptions> particle) {
        super(Properties.copy(Blocks.TORCH), null);
        this.particle = particle;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {
        double d0 = (double) pos.getX() + 0.5D;
        double d1 = (double) pos.getY() + 0.7D;
        double d2 = (double) pos.getZ() + 0.5D;
        //world.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
        world.addParticle(this.particle.get(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
    }

}
