package com.possible_triangle.brazier.data;

import com.possible_triangle.brazier.data.providers.*;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DateGenerators {

    private static final Loot loot = new Loot();

    @SubscribeEvent
    public static void register(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        generator.addProvider(new Recipes(generator));
        generator.addProvider(loot.getProvider(generator));
        generator.addProvider(new Blocks(generator, fileHelper));
        generator.addProvider(new Items(generator, fileHelper));
        generator.addProvider(new Advancements(generator));
    }

}
