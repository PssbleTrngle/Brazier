package com.possible_triangle.brazier;

import com.mojang.datafixers.util.Pair;
import com.possible_triangle.brazier.config.ServerConfig;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Conditional {

    private static final List<Conditional> CONDITIONALS = new ArrayList<>();

    @SafeVarargs
    public static Conditional when(Predicate<ServerConfig> condition, RegistrySupplier<? extends ItemLike>... items) {
        Conditional conditional = new Conditional(condition);
        CONDITIONALS.add(conditional);
        if (items.length > 0) conditional.add(items);
        return conditional;
    }

    public static Stream<ItemLike> disabled() {
        ServerConfig config = Brazier.serverConfig();
        return CONDITIONALS.stream()
                .filter(e -> !e.condition.test(config))
                .map(c -> c.items)
                .flatMap(Collection::stream)
                .filter(RegistrySupplier::isPresent)
                .map(Supplier::get);
    }

    public static void injectLoot(ResourceLocation target, Consumer<LootPool.Builder> table) {
        ServerConfig config = Brazier.serverConfig();

        CONDITIONALS.stream()
                .filter(e -> e.condition.test(config))
                .map(conditional -> conditional.loot)
                .flatMap(Collection::stream)
                .filter(p -> p.getFirst().equals(target))
                .map(p -> LootPool.lootPool().add(LootTableReference.lootTableReference(p.getSecond())))
                .forEach(table);
    }

    public static void removeTags(RegistryAccess registries) {
        // TODO remove tags from disabled items
        /*var items = registries.registryOrThrow(Registry.ITEM_REGISTRY);
        disabled().map(ItemLike::asItem).forEach(item -> {
            var key = items.getResourceKey(item);
            key.map(items::getHolder).flatMap(Function.identity()).ifPresent(holder -> {
                holder.tags().forEach(tag -> {
                    items.getOrCreateTag(tag).
                });
            });
        });
        */
    }

    public final Predicate<ServerConfig> condition;
    public final List<RegistrySupplier<? extends ItemLike>> items = new ArrayList<>();
    public final List<Pair<ResourceLocation, ResourceLocation>> loot = new ArrayList<>();

    private Conditional(Predicate<ServerConfig> condition) {
        this.condition = condition;
    }

    @SafeVarargs
    final Conditional add(RegistrySupplier<? extends ItemLike>... item) {
        items.addAll(Arrays.asList(item));
        return this;
    }

    final Conditional loot(ResourceLocation target, ResourceLocation inject) {
        loot.add(new Pair<>(target, inject));
        return this;
    }

    final Conditional loot(ResourceLocation target, String inject) {
        return loot(target, new ResourceLocation(Brazier.MOD_ID, "inject/" + inject));
    }

}
