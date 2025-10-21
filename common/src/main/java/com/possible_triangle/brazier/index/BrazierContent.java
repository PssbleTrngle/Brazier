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
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class BrazierContent {

    static final MultikultiRegistrate<?> REGISTRATE = Services.PLATFORM.getRegistrate();

    public static void init() {
        BrazierItems.init();
        BrazierBlocks.init();
        BrazierEntities.init();
    }

    public static final RegistryEntry<SimpleParticleType> FLAME_PARTICLE = REGISTRATE.object("flame")
            .particle()
            .provider(() -> FlameParticle.Provider::new)
            .register();

    public static final ConstructBrazierTrigger CONSTRUCT_BRAZIER = CriteriaTriggers.register(new ConstructBrazierTrigger());

    public static final RegistryEntry<LootItemConditionType> CONFIG_CONDITION = REGISTRATE.object("config")
            .generic(Registries.LOOT_CONDITION_TYPE, () -> new LootItemConditionType(new ConfigLootCondition.Serializer()))
            .register();

    public static final RegistryEntry<LootItemConditionType> MOD_CONDITION = REGISTRATE.object("mod_loaded")
            .generic(Registries.LOOT_CONDITION_TYPE, () -> new LootItemConditionType(new ModLootCondition.Serializer()))
            .register();

    public static <T extends Item, P> NonNullFunction<ItemBuilder<T, P>, ItemBuilder<T, P>> conditionalTab(ResourceKey<CreativeModeTab> tab, BooleanSupplier test) {
        return it -> it.tab(tab, mod -> {
            if (test.getAsBoolean()) mod.accept(it.getEntry());
        });
    }

}