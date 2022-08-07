package com.possible_triangle.brazier.forge.data;

import com.possible_triangle.brazier.forge.data.providers.*;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DateGenerators {

    @SubscribeEvent
    public static void register(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        generator.addProvider(event.includeServer(), new Recipes(generator));
        generator.addProvider(event.includeServer(), new Loot(generator));
        generator.addProvider(event.includeClient(), new Blocks(generator, fileHelper));
        generator.addProvider(event.includeClient(), new Items(generator, fileHelper));
        generator.addProvider(event.includeServer(), new Advancements(generator));
    }

}
