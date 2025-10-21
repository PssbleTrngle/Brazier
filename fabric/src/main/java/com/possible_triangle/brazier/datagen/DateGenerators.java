package com.possible_triangle.brazier.datagen;

import com.possible_triangle.brazier.BrazierFabric;
import com.possible_triangle.brazier.datagen.providers.AdditionalLang;
import com.possible_triangle.brazier.datagen.providers.Advancements;
import com.possible_triangle.brazier.datagen.providers.LootInjects;
import com.possible_triangle.brazier.datagen.providers.PackMetadata;
import com.tterrag.registrate.providers.ProviderType;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public final class DateGenerators implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        var pack = generator.createPack();
        var existingFiles = ExistingFileHelper.withResourcesFromArg();

        pack.addProvider(PackMetadata::new);
        pack.addProvider(LootInjects::new);

        BrazierFabric.REGISTRATE.addDataGenerator(ProviderType.ADVANCEMENT, Advancements::generate);
        BrazierFabric.REGISTRATE.addDataGenerator(ProviderType.LANG, AdditionalLang::generate);

        BrazierFabric.REGISTRATE.setupDatagen(pack, existingFiles);
    }

}
