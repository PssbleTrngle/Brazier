package com.possible_triangle.brazier.data;

import com.possible_triangle.brazier.Content;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
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
        boolean noNetherEx = !ModList.get().isLoaded("nether_extension");

        if (event.getName().equals(LootTables.CHESTS_JUNGLE_TEMPLE)) {
            Content.LIVING_FLAME.ifPresent(flame ->
                    event.getTable().addPool(LootPool.builder()
                            .addEntry(ItemLootEntry.builder(flame))
                            .addEntry(EmptyLootEntry.func_216167_a().weight(1))
                            .build())
            );
        } else if (event.getName().equals(EntityType.WITHER_SKELETON.getLootTable()) && !noNetherEx) {
            Content.ASH.ifPresent(ash -> event.getTable().addPool(LootPool.builder()
                    .addEntry(ItemLootEntry.builder(ash)
                            .acceptFunction(SetCount.builder(RandomValueRange.of(-1, 1)))
                            .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 1)))
                    )
                    .build())
            );
        } else if (event.getName().equals(Blocks.NETHER_WART.getLootTable())) {
            Content.WARPED_NETHERWART.ifPresent(wart -> event.getTable().addPool(LootPool.builder()
                    .addEntry(ItemLootEntry.builder(wart).acceptFunction(SetCount.builder(RandomValueRange.of(-1, 1))
                            .acceptCondition(RandomChance.builder(0.02F))
                            .acceptCondition(BlockStateProperty.builder(Blocks.NETHER_WART)
                                    .fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(NetherWartBlock.AGE, 3)))
                    ))
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

        Stream.of(Content.BRAZIER, Content.LIVING_TORCH_BLOCK, Content.LIVING_TORCH_BLOCK_WALL, Content.SPAWN_POWDER)
                .filter(RegistryObject::isPresent).map(RegistryObject::get)
                .forEach(block -> lootTables.put(block.getLootTable(), LootTable.builder()
                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(block))
                                .acceptCondition(SurvivesExplosion.builder())
                        ).setParameterSet(LootParameterSets.BLOCK)
                ));
    }

}
