package com.possible_triangle.brazier.datagen;

import static net.minecraft.world.level.block.Blocks.NETHER_WART;

import com.possible_triangle.brazier.BrazierConstants;
import com.possible_triangle.brazier.index.BrazierItems;

import java.util.function.BiConsumer;

import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public final class LootInjects {

    private static ResourceKey<LootTable> inject(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, BrazierConstants.createId("inject/" + name));
    }

    public static void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
        consumer.accept(inject("warped_wart"), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(BrazierItems.WARPED_NETHER_WART)
                                .when(LootItemRandomChanceCondition.randomChance(0.02F))
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(NETHER_WART)
                                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(NetherWartBlock.AGE, 3)))
                        )
                )
        );

        consumer.accept(inject("flame_jungle_temple"), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(BrazierItems.LIVING_FLAME))
                        .add(EmptyLootItem.emptyItem().setWeight(1))
                )
        );

        consumer.accept(inject("wither_ash"), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(BrazierItems.ASH)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(-1, 2)))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))
                        )
                )
        );
    }

}
