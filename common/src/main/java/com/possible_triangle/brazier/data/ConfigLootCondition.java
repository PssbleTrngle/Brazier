package com.possible_triangle.brazier.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.possible_triangle.brazier.index.BrazierContent;
import com.possible_triangle.brazier.platform.Services;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record ConfigLootCondition(String key) implements LootItemCondition {

    public static final Codec<ConfigLootCondition> CODEC = RecordCodecBuilder.create(builder ->
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

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ConfigLootCondition> {

        @Override
        public void serialize(JsonObject json, ConfigLootCondition object, JsonSerializationContext context) {
            CODEC.encode(object, JsonOps.INSTANCE, json).getOrThrow(false, msg -> {
                throw new JsonSyntaxException(msg);
            });
        }

        @Override
        public ConfigLootCondition deserialize(JsonObject json, JsonDeserializationContext context) {
            return CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(false, msg -> {
                throw new JsonParseException(msg);
            });
        }

    }
}
