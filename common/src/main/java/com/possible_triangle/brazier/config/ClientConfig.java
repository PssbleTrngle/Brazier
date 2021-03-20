package com.possible_triangle.brazier.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "brazier-client")
@Config.Gui.Background("minecraft:textures/block/blackstone.png")
public class ClientConfig implements ConfigData {

   public boolean RENDER_RUNES = true;


}
