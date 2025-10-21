package com.possible_triangle.brazier.index;

import com.possible_triangle.brazier.platform.Services;
import com.possible_triangle.brazier.world.entity.Crazed;
import com.possible_triangle.brazier.world.entity.CrazedFlame;
import com.possible_triangle.brazier.world.entity.render.CrazedFlameRenderer;
import com.possible_triangle.brazier.world.entity.render.CrazedRender;
import com.tterrag.registrate.util.entry.EntityEntry;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class BrazierEntities {

    public static final EntityEntry<Crazed> CRAZED = BrazierContent.REGISTRATE.object("crazed")
            .entity(Crazed::new, MobCategory.MONSTER)
            .attributes(Crazed::createAttributes)
            .renderer(() -> CrazedRender::new)
            .transform(Services.PLATFORM::fireImmune)
            .transform(Services.PLATFORM.sized(EntityDimensions.fixed(2F, 0.5F)))
            .loot((tables, entry) -> {
                tables.add(entry, LootTable.lootTable()
                        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(BrazierItems.LIVING_FLAME))
                        ));
            })
            .register();

    public static final EntityEntry<CrazedFlame> CRAZED_FLAME = BrazierContent.REGISTRATE.object("crazed_flame")
            .entity((EntityType.EntityFactory<CrazedFlame>) CrazedFlame::new, MobCategory.MISC)
            .renderer(() -> CrazedFlameRenderer::new)
            .transform(Services.PLATFORM::fireImmune)
            .transform(Services.PLATFORM.sized(EntityDimensions.fixed(0.6F, 0.6F)))
            .register();

    static void init() {
        // Load this class
    }
}
