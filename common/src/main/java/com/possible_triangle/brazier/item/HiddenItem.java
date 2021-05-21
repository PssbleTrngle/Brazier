package com.possible_triangle.brazier.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class HiddenItem extends Item {

    public HiddenItem() {
        super(new Properties().tab(CreativeModeTab.TAB_SEARCH));
    }

}
