package com.possible_triangle.brazier.world;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class InjectLootModifier extends LootModifier {

    public static final MapCodec<InjectLootModifier> CODEC = RecordCodecBuilder.mapCodec(builder ->
            codecStart(builder).and(
                    ResourceLocation.CODEC.fieldOf("loot_table").forGetter(it -> it.lootTable.location())
            ).apply(builder, InjectLootModifier::new)
    );

    private final ResourceKey<LootTable> lootTable;

    public InjectLootModifier(LootItemCondition[] conditions, ResourceLocation lootTable) {
        super(conditions);
        this.lootTable = ResourceKey.create(Registries.LOOT_TABLE, lootTable);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> items, LootContext context) {
        var copy = items.clone();
        var server = context.getLevel().getServer();
        var lootTable = server.reloadableRegistries().getLootTable(this.lootTable);
        lootTable.getRandomItems(context, copy::add);
        return copy;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
