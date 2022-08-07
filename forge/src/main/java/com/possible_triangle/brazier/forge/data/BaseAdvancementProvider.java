package com.possible_triangle.brazier.forge.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class BaseAdvancementProvider extends LootTableProvider {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<ResourceLocation, Advancement.Builder> advancements = new HashMap<>();

    private final DataGenerator generator;

    public BaseAdvancementProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
        this.generator = dataGeneratorIn;
    }

    protected abstract void addAdvancements(BiConsumer<ResourceLocation, Advancement.Builder> registry);


    @Override
    public void run(CachedOutput cache) {
        addAdvancements(advancements::put);

        Path outputFolder = this.generator.getOutputFolder();
        advancements.forEach((key, advancement) -> {
            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/advancements/" + key.getPath() + ".json");
            try {
                DataProvider.saveStable(cache, advancement.serializeToJson(), path);
            } catch (IOException e) {
                LOGGER.error("Couldn't write loot table {}", path, e);
            }
        });
    }

}