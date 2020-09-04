package com.possible_triangle.brazier.data;

import com.possible_triangle.brazier.Content;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

import java.util.stream.Stream;

@Mod.EventBusSubscriber
public class Loot extends BaseLootTableProvider {

    public Loot(DataGenerator generator) {
        super(generator);
    }

    @SubscribeEvent
    public static void onLootLoaded(LootTableLoadEvent event) {
        if (event.getName().equals(LootTables.CHESTS_JUNGLE_TEMPLE)) {
            Content.LIVING_FLAME.ifPresent(flame ->
                    event.getTable().addPool(LootPool.builder()
                            .addEntry(ItemLootEntry.builder(flame))
                            .addEntry(EmptyLootEntry.func_216167_a().weight(1))
                            .build())
            );
        }
    }

    @Override
    protected void addTables() {
        Content.CRAZED.ifPresent(type -> lootTables.put(type.getLootTable(), LootTable.builder()
                .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(Content.LIVING_FLAME.orElse(Items.BLAZE_POWDER)))
                ).setParameterSet(LootParameterSets.ENTITY)
        ));

        Stream.of(Content.BRAZIER, Content.LIVING_TORCH_BLOCK, Content.LIVING_TORCH_BLOCK_WALL)
                .filter(RegistryObject::isPresent).map(RegistryObject::get)
                .forEach(block -> lootTables.put(block.getLootTable(), LootTable.builder()
                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(block))
                                .acceptCondition(SurvivesExplosion.builder())
                        ).setParameterSet(LootParameterSets.BLOCK)
                ));
    }

}
