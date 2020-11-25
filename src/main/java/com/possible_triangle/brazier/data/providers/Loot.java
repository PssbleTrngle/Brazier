package com.possible_triangle.brazier.data.providers;

import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.config.BrazierConfig;
import com.possible_triangle.brazier.data.InjectingLootTableProvider;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.conditions.SurvivesExplosion;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;

import java.util.stream.Stream;

public class Loot extends InjectingLootTableProvider {

    @Override
    protected void addTables() {
        Content.CRAZED.ifPresent(type -> addLootTable(type.getLootTable(), LootTable.builder()
                .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                        .addEntry(ItemLootEntry.builder(Content.LIVING_FLAME.orElse(Items.BLAZE_POWDER)))
                ).setParameterSet(LootParameterSets.ENTITY)
        ));

        Stream.of(Content.BRAZIER, Content.LIVING_TORCH_BLOCK, Content.LIVING_TORCH_BLOCK_WALL, Content.SPAWN_POWDER)
                .filter(RegistryObject::isPresent).map(RegistryObject::get)
                .forEach(block -> addLootTable(block.getLootTable(), LootTable.builder()
                        .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                                .addEntry(ItemLootEntry.builder(block))
                                .acceptCondition(SurvivesExplosion.builder())
                        ).setParameterSet(LootParameterSets.BLOCK)
                ));

        Content.WARPED_NETHERWART.ifPresent(wart -> addInject("warped_wart", Blocks.NETHER_WART.getLootTable(), LootPool.builder()
                .addEntry(ItemLootEntry.builder(wart)
                        .acceptCondition(RandomChance.builder(0.02F))
                        .acceptCondition(BlockStateProperty.builder(Blocks.NETHER_WART)
                                .fromProperties(StatePropertiesPredicate.Builder.newBuilder().withIntProp(NetherWartBlock.AGE, 3)))
                )
        ));

        Content.LIVING_FLAME.ifPresent(flame -> addInject("flame_jungle_temple", LootTables.CHESTS_JUNGLE_TEMPLE, LootPool.builder()
                .addEntry(ItemLootEntry.builder(flame))
                .addEntry(EmptyLootEntry.func_216167_a().weight(1)),
                BrazierConfig.SERVER.JUNGLE_LOOT::get
        ));

        Content.ASH.ifPresent(ash -> addInject("wither_ash", EntityType.WITHER_SKELETON.getLootTable(), LootPool.builder()
                .addEntry(ItemLootEntry.builder(ash)
                        .acceptFunction(SetCount.builder(RandomValueRange.of(-1, 2)))
                        .acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0, 1)))
                ),
                () -> !ModList.get().isLoaded("nether_extension")
        ));
    }

}
