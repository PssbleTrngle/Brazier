package com.possible_triangle.brazier.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;

public class Flame extends Item {

    public Flame() {
        super((new Item.Properties()).group(ItemGroup.MATERIALS).rarity(Rarity.UNCOMMON));
    }

}
