package com.possible_triangle.brazier;

import com.possible_triangle.brazier.config.BrazierConfig;
import com.possible_triangle.brazier.item.BrazierIndicator;
import com.possible_triangle.brazier.network.BrazierNetwork;
import me.shedaniel.architectury.event.events.CommandRegistrationEvent;
import me.shedaniel.architectury.event.events.PlayerEvent;
import me.shedaniel.architectury.event.events.TickEvent;
import net.minecraft.client.Minecraft;

public class Brazier {

    public static final String MODID = "brazier";

    public static void init() {
        Content.init();
        BrazierNetwork.init();

        TickEvent.PLAYER_POST.register(BrazierIndicator::playerTick);
    }

    public static void setup() {
        Content.setup();
    }

    public static void clientSetup(Minecraft mc) {
        Content.clientSetup(mc);
    }

}
