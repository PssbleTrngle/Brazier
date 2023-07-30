package com.possible_triangle.brazier.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "brazier-common")
@Config.Gui.Background("minecraft:textures/block/blackstone.png")
public class ServerConfig implements ConfigData, IServerConfig {

   @ConfigEntry.Category("acquisition")
   public boolean JUNGLE_LOOT = true;

   @Override
   public boolean injectJungleLoot() {
      return JUNGLE_LOOT;
   }

   @ConfigEntry.Category("acquisition")
   public boolean SPAWN_CRAZED = true;

   @Override
   public boolean spawnCrazed() {
      return SPAWN_CRAZED;
   }

   @ConfigEntry.Category("acquisition")
   public double CRAZED_CHANCE = 0.6;

   @Override
   public double crazedSpawnChance() {
      return CRAZED_CHANCE;
   }

   @ConfigEntry.Category("brazier")
   @ConfigEntry.BoundedDiscrete(min = 1, max = Integer.MAX_VALUE)
   public int MAX_HEIGHT = 10;

   @Override
   public int maxHeight() {
      return MAX_HEIGHT;
   }

   @ConfigEntry.Category("brazier")
   @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
   public int RANGE_PER_LEVEL = 10;

   @Override
   public int rangePerLevel() {
      return RANGE_PER_LEVEL;
   }

   @ConfigEntry.Category("brazier")
   @ConfigEntry.BoundedDiscrete(min = 0, max = Integer.MAX_VALUE)
   public int BASE_RANGE = 20;

   @Override
   public int baseRange() {
      return BASE_RANGE;
   }

   @ConfigEntry.Category("brazier")
   public boolean PROTECT_ABOVE = false;

   @Override
   public boolean protectAbove() {
      return PROTECT_ABOVE;
   }

   @ConfigEntry.Category("brazier")
   @ConfigEntry.Gui.Tooltip
   @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
   public DistanceHandler.Type DISTANCE_CALC = DistanceHandler.Type.CYLINDER;

   @Override
   public DistanceHandler.Type distanceCalculator() {
      return DISTANCE_CALC;
   }

   @ConfigEntry.Category("content")
   public boolean SPAWN_POWDER = true;

   @Override
   public boolean enableSpawnPowder() {
      return SPAWN_POWDER;
   }

   @ConfigEntry.Category("content")
   public boolean DECORATION = true;

   @Override
   public boolean enableDecoration() {
      return DECORATION;
   }
}
