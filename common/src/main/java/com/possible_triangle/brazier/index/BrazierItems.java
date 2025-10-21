package com.possible_triangle.brazier.index;

import static com.possible_triangle.brazier.BrazierConstants.createId;
import static com.possible_triangle.brazier.index.BrazierContent.REGISTRATE;
import static com.possible_triangle.brazier.index.BrazierContent.conditionalTab;

import com.possible_triangle.brazier.platform.Services;
import com.possible_triangle.brazier.world.entity.Crazed;
import com.possible_triangle.brazier.world.item.LazySpawnEgg;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class BrazierItems {

    public static final ItemEntry<Item> LIVING_FLAME = REGISTRATE.object("living_flame")
            .item(Item::new)
            .properties(it -> it.rarity(Rarity.UNCOMMON))
            .tab(CreativeModeTabs.INGREDIENTS)
            .register();

    public static final ItemEntry<Item> ASH = REGISTRATE.object("ash")
            .item(Item::new)
            .transform(conditionalTab(CreativeModeTabs.INGREDIENTS, () -> !Services.PLATFORM.isModLoaded("nether_extension") && !Services.PLATFORM.isModLoaded("supplementaries")))
            .register();

    public static final ItemEntry<Item> WARPED_NETHER_WART = REGISTRATE.object("warped_nether_wart")
            .item(Item::new)
            .tab(CreativeModeTabs.INGREDIENTS)
            .transform(conditionalTab(CreativeModeTabs.INGREDIENTS, () -> !Services.PLATFORM.isModLoaded("nether_extension")))
            .register();

    public static final ItemEntry<LazySpawnEgg<Crazed>> CRAZED_SPAWN_EGG = REGISTRATE.object("crazed_spawn_egg")
            .item(props -> new LazySpawnEgg<>(props, BrazierEntities.CRAZED, 0x9804699, 0x89CB07))
            .color(() -> () -> LazySpawnEgg::getColor)
            .model((context, provider) -> provider.withExistingParent(context.getName(), "item/template_spawn_egg"))
            .register();

    public static final ItemEntry<Item> ICON = REGISTRATE.object("icon")
            .item(Item::new)
            .lang("Right-Click Brazier")
            .model((context, provider) -> provider.withExistingParent(context.getName(), createId("block/brazier_lit")))
            .register();

    static void init() {
        // Load this class
    }

}
