package com.possible_triangle.brazier.data;

import com.mojang.datafixers.util.Pair;
import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Loot extends BaseLootTableProvider {

    public Loot(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void addTables() {
        Content.CRAZED.ifPresent(type -> lootTables.put(type.getLootTable(), LootTable.builder()
                .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(Content.LIVING_FLAME.orElse(Items.BLAZE_POWDER)))
                )
        ));
    }

}
