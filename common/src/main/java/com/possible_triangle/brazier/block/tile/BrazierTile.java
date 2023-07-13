package com.possible_triangle.brazier.block.tile;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.block.BrazierBlock;
import com.possible_triangle.brazier.config.ServerConfig;
import com.possible_triangle.brazier.logic.BrazierLogic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class BrazierTile extends BlockEntity {

    private int ticksExisted = 0;
    private int height = 0;

    private void setHeight(int height) {
        if (this.height != height) {
            this.height = height;
            setChanged();
            if (this.getBlockPos() != null && this.level != null)
                BrazierLogic.addBrazier(getBlockPos(), level.dimension(), getRange());
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, BrazierTile tile) {
        if (!level.isLoaded(pos)) return;

        ++tile.ticksExisted;
        if (tile.ticksExisted % 40 == 0) tile.checkStructure(pos, state);

        if (tile.height > 0 && level instanceof ServerLevel serverLevel && tile.ticksExisted % 10 == 0) {
            serverLevel.sendParticles(Content.FLAME_PARTICLE.get(), pos.getX() + 0.5, pos.getY() + 2.0, pos.getZ() + 0.5, 1, 0.4, 0.8, 0.4, 0);
        }
    }

    public void playSound(SoundEvent sound) {
        if (level != null) level.playSound(null, this.getBlockPos(), sound, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private void checkStructure(BlockPos pos, BlockState state) {
        if (level != null) {
            int newHeight = findHeight();
            if (newHeight != this.height) {

                if (newHeight > 0 && this.height == 0) playSound(SoundEvents.FIRECHARGE_USE);
                else if (newHeight == 0) playSound(SoundEvents.FIRE_EXTINGUISH);

                setHeight(newHeight);
                level.setBlockAndUpdate(pos, state.setValue(BrazierBlock.LIT, newHeight > 0));

                level.getEntitiesOfClass(ServerPlayer.class, new AABB(pos).inflate(10.0, 10.0, 10.0)).forEach(it ->
                        Content.CONSTRUCT_BRAZIER.get().trigger(it, newHeight)
                );
            }
        }
    }

    private int findHeight() {
        assert level != null;
        int max = Brazier.serverConfig().MAX_HEIGHT;
        var pos = getBlockPos();
        if (!level.getBlockState(pos.above()).isAir()) return 0;
        for (int y = 1; y <= max; y++) {
            var blocksMatch = true;
            for (int x = -2; x <= 2; x++)
                for (int z = -2; z <= 2; z++)
                    if (Math.abs(x * z) < 4) {
                        var isStripe = (x == 0 && Math.abs(z) == 2) || (z == 0 && Math.abs(x) == 2);
                        var tag = isStripe ? Content.BRAZIER_STRIPE_BLOCKS : Content.BRAZIER_BASE_BLOCKS;
                        var state = level.getBlockState(pos.offset(x, -y, z));
                        blocksMatch = blocksMatch && state.is(tag);
                    }
            if (!blocksMatch) return y - 1;
        }
        return max;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("height")) setHeight(nbt.getInt("height"));
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("height", height);
    }

    public BrazierTile(BlockPos pos, BlockState state) {
        super(Content.BRAZIER_TILE.get(), pos, state);
    }

    public int getRange() {
        if (height <= 0) return 0;
        ServerConfig config = Brazier.serverConfig();
        return config.BASE_RANGE + config.RANGE_PER_LEVEL * (height - 1);
    }

    public int getHeight() {
        return height;
    }


    public void onLoad() {
        if (this.height > 0) BrazierLogic.addBrazier(getBlockPos(), level.dimension(), getRange());
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (level != null) BrazierLogic.removeBrazier(getBlockPos(), level.dimension());
    }

    /**
     * This overrides the method `BlockEntity$getRenderBoundingBox`, which for some reason is not found in the mappings,
     * therefore the @Override is missing.
     */
    public AABB getRenderBoundingBox() {
        return new AABB(getBlockPos()).inflate(height + 2);
    }

}
