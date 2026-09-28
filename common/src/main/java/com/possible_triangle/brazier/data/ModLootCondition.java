package com.possible_triangle.brazier.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.possible_triangle.brazier.index.BrazierContent;
import com.possible_triangle.brazier.platform.Services;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record ModLootCondition(String modId) implements LootItemCondition {

    public static final MapCodec<ModLootCondition> CODEC = RecordCodecBuilder.mapCodec(builder ->
        builder.group(
                Codec.STRING.fieldOf("mod").forGetter(ModLootCondition::modId)
        ).apply(builder, ModLootCondition::new)
    );

    @Override
    public LootItemConditionType getType() {
        return BrazierContent.MOD_CONDITION.get();
    }

    @Override
    public boolean test(LootContext context) {
        return Services.PLATFORM.isModLoaded(modId);
    }

}
