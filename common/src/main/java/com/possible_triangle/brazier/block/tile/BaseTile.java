package com.possible_triangle.brazier.block.tile;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BaseTile extends BlockEntity {

    public BaseTile(BlockEntityType<?> type) {
        super(type);
    }

    /*
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 3, this.getUpdateTag());
    }

    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }
    */

}
