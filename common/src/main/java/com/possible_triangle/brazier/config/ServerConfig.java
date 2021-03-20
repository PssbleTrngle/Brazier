package com.possible_triangle.brazier.config;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.network.BrazierNetwork;
import com.possible_triangle.brazier.network.SyncConfigMessage;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Config(name = "brazier-common")
@Config.Gui.Background("minecraft:textures/block/blackstone.png")
public class ServerConfig implements ConfigData {

   public boolean JUNGLE_LOOT = true;

   public boolean SPAWN_CRAZED = true;
   public double CRAZED_CHANCE = 0.4;

   @ConfigEntry.BoundedDiscrete(min = 1, max = Integer.MAX_VALUE)
   public int MAX_HEIGHT = 10;
   @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
   public int RANGE_PER_LEVEL = 10;
   @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
   public int BASE_RANGE = 20;

   public boolean PROTECT_ABOVE = false;

   public boolean SPAWN_POWDER = true;

   @ConfigEntry.Gui.Tooltip
   @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
   public DistanceHandler.Type DISTANCE_CALC = DistanceHandler.Type.CYLINDER;


}
