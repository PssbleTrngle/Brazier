package com.possible_triangle.brazier.compat.rei;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.LightOnBrazierRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public class REIBrazierDisplay extends BasicDisplay {

    public static final CategoryIdentifier<REIBrazierDisplay> ID = CategoryIdentifier.of(Brazier.MOD_ID, "light_on_brazier");

    private REIBrazierDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> location) {
        super(inputs, outputs, location);
    }


    public REIBrazierDisplay(LightOnBrazierRecipe recipe) {
        this(
                List.of(EntryIngredients.ofIngredient(recipe.input())),
                List.of(EntryIngredients.of(recipe.output())),
                Optional.empty()
        );
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ID;
    }

    public static BasicDisplay.Serializer<REIBrazierDisplay> serializer() {
        return BasicDisplay.Serializer.ofSimple(REIBrazierDisplay::new);
    }

    public final EntryIngredient getIn() {
        return getInputEntries().get(0);
    }

    public final EntryIngredient getOut() {
        return getOutputEntries().get(0);
    }
}
