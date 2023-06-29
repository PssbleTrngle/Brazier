package com.possible_triangle.brazier.item;

import com.possible_triangle.brazier.Content;
import net.minecraft.core.Direction;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.StandingAndWallBlockItem;

public class LivingTorch extends StandingAndWallBlockItem {

    public LivingTorch() {
        super(Content.LIVING_TORCH_BLOCK.get(), Content.LIVING_TORCH_BLOCK_WALL.get(), new Properties().arch$tab(CreativeModeTabs.BUILDING_BLOCKS), Direction.DOWN);
    }

}
