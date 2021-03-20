package com.possible_triangle.brazier.fabric;

import com.possible_triangle.brazier.Brazier;
import net.fabricmc.api.ModInitializer;

@SuppressWarnings("unused")
public class BrazierFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Brazier.init();
        Brazier.setup();
    }

}
