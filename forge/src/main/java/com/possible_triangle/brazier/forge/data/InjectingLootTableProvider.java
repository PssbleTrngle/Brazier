package com.possible_triangle.brazier.forge.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.possible_triangle.brazier.Brazier.MOD_ID;

public abstract class InjectingLootTableProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private final List<Inject> injects = new ArrayList<>();
    private final List<Consumer<BaseLootTableProvider>> tables = new ArrayList<>();

    public InjectingLootTableProvider() {
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::onLootLoaded);
    }

    protected void addInject(String injectName, ResourceLocation into, LootPool.Builder pool) {
        this.addInject(injectName, into, pool, () -> true);
    }

    protected void addInject(String injectName, ResourceLocation into, LootPool.Builder pool, Supplier<Boolean> predicate) {
        ResourceLocation loc = new ResourceLocation(MOD_ID, "inject/" + injectName);
        if (injects.stream().anyMatch(i -> i.name.equals(loc))) {
            LOGGER.warn(String.format("Attempted to add duplicate inject table '%s'", injectName));
        } else {
            addLootTable(loc, LootTable.lootTable().withPool(pool.name("inject")));
            injects.add(new Inject(loc, into, predicate));
        }
    }

    protected abstract void addTables();

    protected void addLootTable(ResourceLocation name, LootTable.Builder table) {
        tables.add(provider -> provider.addLootTable(name, table));
    }

    public DataProvider getProvider(DataGenerator generator) {
        return new BaseLootTableProvider(generator) {
            @Override
            protected void addTables() {
                InjectingLootTableProvider.this.addTables();
                tables.forEach(c -> c.accept(this));
            }
        };
    }

    public void onLootLoaded(LootTableLoadEvent event) {
        if(injects.isEmpty()) addTables();
        long injected = injects.stream()
                .filter(i -> i.predicate.get())
                .filter(i -> i.into.equals(event.getName()))
                .map(i -> LootPool.lootPool().add(LootTableReference.lootTableReference(i.name)).name(i.name.getPath()).build())
                .peek(event.getTable()::addPool)
                .count();

        if (injected > 0) LOGGER.info(String.format("Injected %d pools into '%s'", injected, event.getName()));
    }

    public static class Inject {
        public final ResourceLocation name;
        public final ResourceLocation into;
        public final Supplier<Boolean> predicate;

        private Inject(ResourceLocation inject, ResourceLocation into, Supplier<Boolean> predicate) {
            this.name = inject;
            this.into = into;
            this.predicate = predicate;
        }
    }

}
