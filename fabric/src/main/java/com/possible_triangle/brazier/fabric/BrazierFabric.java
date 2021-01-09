package com.possible_triangle.fabric;

import com.possible_triangle.Bourgeoisie;
import net.fabricmc.api.ModInitializer;

public class BourgeoisieFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Bourgeoisie.INSTANCE.init();
    }
}
