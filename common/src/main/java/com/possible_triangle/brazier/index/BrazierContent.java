package com.possible_triangle.brazier.index;


import com.possible_triangle.brazier.data.ConfigLootCondition;
import com.possible_triangle.brazier.data.ModLootCondition;
import com.possible_triangle.brazier.logic.ConstructBrazierTrigger;
import com.possible_triangle.brazier.platform.Services;
import com.possible_triangle.multikulti.registrate.MultikultiRegistrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import java.util.function.BooleanSupplier;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class BrazierContent {

    static final MultikultiRegistrate<?> REGISTRATE = Services.PLATFORM.getRegistrate();

    public static void init() {
        REGISTRATE.defaultCreativeTab((ResourceKey<CreativeModeTab>) null);

        BrazierItems.init();
        BrazierBlocks.init();
        BrazierEntities.init();
    }

    public static final RegistryEntry<ParticleType<?>, SimpleParticleType> FLAME_PARTICLE = REGISTRATE.object("flame")
            .particle()
            .provider(() -> FlameParticle.Provider::new)
            .register();

    public static final RegistryEntry<CriterionTrigger<?>, ConstructBrazierTrigger> CONSTRUCT_BRAZIER = REGISTRATE.object("construct_brazier")
            .generic(Registries.TRIGGER_TYPE, ConstructBrazierTrigger::new)
            .register();

    public static final RegistryEntry<LootItemConditionType, LootItemConditionType> CONFIG_CONDITION = REGISTRATE.object("config")
            .generic(Registries.LOOT_CONDITION_TYPE, () -> new LootItemConditionType(ConfigLootCondition.CODEC))
            .register();

    // TODO remove and use load conditions?
    public static final RegistryEntry<LootItemConditionType, LootItemConditionType> MOD_CONDITION = REGISTRATE.object("mod_loaded")
            .generic(Registries.LOOT_CONDITION_TYPE, () -> new LootItemConditionType(ModLootCondition.CODEC))
            .register();

    public static <T extends Item, P> NonNullFunction<ItemBuilder<T, P>, ItemBuilder<T, P>> conditionalTab(ResourceKey<CreativeModeTab> tab, BooleanSupplier test) {
        return it -> it.tab(tab, mod -> {
            if (test.getAsBoolean()) mod.accept(it.getEntry());
        });
    }

}