package com.possible_triangle.brazier.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.platform.Services;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record ModLootCondition(String modId) implements LootItemCondition {

    public static final Codec<ModLootCondition> CODEC = RecordCodecBuilder.create(builder ->
        builder.group(
                Codec.STRING.fieldOf("mod").forGetter(ModLootCondition::modId)
        ).apply(builder, ModLootCondition::new)
    );

    @Override
    public LootItemConditionType getType() {
        return Content.MOD_CONDITION.get();
    }

    @Override
    public boolean test(LootContext context) {
        return Services.PLATFORM.isModLoaded(modId);
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ModLootCondition> {

        @Override
        public void serialize(JsonObject json, ModLootCondition object, JsonSerializationContext context) {
            CODEC.encode(object, JsonOps.INSTANCE, json).getOrThrow(false, msg -> {
                throw new JsonSyntaxException(msg);
            });
        }

        @Override
        public ModLootCondition deserialize(JsonObject json, JsonDeserializationContext context) {
            return CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(false, msg -> {
                throw new JsonParseException(msg);
            });
        }

    }
}
