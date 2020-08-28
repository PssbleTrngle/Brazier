package com.possible_triangle.brazier.item;

import com.possible_triangle.brazier.Content;
import net.minecraft.item.*;

public class LivingTorch extends WallOrFloorItem implements BrazierIndicator {

    public LivingTorch() {
        super(Content.LIVING_TORCH_BLOCK.get(), Content.LIVING_TORCH_BLOCK_WALL.get(), (new Item.Properties()).group(ItemGroup.DECORATIONS));
    }

}
