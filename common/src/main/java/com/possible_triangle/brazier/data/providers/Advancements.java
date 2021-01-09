package com.possible_triangle.brazier.data.providers;

import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.data.BaseAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.PlacedBlockTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.BiConsumer;

@Mod.EventBusSubscriber
public class Advancements extends BaseAdvancementProvider {

    public Advancements(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void addAdvancements(BiConsumer<ResourceLocation, Advancement.Builder> registry) {
        Content.BRAZIER.ifPresent(brazier -> registry.accept(brazier.getRegistryName(), Advancement.Builder.builder()
                .withCriterion("placed", PlacedBlockTrigger.Instance.placedBlock(brazier))
                .withDisplay(new DisplayInfo(
                        new ItemStack(brazier),
                        new TranslationTextComponent("advancements.brazier.place_brazier.title"),
                        new TranslationTextComponent("advancements.brazier.place_brazier.description"),
                        null, FrameType.GOAL, true, true, false
                ))
                .withParentId(new ResourceLocation("adventure/totem_of_undying"))
        ));
    }

}
