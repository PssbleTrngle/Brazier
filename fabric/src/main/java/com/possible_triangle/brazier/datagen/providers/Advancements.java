package com.possible_triangle.brazier.datagen.providers;

import static com.possible_triangle.brazier.BrazierConstants.MOD_ID;

import com.possible_triangle.brazier.index.BrazierBlocks;
import com.possible_triangle.brazier.index.BrazierContent;
import com.possible_triangle.brazier.index.BrazierItems;
import com.possible_triangle.brazier.logic.ConstructBrazierTrigger;
import com.tterrag.registrate.providers.RegistrateAdvancementProvider;
import java.util.Optional;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.resources.ResourceLocation;

public class Advancements {

    private static AdvancementHolder existing(String path) {
        return Advancement.Builder.advancement().build(ResourceLocation.parse(path));
    }

    private static Criterion<ConstructBrazierTrigger.TriggerInstance> constructedBrazier() {
        return constructedBrazier(MinMaxBounds.Ints.ANY);
    }

    private static Criterion<ConstructBrazierTrigger.TriggerInstance> constructedBrazier(MinMaxBounds.Ints ints) {
        return BrazierContent.CONSTRUCT_BRAZIER.value().createCriterion(new ConstructBrazierTrigger.TriggerInstance(ints));
    }

    public static void generate(RegistrateAdvancementProvider provider) {
        provider.accept(Advancement.Builder.advancement()
                .addCriterion("placed", constructedBrazier())
                .display(new DisplayInfo(
                        BrazierItems.ICON.asStack(),
                        provider.title(MOD_ID, "place_brazier", "Begone, demon!"),
                        provider.desc(MOD_ID, "place_brazier", "Place a brazier down and drive away the monsters of the night"),
                        Optional.empty(), AdvancementType.GOAL, true, true, false
                ))
                .parent(existing("adventure/root"))
                .build(BrazierBlocks.BRAZIER.getId())
        );
    }

}
