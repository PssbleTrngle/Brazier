package com.possible_triangle.brazier.block.tile;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BaseTile extends BlockEntity {

    public BaseTile(BlockEntityType<?> type) {
        super(type);
    }

   public void onLoad() {}

}
