package com.possible_triangle.brazier.logic;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.possible_triangle.brazier.BrazierConstants;

import java.util.Optional;

import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ConstructBrazierTrigger extends SimpleCriterionTrigger<ConstructBrazierTrigger.TriggerInstance> {
    public static final ResourceLocation ID = BrazierConstants.createId("construct_brazier");

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, int height) {
        trigger(player, (instance) -> instance.height().matches(height));
    }

    public record TriggerInstance(MinMaxBounds.Ints height) implements SimpleInstance {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(builder ->
                builder.group(
                        MinMaxBounds.Ints.CODEC.fieldOf("height").forGetter(TriggerInstance::height)
                ).apply(builder, TriggerInstance::new)
        );

        @Override
        public Optional<ContextAwarePredicate> player() {
            return Optional.empty();
        }

    }

}
