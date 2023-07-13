package com.possible_triangle.brazier.datagen.providers;

import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.logic.ConstructBrazierTrigger;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.function.Consumer;

public class Advancements extends FabricAdvancementProvider {

    public Advancements(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateAdvancement(Consumer<Advancement> consumer) {
        var parent = new Advancement(new ResourceLocation("adventure/root"), null, null, null, Collections.emptyMap(), null, false);

        Content.BRAZIER.ifPresent(brazier -> consumer.accept(Advancement.Builder.advancement()
                .addCriterion("placed", ConstructBrazierTrigger.constructedBrazier())
                .display(new DisplayInfo(
                        new ItemStack(brazier),
                        Component.translatable("advancements.brazier.place_brazier.title"),
                        Component.translatable("advancements.brazier.place_brazier.description"),
                        null, FrameType.GOAL, true, true, false
                ))
                .parent(parent)
                .build(Content.BRAZIER.getKey().location())
        ));
    }

}
