package com.possible_triangle.brazier.datagen;

import com.possible_triangle.brazier.platform.Services;
import com.tterrag.registrate.providers.ProviderType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber
public class BrazierData {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void setup(GatherDataEvent event) {
        var registrate = Services.PLATFORM.getRegistrate();

        event.createProvider(PackMetadata::new);

        registrate.addDataGenerator(ProviderType.LOOT, provider -> {
            provider.addLootAction(LootContextParamSets.EMPTY, LootInjects::generate);
        });

        registrate.addDataGenerator(ProviderType.ADVANCEMENT, Advancements::generate);
        registrate.addDataGenerator(ProviderType.LANG, AdditionalLang::generate);
    }

}
