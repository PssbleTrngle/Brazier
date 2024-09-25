package com.possible_triangle.brazier;

import com.possible_triangle.brazier.network.BrazierNetwork;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Brazier {
    
    private Brazier() {}

    public static final String MOD_ID = "brazier";

    public static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        Content.init();
        BrazierNetwork.init();
    }

    public static void setup() {
    }

    public static void clientSetup() {
    }

}
