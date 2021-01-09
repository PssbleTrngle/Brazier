package com.possible_triangle.brazier.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public abstract class BaseAdvancementProvider extends LootTableProvider {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final Map<ResourceLocation, Advancement.Builder> advancements = new HashMap<>();

    private final DataGenerator generator;

    public BaseAdvancementProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
        this.generator = dataGeneratorIn;
    }

    protected abstract void addAdvancements(BiConsumer<ResourceLocation,Advancement.Builder> registry);

    @Override
    public void act(DirectoryCache cache) {
        addAdvancements(advancements::put);

        Path outputFolder = this.generator.getOutputFolder();
        advancements.forEach((key, advancement) -> {
            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/advancements/" + key.getPath() + ".json");
            try {
                IDataProvider.save(GSON, cache, advancement.serialize(), path);
            } catch (IOException e) {
                LOGGER.error("Couldn't write loot table {}", path, e);
            }
        });
    }

}