package com.possible_triangle.brazier.datagen.providers;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static net.minecraft.world.level.block.Blocks.NETHER_WART;

public class Loot {

    private static ResourceLocation inject(String name) {
        return new ResourceLocation(Brazier.MOD_ID, "inject/" + name);
    }

    public static class Entities extends SimpleFabricLootTableProvider {

        public Entities(FabricDataOutput output) {
            super(output, LootContextParamSets.ENTITY);
        }

        @Override
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> registry) {
            Content.CRAZED.ifPresent(type -> registry.accept(type.getDefaultLootTable(), LootTable.lootTable()
                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(Content.LIVING_FLAME.orElse(Items.BLAZE_POWDER)))
                    )
            ));
        }
    }

    public static class Blocks extends FabricBlockLootTableProvider {

        public Blocks(FabricDataOutput output) {
            super(output);
        }


        @Override
        public void generate() {
            Stream.of(Content.BRAZIER, Content.LIVING_TORCH_BLOCK, Content.LIVING_TORCH_BLOCK_WALL, Content.SPAWN_POWDER)
                    .filter(RegistrySupplier::isPresent).map(RegistrySupplier::get)
                    .forEach(block -> add(block, LootTable.lootTable()
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(block))
                                    .when(ExplosionCondition.survivesExplosion())
                            )
                    ));
        }
    }

    public static class Injects extends SimpleFabricLootTableProvider {

        public Injects(FabricDataOutput output) {
            super(output, LootContextParamSets.EMPTY);
        }

        @Override
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> registry) {
            Content.WARPED_NETHERWART.ifPresent(wart -> registry.accept(inject("warped_wart"), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .add(LootItem.lootTableItem(wart)
                                    .when(LootItemRandomChanceCondition.randomChance(0.02F))
                                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(NETHER_WART)
                                            .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(NetherWartBlock.AGE, 3)))
                            )
                    )
            ));

            Content.LIVING_FLAME.ifPresent(flame -> registry.accept(inject("flame_jungle_temple"), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .add(LootItem.lootTableItem(flame))
                            .add(EmptyLootItem.emptyItem().setWeight(1))
                    )
            ));

            Content.ASH.ifPresent(ash -> registry.accept(inject("wither_ash"), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .add(LootItem.lootTableItem(ash)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(-1, 2)))
                                    .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))
                            )
                    )
            ));
        }
    }

}
