package com.possible_triangle.brazier.item;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;

public class HiddenItem extends Item {

    public HiddenItem() {
        super(new Properties().arch$tab(CreativeModeTabs.SEARCH));
    }

}
