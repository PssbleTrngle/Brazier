package com.possible_triangle.brazier.data;

import com.possible_triangle.brazier.Content;
import net.minecraft.advancements.*;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.PlacedBlockTrigger;
import net.minecraft.block.Block;
import net.minecraft.data.AdvancementProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdventureAdvancements;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.function.BiConsumer;

import static net.minecraft.advancements.IRequirementsStrategy.AND;

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
