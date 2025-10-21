package com.possible_triangle.brazier.datagen.providers;

import static net.minecraft.world.level.block.Blocks.NETHER_WART;

import com.possible_triangle.brazier.BrazierConstants;
import com.possible_triangle.brazier.index.BrazierItems;
import java.util.function.BiConsumer;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public final class LootInjects extends SimpleFabricLootTableProvider {

    public LootInjects(FabricDataOutput output) {
        super(output, LootContextParamSets.EMPTY);
    }

    private static ResourceLocation inject(String name) {
        return BrazierConstants.createId("inject/" + name);
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
        BrazierItems.WARPED_NETHER_WART.ifPresent(wart -> consumer.accept(inject("warped_wart"), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(wart)
                                .when(LootItemRandomChanceCondition.randomChance(0.02F))
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(NETHER_WART)
                                        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(NetherWartBlock.AGE, 3)))
                        )
                )
        ));

        BrazierItems.LIVING_FLAME.ifPresent(flame -> consumer.accept(inject("flame_jungle_temple"), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(flame))
                        .add(EmptyLootItem.emptyItem().setWeight(1))
                )
        ));

        BrazierItems.ASH.ifPresent(ash -> consumer.accept(inject("wither_ash"), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(ash)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(-1, 2)))
                                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))
                        )
                )
        ));
    }

}
