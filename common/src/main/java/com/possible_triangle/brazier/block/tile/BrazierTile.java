package com.possible_triangle.brazier.block.tile;

import com.google.common.collect.Maps;
import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.block.BrazierBlock;
import com.possible_triangle.brazier.config.DistanceHandler;
import com.possible_triangle.brazier.config.ServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;

public class BrazierTile extends BaseTile implements TickableBlockEntity {

    private static final HashMap<BlockPos, Integer> BRAZIERS = Maps.newHashMap();
    private static final float TOLERANCE = 0.03F;

    private int ticksExisted = 0;
    private int height = 0;

    public static boolean isBorder(Vec3 pos) {
        synchronized (BRAZIERS) {
            return BRAZIERS.entrySet().stream().anyMatch(e -> {
                double dist = DistanceHandler.getDistance(pos, e.getKey());
                double minDist = Math.pow(e.getValue() - TOLERANCE, 2);
                double maxDist = Math.pow(e.getValue() + TOLERANCE, 2);
                return dist <= maxDist && dist >= minDist;
            });
        }
    }

    public static boolean inRange(BlockPos pos) {
        synchronized (BRAZIERS) {
            return BRAZIERS.entrySet().stream().anyMatch(e -> {
                if (!Brazier.SERVER_CONFIG.get().PROTECT_ABOVE && e.getKey().getY() < pos.getY()) return false;
                double dist = DistanceHandler.getDistance(pos, e.getKey());
                int maxDist = e.getValue() * e.getValue();
                return dist <= maxDist;
            });
        }
    }

    private void setHeight(int height) {
        if (this.height != height) {
            this.height = height;
            setChanged();
            if (this.getBlockPos() != null) synchronized (BRAZIERS) {
                BRAZIERS.put(this.getBlockPos(), getRange());
            }
        }
    }

    @Override
    public void tick() {
        ++this.ticksExisted;
        if (this.ticksExisted % 40 == 0) checkStructure();

        if (this.height > 0 && level instanceof ServerLevel && ticksExisted % 10 == 0) {
            BlockPos pos = this.getBlockPos();
            ((ServerLevel) level).sendParticles(Content.FLAME_PARTICLE.get(), pos.getX() + 0.5, pos.getY() + 2, pos.getZ() + 0.5, 1, 0.4, 0.8, 0.4, 0);
        }
    }

    public void playSound(SoundEvent sound) {
        if (level != null) level.playSound(null, this.getBlockPos(), sound, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private void checkStructure() {
        if (level != null) {
            int height = findHeight();
            if (height != this.height) {

                if (height > 0 && this.height == 0) playSound(SoundEvents.FIRECHARGE_USE);
                else if (height == 0) playSound(SoundEvents.FIRE_EXTINGUISH);

                setHeight(height);
                BlockState s = level.getBlockState(getBlockPos());
                level.setBlockAndUpdate(getBlockPos(), s.setValue(BrazierBlock.LIT, height > 0));
            }
        }
    }

    private int findHeight() {
        assert level != null;
        int max = Brazier.SERVER_CONFIG.get().MAX_HEIGHT;
        BlockPos pos = getBlockPos();
        if (!level.getBlockState(pos.above()).isAir()) return 0;
        for (int height = 1; height <= max; height++) {
            boolean b = true;
            for (int x = -2; x <= 2; x++)
                for (int z = -2; z <= 2; z++)
                    if (Math.abs(x * z) < 4) {
                        BlockState state = level.getBlockState(pos.offset(x, -height, z));
                        b = b && state.is(Content.BRAZIER_BASE_BLOCKS);
                    }
            if (!b) return height - 1;
        }
        return max;
    }


    @Override
    public void load(BlockState state, CompoundTag nbt) {
        super.load(state, nbt);
        if (nbt.contains("height")) setHeight(nbt.getInt("height"));
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        nbt = super.save(nbt);
        nbt.putInt("height", height);
        return nbt;
    }

    public BrazierTile() {
        super(Content.BRAZIER_TILE.get());
    }

    public int getRange() {
        if (height <= 0) return 0;
        ServerConfig config = Brazier.SERVER_CONFIG.get();
        return config.BASE_RANGE + config.RANGE_PER_LEVEL * (height - 1);
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void onLoad() {
        if (this.height > 0) synchronized (BRAZIERS) {
            BRAZIERS.put(this.getBlockPos(), getRange());
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        synchronized (BRAZIERS) {
            BRAZIERS.remove(getBlockPos());
        }
    }

}
