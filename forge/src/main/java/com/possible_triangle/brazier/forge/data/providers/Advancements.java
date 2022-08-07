package com.possible_triangle.brazier.forge.data.providers;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.forge.data.BaseAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.PlacedBlockTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

import java.util.function.BiConsumer;

@Mod.EventBusSubscriber
public class Advancements extends BaseAdvancementProvider {

    public Advancements(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void addAdvancements(BiConsumer<ResourceLocation, Advancement.Builder> registry) {
        Content.BRAZIER.ifPresent(brazier -> registry.accept(new ResourceLocation(Brazier.MOD_ID, "brazier"), Advancement.Builder.advancement()
                .addCriterion("placed", PlacedBlockTrigger.TriggerInstance.placedBlock(brazier))
                .display(new DisplayInfo(
                        new ItemStack(brazier),
                        Component.translatable("advancements.brazier.place_brazier.title"),
                        Component.translatable("advancements.brazier.place_brazier.description"),
                        null, FrameType.GOAL, true, true, false
                ))
                .parent(new ResourceLocation("adventure/totem_of_undying"))
        ));
    }

}
