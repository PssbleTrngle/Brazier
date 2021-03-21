package com.possible_triangle.brazier.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "brazier-common")
@Config.Gui.Background("minecraft:textures/block/blackstone.png")
public class ServerConfig implements ConfigData {

   @ConfigEntry.Category("acquisition")
   public boolean JUNGLE_LOOT = true;

   @ConfigEntry.Category("acquisition")
   public boolean SPAWN_CRAZED = true;

   @ConfigEntry.Category("acquisition")
   public double CRAZED_CHANCE = 0.4;

   @ConfigEntry.Category("brazier")
   @ConfigEntry.BoundedDiscrete(min = 1, max = Integer.MAX_VALUE)
   public int MAX_HEIGHT = 10;

   @ConfigEntry.Category("brazier")
   @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
   public int RANGE_PER_LEVEL = 10;

   @ConfigEntry.Category("brazier")
   @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
   public int BASE_RANGE = 20;

   @ConfigEntry.Category("brazier")
   public boolean PROTECT_ABOVE = false;

   @ConfigEntry.Category("brazier")
   public boolean SPAWN_POWDER = true;

   @ConfigEntry.Category("brazier")
   @ConfigEntry.Gui.Tooltip
   @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
   public DistanceHandler.Type DISTANCE_CALC = DistanceHandler.Type.CYLINDER;


}
