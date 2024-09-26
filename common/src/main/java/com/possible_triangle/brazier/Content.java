package com.possible_triangle.brazier;

import com.possible_triangle.brazier.data.ConfigLootCondition;
import com.possible_triangle.brazier.data.ModLootCondition;
import com.possible_triangle.brazier.logic.ConstructBrazierTrigger;
import com.possible_triangle.brazier.platform.Services;
import com.possible_triangle.brazier.world.block.BrazierBlock;
import com.possible_triangle.brazier.world.block.LazyTorchBlock;
import com.possible_triangle.brazier.world.block.LazyWallTorchBlock;
import com.possible_triangle.brazier.world.block.SpawnPowder;
import com.possible_triangle.brazier.world.block.tile.BrazierBlockEntity;
import com.possible_triangle.brazier.world.entity.Crazed;
import com.possible_triangle.brazier.world.entity.CrazedFlame;
import com.possible_triangle.brazier.world.entity.render.CrazedFlameRenderer;
import com.possible_triangle.brazier.world.entity.render.CrazedRender;
import com.possible_triangle.brazier.world.item.LazySpawnEgg;
import com.possible_triangle.brazier.world.particle.ModdedParticleType;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static com.possible_triangle.brazier.Brazier.MOD_ID;

public class Content {

    private static final AbstractRegistrate<?> REGISTRATE = Services.PLATFORM.getRegistrate();

    private Content() {
    }

    private static ResourceLocation id(String key) {
        return new ResourceLocation(MOD_ID, key);
    }

    public static final TagKey<Block> BRAZIER_BASE_BLOCKS = TagKey.create(Registries.BLOCK, id("brazier_base_blocks"));
    public static final TagKey<Block> BRAZIER_STRIPE_BLOCKS = TagKey.create(Registries.BLOCK, id("brazier_stripe_blocks"));

    public static final TagKey<EntityType<?>> BRAZIER_WHITELIST = TagKey.create(Registries.ENTITY_TYPE, id("brazier_whitelist"));
    public static final TagKey<EntityType<?>> BRAZIER_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, id("brazier_blacklist"));
    public static final TagKey<Item> TORCHES = TagKey.create(Registries.ITEM, id("torches"));
    public static final TagKey<Item> ASH_TAG = TagKey.create(Registries.ITEM, id("ash"));
    public static final TagKey<Item> RANGE_INDICATOR = TagKey.create(Registries.ITEM, id("range_indicator"));
    public static final TagKey<Item> WARPED_WART_TAG = TagKey.create(Registries.ITEM, id("warped_wart"));

    public static final RegistryEntry<ModdedParticleType> FLAME_PARTICLE = REGISTRATE.object("flame")
            .generic(Registries.PARTICLE_TYPE, () -> new ModdedParticleType(false))
            .register();

    public static final BlockEntry<BrazierBlock> BRAZIER = REGISTRATE.object("brazier")
            .block(BrazierBlock::new)
            .properties(it -> it
                    .strength(1.5F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .lightLevel(s -> s.getValue(BrazierBlock.LIT) ? 15 : 0)
            )
            .addLayer(() -> RenderType::cutout)
            .item()
            .tab(CreativeModeTabs.FUNCTIONAL_BLOCKS)
            .build()
            .register();

    public static final BlockEntityEntry<BrazierBlockEntity> BRAZIER_TILE = REGISTRATE.object("brazier")
            .blockEntity(BrazierBlockEntity::new)
            .validBlock(BRAZIER)
            .register();

    public static final BlockEntry<LazyWallTorchBlock> LIVING_TORCH_BLOCK_WALL = REGISTRATE.object("living_wall_torch")
            .block(props -> new LazyWallTorchBlock(props, FLAME_PARTICLE))
            .initialProperties(() -> Blocks.WALL_TORCH)
            .addLayer(() -> RenderType::cutout)
            .register();

    public static final BlockEntry<LazyTorchBlock> LIVING_TORCH = REGISTRATE.object("living_torch")
            .block(props -> new LazyTorchBlock(props, FLAME_PARTICLE))
            .initialProperties(() -> Blocks.TORCH)
            .addLayer(() -> RenderType::cutout)
            .item((block, props) -> new StandingAndWallBlockItem(block, Content.LIVING_TORCH_BLOCK_WALL.get(), props, Direction.DOWN))
            .transform(conditionalTab(CreativeModeTabs.FUNCTIONAL_BLOCKS, Services.CONFIGS.server()::enableDecoration))
            .build()
            .register();

    public static final BlockEntry<LanternBlock> LIVING_LANTERN = REGISTRATE.object("living_lantern")
            .block(LanternBlock::new)
            .initialProperties(() -> Blocks.LANTERN)
            .addLayer(() -> RenderType::cutout)
            .item()
            .transform(conditionalTab(CreativeModeTabs.FUNCTIONAL_BLOCKS, Services.CONFIGS.server()::enableDecoration))
            .build()
            .register();

    public static final ItemEntry<Item> LIVING_FLAME = REGISTRATE.object("living_flame")
            .item(Item::new)
            .properties(it -> it.rarity(Rarity.UNCOMMON))
            .tab(CreativeModeTabs.INGREDIENTS)
            .register();

    public static final ItemEntry<Item> ASH = REGISTRATE.object("ash")
            .item(Item::new)
            .transform(conditionalTab(CreativeModeTabs.INGREDIENTS, () -> !Services.PLATFORM.isModLoaded("nether_extension") && !Services.PLATFORM.isModLoaded("supplementaries")))
            .register();

    public static final ItemEntry<Item> WARPED_NETHERWART = REGISTRATE.object("warped_nether_wart")
            .item(Item::new)
            .tab(CreativeModeTabs.INGREDIENTS)
            .transform(conditionalTab(CreativeModeTabs.INGREDIENTS, () -> !Services.PLATFORM.isModLoaded("nether_extension")))
            .register();

    public static final BlockEntry<SpawnPowder> SPAWN_POWDER = REGISTRATE.object("spawn_powder")
            .block(SpawnPowder::new)
            .properties(it -> it
                    .noCollission()
                    .instabreak()
                    .lightLevel($ -> 1)
                    .sound(SoundType.SOUL_SAND)
            )
            .addLayer(() -> RenderType::cutout)
            .item()
            .transform(conditionalTab(CreativeModeTabs.FUNCTIONAL_BLOCKS, Services.CONFIGS.server()::enableSpawnPowder))
            .build()
            .register();

    public static final ConstructBrazierTrigger CONSTRUCT_BRAZIER = CriteriaTriggers.register(new ConstructBrazierTrigger());

    public static final EntityEntry<Crazed> CRAZED = REGISTRATE.object("crazed")
            .entity(Crazed::new, MobCategory.MONSTER)
            .attributes(Crazed::createAttributes)
            .renderer(() -> CrazedRender::new)
            .transform(Services.PLATFORM::fireImmune)
            .transform(Services.PLATFORM.sized(EntityDimensions.fixed(2F, 0.5F)))
            .register();

    public static final ItemEntry<LazySpawnEgg<Crazed>> CRAZED_SPAWN_EGG = REGISTRATE.object("crazed_spawn_egg")
            .item(props -> new LazySpawnEgg<>(props, CRAZED, 0x9804699, 0x89CB07))
            .register();

    public static final EntityEntry<CrazedFlame> CRAZED_FLAME = REGISTRATE.object("crazed_flame")
            .entity((EntityType.EntityFactory<CrazedFlame>) CrazedFlame::new, MobCategory.MISC)
            .renderer(() -> CrazedFlameRenderer::new)
            .transform(Services.PLATFORM::fireImmune)
            .transform(Services.PLATFORM.sized(EntityDimensions.fixed(0.6F, 0.6F)))
            .register();

    public static final ItemEntry<Item> ICON = REGISTRATE.object("icon")
            .item(Item::new)
            .register();

    public static final RegistryEntry<LootItemConditionType> CONFIG_CONDITION = REGISTRATE.object("config")
            .generic(Registries.LOOT_CONDITION_TYPE, () -> new LootItemConditionType(new ConfigLootCondition.Serializer()))
            .register();

    public static final RegistryEntry<LootItemConditionType> MOD_CONDITION = REGISTRATE.object("mod_loaded")
            .generic(Registries.LOOT_CONDITION_TYPE, () -> new LootItemConditionType(new ModLootCondition.Serializer()))
            .register();

    public static void init() {
        // Load this class
    }

    private static <T extends Item, P> NonNullFunction<ItemBuilder<T, P>, ItemBuilder<T, P>> conditionalTab(ResourceKey<CreativeModeTab> tab, BooleanSupplier test) {
        return it -> it.tab(tab, mod -> {
            if (test.getAsBoolean()) mod.accept(it.getEntry());
        });
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> candidate, Supplier<BlockEntityType<E>> desired, BlockEntityTicker<? super E> ticker) {
        return desired.get() == candidate ? (BlockEntityTicker<A>) ticker : null;
    }

}