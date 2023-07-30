package com.possible_triangle.brazier;

import com.possible_triangle.brazier.block.BrazierBlock;
import com.possible_triangle.brazier.block.LazyTorchBlock;
import com.possible_triangle.brazier.block.LazyWallTorchBlock;
import com.possible_triangle.brazier.block.SpawnPowder;
import com.possible_triangle.brazier.block.tile.BrazierTile;
import com.possible_triangle.brazier.config.IServerConfig;
import com.possible_triangle.brazier.entity.Crazed;
import com.possible_triangle.brazier.entity.CrazedFlame;
import com.possible_triangle.brazier.item.HiddenItem;
import com.possible_triangle.brazier.item.LazySpawnEgg;
import com.possible_triangle.brazier.item.LivingTorch;
import com.possible_triangle.brazier.logic.ConstructBrazierTrigger;
import com.possible_triangle.brazier.particle.ModdedParticleType;
import com.possible_triangle.brazier.platform.PlatformRegistries;
import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static com.possible_triangle.brazier.Brazier.MOD_ID;

public class Content {

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

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD_ID, Registries.BLOCK);
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(MOD_ID, Registries.ENTITY_TYPE);
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(MOD_ID, Registries.PARTICLE_TYPE);

    public static final RegistrySupplier<SimpleParticleType> FLAME_PARTICLE = PARTICLES.register("flame", () -> new ModdedParticleType(false));

    public static final RegistrySupplier<BrazierBlock> BRAZIER = registerBlock("brazier", BrazierBlock::new, p -> p.arch$tab(CreativeModeTabs.FUNCTIONAL_BLOCKS));
    public static final RegistrySupplier<BlockEntityType<BrazierTile>> BRAZIER_TILE = TILES.register("brazier", () ->
            BlockEntityType.Builder.of(BrazierTile::new, BRAZIER.get()).build(null)
    );

    public static final RegistrySupplier<LazyTorchBlock> LIVING_TORCH_BLOCK = BLOCKS.register("living_torch", () -> new LazyTorchBlock(FLAME_PARTICLE));
    public static final RegistrySupplier<LazyWallTorchBlock> LIVING_TORCH_BLOCK_WALL = BLOCKS.register("living_wall_torch", () -> new LazyWallTorchBlock(FLAME_PARTICLE));
    public static final RegistrySupplier<Block> LIVING_LANTERN = registerBlock("living_lantern", () -> new LanternBlock(Block.Properties.copy(Blocks.LANTERN)), p -> p.arch$tab(CreativeModeTabs.FUNCTIONAL_BLOCKS));


    public static final RegistrySupplier<Item> LIVING_FLAME = ITEMS.register("living_flame", () -> new Item(new Item.Properties().arch$tab(CreativeModeTabs.FOOD_AND_DRINKS).rarity(Rarity.UNCOMMON)));
    public static final RegistrySupplier<LivingTorch> LIVING_TORCH = ITEMS.register("living_torch", LivingTorch::new);

    public static final RegistrySupplier<Item> ASH = ITEMS.register("ash", () -> new Item(new Item.Properties().arch$tab(CreativeModeTabs.INGREDIENTS)));
    public static final RegistrySupplier<Item> WARPED_NETHERWART = ITEMS.register("warped_nether_wart", () -> new Item(new Item.Properties().arch$tab(CreativeModeTabs.INGREDIENTS)));
    public static final RegistrySupplier<Block> SPAWN_POWDER = registerBlock("spawn_powder", SpawnPowder::new, p -> p.arch$tab(CreativeModeTabs.INGREDIENTS));

    public static final Supplier<ConstructBrazierTrigger> CONSTRUCT_BRAZIER = PlatformRegistries.createCriteria(new ConstructBrazierTrigger());

    public static final RegistrySupplier<EntityType<Crazed>> CRAZED = ENTITIES.register("crazed",
            () -> PlatformRegistries.<Crazed>createMob(MobCategory.MONSTER, Crazed::new)
                    .size(2F, 0.5F)
                    .fireImmune()
                    .attributes(Crazed.createAttributes())
                    .build("crazed")
    );

    public static final RegistrySupplier<LazySpawnEgg<Crazed>> CRAZED_SPAWN_EGG = ITEMS.register("crazed_spawn_egg", () -> new LazySpawnEgg<>(
            CRAZED,
            new Color(9804699).getRGB(),
            new Color(0x89CB07).getRGB())
    );

    public static final RegistrySupplier<EntityType<CrazedFlame>> CRAZED_FLAME = ENTITIES.register("crazed_flame", () ->
            PlatformRegistries.<CrazedFlame>createMob(MobCategory.MISC, CrazedFlame::new)
                    .size(0.6F, 0.6F)
                    .fireImmune()
                    .build("crazed_flame")
    );

    public static final RegistrySupplier<HiddenItem> ICON = ITEMS.register("icon", HiddenItem::new);

    public static <T extends Block> RegistrySupplier<T> registerBlock(String name, Supplier<T> supplier, UnaryOperator<Item.Properties> props) {
        RegistrySupplier<T> block = BLOCKS.register(name, supplier);
        ITEMS.register(name, () -> new BlockItem(block.get(), props.apply(new Item.Properties())));
        return block;
    }

    public static void init() {
        PARTICLES.register();
        ENTITIES.register();
        BLOCKS.register();
        ITEMS.register();
        TILES.register();
    }

    public static void setup() {
        Conditional.when(IServerConfig::enableDecoration, LIVING_LANTERN, LIVING_TORCH);
        Conditional.when(IServerConfig::enableSpawnPowder, SPAWN_POWDER);

        Conditional.when(IServerConfig::injectJungleLoot).loot(BuiltInLootTables.JUNGLE_TEMPLE, "flame_jungle_temple");

        Conditional.when($ -> !Platform.isModLoaded("nether_extension") && !Platform.isModLoaded("supplementaries"))
                .add(Content.ASH, Content.WARPED_NETHERWART)
                .loot(EntityType.WITHER_SKELETON.getDefaultLootTable(), "wither_ash")
                .loot(Blocks.NETHER_WART.getLootTable(), "warped_wart");
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> candidate, Supplier<BlockEntityType<E>> desired, BlockEntityTicker<? super E> ticker) {
        return desired.get() == candidate ? (BlockEntityTicker<A>) ticker : null;
    }

}