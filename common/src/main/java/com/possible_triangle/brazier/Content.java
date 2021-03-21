package com.possible_triangle.brazier;

import com.possible_triangle.brazier.block.BrazierBlock;
import com.possible_triangle.brazier.block.LazyTorchBlock;
import com.possible_triangle.brazier.block.LazyWallTorchBlock;
import com.possible_triangle.brazier.block.SpawnPowder;
import com.possible_triangle.brazier.block.tile.BrazierTile;
import com.possible_triangle.brazier.block.tile.render.BrazierRenderer;
import com.possible_triangle.brazier.entity.Crazed;
import com.possible_triangle.brazier.entity.CrazedFlame;
import com.possible_triangle.brazier.entity.render.CrazedFlameRenderer;
import com.possible_triangle.brazier.entity.render.CrazedRender;
import com.possible_triangle.brazier.item.LivingTorch;
import com.possible_triangle.brazier.particle.FlameParticle;
import com.possible_triangle.brazier.particle.ParticleRegistry;
import me.shedaniel.architectury.hooks.TagHooks;
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
    public static final Tag.Named<Item> WARPED_WART_TAG = TagHooks.getItemOptional(new ResourceLocation(MOD_ID, "warped_wart"));

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_REGISTRY);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD_ID, Registry.BLOCK_REGISTRY);
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(MOD_ID, Registry.ENTITY_TYPE_REGISTRY);
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(MOD_ID, Registry.PARTICLE_TYPE_REGISTRY);

    public static final Supplier<SimpleParticleType> FLAME_PARTICLE = ParticleRegistry.register("flame", FlameParticle::new);

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
            () -> EntityType.Builder.<Crazed>of(Crazed::new, MobCategory.MONSTER).fireImmune().build("crazed")
    );

    @Deprecated
    public static final RegistrySupplier<Item> CRAZED_SPAWN_EGG = ITEMS.register("crazed_spawn_egg", () -> new Item(new Item.Properties()));
    /*
    public static final RegistrySupplier<LazySpawnEgg> CRAZED_SPAWN_EGG = ITEMS.register("crazed_spawn_egg", () -> new LazySpawnEgg(CRAZED::get,
            new Color(9804699).getRGB(),
            new Color(0x89CB07).getRGB())
    );
    */

    public static final RegistrySupplier<EntityType<CrazedFlame>> CRAZED_FLAME = ENTITIES.register("crazed_flame", () -> EntityType.Builder.<CrazedFlame>of(CrazedFlame::new, MobCategory.MISC)
            .sized(0.6F, 0.6F)
            .fireImmune()
            .build("crazed_flame"));

    public static <T extends Block> RegistrySupplier<T> registerBlock(String name, Supplier<T> supplier, Function<Item.Properties, Item.Properties> props) {
        RegistrySupplier<T> block = BLOCKS.register(name, supplier);
        ITEMS.register(name, () -> new BlockItem(block.get(), props.apply(new Item.Properties())));
        return block;
    }

    public static void init() {
        ENTITIES.register();
        ITEMS.register();
        BLOCKS.register();
        TILES.register();
        PARTICLES.register();
    }

    public static void setup() {
        //EntitySpawnPlacementRegistry.register(type, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, Crazed::canSpawnHere);
        Content.CRAZED.ifPresent(Crazed::init);
    }

    //@SubscribeEvent
    //public static void registerParticles(ParticleFactoryRegisterEvent event) {
    //    FLAME_PARTICLE.ifPresent(type -> Minecraft.getInstance().particles.registerFactory(type, FlameParticle.Factory::new));
    //}


    //@Environment(EnvType.CLIENT)
    //@SubscribeEvent
    //public static void itemColors(ItemColors.Item event) {
    //
    //    event.getItemColors().register((s, i) -> {
    //        if (s.getItem() instanceof LazySpawnEgg) {
    //            LazySpawnEgg egg = (LazySpawnEgg) s.getItem();
    //            return egg.getColor(i);
    //        } else return -1;
    //    }, CRAZED_SPAWN_EGG.get());
    //
    //}

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