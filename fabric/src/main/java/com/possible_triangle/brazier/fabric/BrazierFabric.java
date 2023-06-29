package com.possible_triangle.brazier.fabric;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Conditional;
import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.item.LazySpawnEgg;
import com.possible_triangle.brazier.particle.fabric.ParticleRegistryImpl;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;

@SuppressWarnings("unused")
public class BrazierFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        Brazier.init();
        Brazier.setup();

        LootTableEvents.MODIFY.register((resourceManager, manager, id, supplier, setter) ->
                Conditional.injectLoot(id, supplier::withPool)
        );

    }

    @Override
    public void onInitializeClient() {
        Brazier.clientSetup();
        ParticleRegistryImpl.register();

        Content.CRAZED_SPAWN_EGG.ifPresent(egg ->
                ColorProviderRegistry.ITEM.register(LazySpawnEgg::getColor, egg)
        );
    }
}
