package com.possible_triangle.brazier.datagen.providers;

import static com.possible_triangle.brazier.BrazierConstants.MOD_ID;

import com.possible_triangle.brazier.index.BrazierBlocks;
import com.possible_triangle.brazier.index.BrazierItems;
import com.possible_triangle.brazier.logic.ConstructBrazierTrigger;
import com.tterrag.registrate.providers.RegistrateAdvancementProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class Advancements {


    private static Advancement existing(String path) {
        return Advancement.Builder.advancement().build(new ResourceLocation(path));
    }

    public static void generate(RegistrateAdvancementProvider provider) {
        provider.accept(Advancement.Builder.advancement()
                .addCriterion("placed", ConstructBrazierTrigger.constructedBrazier())
                .display(new DisplayInfo(
                        new ItemStack(BrazierItems.ICON),
                        provider.title(MOD_ID, "place_brazier", "Begone, demon!"),
                        provider.desc(MOD_ID, "place_brazier", "Place a brazier down and drive away the monsters of the night"),
                        null, FrameType.GOAL, true, true, false
                ))
                .parent(existing("adventure/root"))
                .build(BrazierBlocks.BRAZIER.getId())
        );
    }

}
