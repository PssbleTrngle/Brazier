package com.possible_triangle.brazier;

import com.possible_triangle.brazier.block.BrazierBlock;
import com.possible_triangle.brazier.block.LazyTorchBlock;
import com.possible_triangle.brazier.block.LazyWallTorchBlock;
import com.possible_triangle.brazier.block.SpawnPowder;
import com.possible_triangle.brazier.block.tile.BrazierTile;
import com.possible_triangle.brazier.block.tile.render.BrazierRenderer;
import com.possible_triangle.brazier.entity.Crazed;
import com.possible_triangle.brazier.entity.CrazedFlame;
import com.possible_triangle.brazier.entity.EntityUtil;
import com.possible_triangle.brazier.entity.render.CrazedFlameRenderer;
import com.possible_triangle.brazier.entity.render.CrazedRender;
import com.possible_triangle.brazier.item.HiddenItem;
import com.possible_triangle.brazier.item.LazySpawnEgg;
import com.possible_triangle.brazier.item.LivingTorch;
import com.possible_triangle.brazier.particle.FlameParticle;
import com.possible_triangle.brazier.particle.ModdedParticleType;
import com.possible_triangle.brazier.particle.ParticleRegistry;
import me.shedaniel.architectury.hooks.TagHooks;
import me.shedaniel.architectury.platform.Platform;
import me.shedaniel.architectury.registry.BlockEntityRenderers;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import me.shedaniel.architectury.registry.RenderTypes;
import me.shedaniel.architectury.registry.entity.EntityRenderers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Lantern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.awt.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.possible_triangle.brazier.Brazier.MOD_ID;

public class Content {

    public static final Tag.Named<Block> BRAZIER_BASE_BLOCKS = TagHooks.getBlockOptional(new ResourceLocation(MOD_ID, "brazier_base_blocks"));

    public static final Tag.Named<EntityType<?>> BRAZIER_WHITELIST = TagHooks.getEntityTypeOptional(new ResourceLocation(MOD_ID, "brazier_whitelist"));
    public static final Tag.Named<EntityType<?>> BRAZIER_BLACKLIST = TagHooks.getEntityTypeOptional(new ResourceLocation(MOD_ID, "brazier_blacklist"));
    public static final Tag.Named<Item> TORCHES = TagHooks.getItemOptional(new ResourceLocation(MOD_ID, "torches"));
    public static final Tag.Named<Item> ASH_TAG = TagHooks.getItemOptional(new ResourceLocation(MOD_ID, "ash"));
    public static final Tag.Named<Item> IRON_NUGGET_TAG = TagHooks.getItemOptional(new ResourceLocation(MOD_ID, "iron_nuggets"));
    public static final Tag.Named<Item> WARPED_WART_TAG = TagHooks.getItemOptional(new ResourceLocation(MOD_ID, "warped_wart"));

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_REGISTRY);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD_ID, Registry.BLOCK_REGISTRY);
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(MOD_ID, Registry.ENTITY_TYPE_REGISTRY);
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(MOD_ID, Registry.PARTICLE_TYPE_REGISTRY);

    public static final RegistrySupplier<SimpleParticleType> FLAME_PARTICLE = PARTICLES.register("flame", () -> new ModdedParticleType(false));

    public static final RegistrySupplier<BrazierBlock> BRAZIER = registerBlock("brazier", BrazierBlock::new, p -> p.tab(CreativeModeTab.TAB_MISC));
    public static final RegistrySupplier<BlockEntityType<BrazierTile>> BRAZIER_TILE = TILES.register("brazier", () ->
            BlockEntityType.Builder.of(BrazierTile::new, BRAZIER.get()).build(null)
    );

    public static final RegistrySupplier<LazyTorchBlock> LIVING_TORCH_BLOCK = BLOCKS.register("living_torch", () -> new LazyTorchBlock(FLAME_PARTICLE));
    public static final RegistrySupplier<LazyWallTorchBlock> LIVING_TORCH_BLOCK_WALL = BLOCKS.register("living_wall_torch", () -> new LazyWallTorchBlock(FLAME_PARTICLE));
    public static final RegistrySupplier<Block> LIVING_LANTERN = registerBlock("living_lantern", () -> new Lantern(Block.Properties.copy(Blocks.LANTERN)), p -> p.tab(CreativeModeTab.TAB_DECORATIONS));


    public static final RegistrySupplier<Item> LIVING_FLAME = ITEMS.register("living_flame", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_BREWING).rarity(Rarity.UNCOMMON)));
    public static final RegistrySupplier<LivingTorch> LIVING_TORCH = ITEMS.register("living_torch", LivingTorch::new);

    public static final RegistrySupplier<Item> ASH = ITEMS.register("ash", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistrySupplier<Item> WARPED_NETHERWART = ITEMS.register("warped_nether_wart", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistrySupplier<Block> SPAWN_POWDER = registerBlock("spawn_powder", SpawnPowder::new, p -> p.tab(CreativeModeTab.TAB_MATERIALS));

    public static final RegistrySupplier<EntityType<Crazed>> CRAZED = ENTITIES.register("crazed",
            () -> EntityUtil.<Crazed>buildType(MobCategory.MONSTER, Crazed::new)
                    .size(2F, 0.5F)
                    .fireImmune()
                    .build("crazed")
    );

    public static final RegistrySupplier<LazySpawnEgg> CRAZED_SPAWN_EGG = ITEMS.register("crazed_spawn_egg", () -> new LazySpawnEgg(
            (RegistrySupplier) CRAZED,
            new Color(9804699).getRGB(),
            new Color(0x89CB07).getRGB())
    );

    public static final RegistrySupplier<EntityType<CrazedFlame>> CRAZED_FLAME = ENTITIES.register("crazed_flame", () ->
            EntityUtil.<CrazedFlame>buildType(MobCategory.MISC, CrazedFlame::new)
                    .size(0.6F, 0.6F)
                    .fireImmune()
                    .clientHandler(CrazedFlame::new)
                    .build("crazed_flame")
    );

    public static final RegistrySupplier<HiddenItem> ICON = ITEMS.register("icon", HiddenItem::new);

    public static <T extends Block> RegistrySupplier<T> registerBlock(String name, Supplier<T> supplier, Function<Item.Properties, Item.Properties> props) {
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
        Content.CRAZED.ifPresent(Crazed::init);
        //EntitySpawnPlacementRegistry.register(type, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, Crazed::canSpawnHere);

        Conditional.when(config -> config.DECORATION, LIVING_LANTERN, LIVING_TORCH);
        Conditional.when(config -> config.SPAWN_POWDER, SPAWN_POWDER);

        Conditional.when(config -> config.JUNGLE_LOOT).loot(BuiltInLootTables.JUNGLE_TEMPLE, "flame_jungle_temple");

        Conditional.when($ -> !Platform.isModLoaded("nether_extension"))
                .add(Content.ASH, Content.WARPED_NETHERWART)
                .loot(EntityType.WITHER_SKELETON.getDefaultLootTable(), "wither_ash")
                .loot(Blocks.NETHER_WART.getLootTable(), "warped_wart");
    }

    @Environment(EnvType.CLIENT)
    public static void registerParticles() {
        FLAME_PARTICLE.ifPresent(type -> ParticleRegistry.registerFactory(type, FlameParticle::new));
    }

    @Environment(EnvType.CLIENT)
    public static void clientSetup() {
        CRAZED.ifPresent(type -> EntityRenderers.register(type, CrazedRender::new));
        CRAZED_FLAME.ifPresent(type -> EntityRenderers.register(type, CrazedFlameRenderer::new));

        Stream.of(BRAZIER, LIVING_TORCH_BLOCK, LIVING_TORCH_BLOCK_WALL, SPAWN_POWDER, LIVING_LANTERN)
                .filter(RegistrySupplier::isPresent)
                .map(RegistrySupplier::get)
                .map(b -> (Block) b)
                .forEach(block -> RenderTypes.register(RenderType.cutout(), block));

        BRAZIER_TILE.ifPresent(tile -> BlockEntityRenderers.registerRenderer(tile, BrazierRenderer::new));
    }
}