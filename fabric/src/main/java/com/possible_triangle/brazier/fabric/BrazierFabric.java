package com.possible_triangle.brazier.fabric;

import com.possible_triangle.brazier.Brazier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

@SuppressWarnings("unused")
public class BrazierFabric implements ModInitializer, ClientModInitializer {

    @Override
    public void onInitialize() {
        Brazier.init();
        Brazier.setup();
    }

    @Override
    public void onInitializeClient() {
        Brazier.clientSetup();
    }
}
