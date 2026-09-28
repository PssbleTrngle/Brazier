package com.possible_triangle.brazier.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.possible_triangle.brazier.index.BrazierContent;
import com.possible_triangle.brazier.platform.Services;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record ConfigLootCondition(String key) implements LootItemCondition {

    public static final MapCodec<ConfigLootCondition> CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(
                    Codec.STRING.fieldOf("key").forGetter(ConfigLootCondition::key)
            ).apply(builder, ConfigLootCondition::new)
    );

    @Override
    public LootItemConditionType getType() {
        return BrazierContent.CONFIG_CONDITION.get();
    }

    @Override
    public boolean test(LootContext context) {
        return Services.CONFIGS.getValueByKey(key);
    }
}
