package com.possible_triangle.brazier.item;

import com.possible_triangle.brazier.Content;
import net.minecraft.core.Direction;
import net.minecraft.world.item.StandingAndWallBlockItem;

public class LivingTorch extends StandingAndWallBlockItem {

    public LivingTorch(Properties properties) {
        super(Content.LIVING_TORCH.get(), Content.LIVING_TORCH_BLOCK_WALL.get(), properties, Direction.DOWN);
    }

}
