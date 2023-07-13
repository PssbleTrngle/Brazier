package com.possible_triangle.brazier.datagen;

import com.possible_triangle.brazier.datagen.providers.Advancements;
import com.possible_triangle.brazier.datagen.providers.Loot;
import com.possible_triangle.brazier.datagen.providers.Recipes;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DateGenerators implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        var pack = generator.createPack();
        pack.addProvider(Advancements::new);
        pack.addProvider(Recipes::new);
        pack.addProvider(Loot.Blocks::new);
        pack.addProvider(Loot.Entities::new);
        pack.addProvider(Loot.Injects::new);
    }

}
