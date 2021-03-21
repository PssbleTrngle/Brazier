package com.possible_triangle.brazier.forge.data.providers;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.forge.data.InjectingLootTableProvider;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.fml.ModList;

import java.util.stream.Stream;

import static net.minecraft.world.level.block.Blocks.NETHER_WART;

public class Loot extends InjectingLootTableProvider {

    @Override
    protected void addTables() {
        Content.CRAZED.ifPresent(type -> addLootTable(type.getDefaultLootTable(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1))
                        .add(LootItem.lootTableItem(Content.LIVING_FLAME.orElse(Items.BLAZE_POWDER)))
                ).setParamSet(LootContextParamSets.ENTITY)
        ));

        Stream.of(Content.BRAZIER, Content.LIVING_TORCH_BLOCK, Content.LIVING_TORCH_BLOCK_WALL, Content.SPAWN_POWDER)
                .filter(RegistrySupplier::isPresent).map(RegistrySupplier::get)
                .forEach(block -> addLootTable(block.getLootTable(), LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantIntValue.exactly(1))
                                .add(LootItem.lootTableItem(block))
                                .when(ExplosionCondition.survivesExplosion())
                        ).setParamSet(LootContextParamSets.BLOCK)
                ));

        Content.WARPED_NETHERWART.ifPresent(wart -> addInject("warped_wart", NETHER_WART.getLootTable(), LootPool.lootPool()
                .add(LootItem.lootTableItem(wart)
                        .when(LootItemRandomChanceCondition.randomChance(0.02F))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(NETHER_WART)
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(NetherWartBlock.AGE, 3)))
                )
        ));

        Content.LIVING_FLAME.ifPresent(flame -> addInject("flame_jungle_temple", BuiltInLootTables.JUNGLE_TEMPLE, LootPool.lootPool()
                        .add(LootItem.lootTableItem(flame))
                        .add(EmptyLootItem.emptyItem().setWeight(1)),
                () -> Brazier.SERVER_CONFIG.get().JUNGLE_LOOT
        ));

        Content.ASH.ifPresent(ash -> addInject("wither_ash", EntityType.WITHER_SKELETON.getDefaultLootTable(), LootPool.lootPool()
                        .add(LootItem.lootTableItem(ash)
                                .apply(SetItemCountFunction.setCount(RandomValueBounds.between(-1, 2)))
                                .apply(LootingEnchantFunction.lootingMultiplier(RandomValueBounds.between(0, 1)))
                        ),
                () -> !ModList.get().isLoaded("nether_extension")
        ));
    }

}
