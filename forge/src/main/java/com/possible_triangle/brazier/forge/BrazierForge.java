package com.possible_triangle.forge

import com.possible_triangle.brazier.Brazier
import me.shedaniel.architectury.platform.forge.EventBuses
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.MOD_CONTEXT

@Mod(Bourgeoisie.MOD_ID)
public class BrazierForge {

    public BrazierForge () {
        EventBuses.registerModEventBus(Brazier.MOD_ID, MOD_CONTEXT.getKEventBus())
        Brazier.init()
    }
}