package com.possible_triangle.brazier.compat.rei;

import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.data.LightOnBrazierRecipe;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;

public class ReiPlugin implements REIClientPlugin {

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(LightOnBrazierRecipe.class, REIBrazierDisplay::new);
        LightOnBrazierRecipe.all().forEach(registry::add);
    }

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(REIBrazierDisplay.ID, REIBrazierDisplay.serializer());
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new REIBrazierCategory());
        registry.addWorkstations(REIBrazierDisplay.ID, EntryStacks.of(Content.BRAZIER));
    }

}
