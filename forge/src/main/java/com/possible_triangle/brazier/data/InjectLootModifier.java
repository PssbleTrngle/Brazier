package com.possible_triangle.brazier.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

public class InjectLootModifier extends LootModifier {

    public static final Codec<InjectLootModifier> CODEC = RecordCodecBuilder.create(builder ->
            codecStart(builder).and(
                    Codec.STRING.fieldOf("loot_table").forGetter(it -> it.lootTable.toString())
            ).apply(builder, InjectLootModifier::new)
    );

    private final ResourceLocation lootTable;

    public InjectLootModifier(LootItemCondition[] conditions, String lootTable) {
        super(conditions);
        this.lootTable = new ResourceLocation(lootTable);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> items, LootContext context) {
        var copy = items.clone();
        var server = context.getLevel().getServer();
        var lootTable = server.getLootData().getLootTable(this.lootTable);
        lootTable.getRandomItems(context, copy::add);
        return copy;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
