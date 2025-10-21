package com.possible_triangle.brazier.world.item;

import com.possible_triangle.brazier.index.BrazierBlocks;
import net.minecraft.core.Direction;
import net.minecraft.world.item.StandingAndWallBlockItem;

public class LivingTorch extends StandingAndWallBlockItem {

    public LivingTorch(Properties properties) {
        super(BrazierBlocks.LIVING_TORCH.get(), BrazierBlocks.LIVING_TORCH_BLOCK_WALL.get(), properties, Direction.DOWN);
    }

}
