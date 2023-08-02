package com.possible_triangle.brazier.compat.rei;

import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.compat.DisplayConstants;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class REIBrazierCategory implements DisplayCategory<REIBrazierDisplay> {
    private final EntryStack<ItemStack> icon = EntryStacks.of(Content.ICON.get());

    @Override
    public CategoryIdentifier getCategoryIdentifier() {
        return REIBrazierDisplay.ID;
    }

    @Override
    public Component getTitle() {
        return DisplayConstants.TITLE;
    }

    @Override
    public Renderer getIcon() {
        return icon;
    }

    @Override
    public int getDisplayHeight() {
        return DisplayConstants.HEIGHT;
    }

    @Override
    public int getDisplayWidth(REIBrazierDisplay display) {
        return DisplayConstants.WIDTH;
    }

    @Override
    public List<Widget> setupDisplay(REIBrazierDisplay display, Rectangle bounds) {
        return List.of(
                Widgets.createRecipeBase(bounds),
                Widgets.createSlot(new Point(bounds.x + 10, bounds.y + bounds.height / 2 - 9)).entries(display.getIn()).markInput(),
                Widgets.createSlot(new Point(bounds.x + bounds.width / 2 - 9, bounds.y + bounds.height / 2 - 9)).entry(icon).disableBackground(),
                Widgets.createSlot(new Point(bounds.x + bounds.width - 25, bounds.y + bounds.height / 2 - 9)).entries(display.getOut()).markOutput()
        );
    }
}