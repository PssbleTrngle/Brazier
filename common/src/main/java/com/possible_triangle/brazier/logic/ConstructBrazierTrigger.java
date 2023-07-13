package com.possible_triangle.brazier.logic;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ConstructBrazierTrigger  extends SimpleCriterionTrigger<ConstructBrazierTrigger.TriggerInstance> {
    public static final ResourceLocation ID = new ResourceLocation("construct_brazier");

    @Override
    protected ConstructBrazierTrigger.TriggerInstance createInstance(JsonObject jsonObject, ContextAwarePredicate contextAwarePredicate, DeserializationContext deserializationContext) {
        MinMaxBounds.Ints ints = MinMaxBounds.Ints.fromJson(jsonObject.get("height"));
        return new ConstructBrazierTrigger.TriggerInstance(contextAwarePredicate, ints);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player, int height) {
        trigger(player, (instance) -> instance.matches(height));
    }

    public static ConstructBrazierTrigger.TriggerInstance constructedBrazier() {
        return new ConstructBrazierTrigger.TriggerInstance(ContextAwarePredicate.ANY, MinMaxBounds.Ints.ANY);
    }

    public static ConstructBrazierTrigger.TriggerInstance constructedBrazier(MinMaxBounds.Ints ints) {
        return new ConstructBrazierTrigger.TriggerInstance(ContextAwarePredicate.ANY, ints);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final MinMaxBounds.Ints height;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate, MinMaxBounds.Ints ints) {
            super(ConstructBrazierTrigger.ID, contextAwarePredicate);
            this.height = ints;
        }

        public boolean matches(int height) {
            return this.height.matches(height);
        }

        public JsonObject serializeToJson(SerializationContext serializationContext) {
            JsonObject jsonObject = super.serializeToJson(serializationContext);
            jsonObject.add("height", this.height.serializeToJson());
            return jsonObject;
        }
    }

}
