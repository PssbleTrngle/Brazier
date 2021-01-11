package com.possible_triangle.brazier;

import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.Registries;
import me.shedaniel.architectury.registry.RegistrySupplier;
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
import com.possible_triangle.brazier.item.LazySpawnEgg;
import com.possible_triangle.brazier.item.LivingTorch;
import com.possible_triangle.brazier.particle.FlameParticle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.awt.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.possible_triangle.brazier.Brazier.MODID;

public class Content {

    public static final Tag<Block> BRAZIER_BASE_BLOCKS = BlockTags.makeWrapperTag(new ResourceLocation(MODID, "brazier_base_blocks").toString());
    public static final Tag<EntityType<?>> BRAZIER_WHITELIST = EntityTypeTags.getTagById(new ResourceLocation(MODID, "brazier_whitelist").toString());
    public static final Tag<EntityType<?>> BRAZIER_BLACKLIST = EntityTypeTags.getTagById(new ResourceLocation(MODID, "brazier_blacklist").toString());
    public static final Tag<Item> TORCHES = ItemTags.makeWrapperTag(new ResourceLocation(MODID, "torches").toString());

    public static final Tag<Item> ASH_TAG = ItemTags.makeWrapperTag(new ResourceLocation(MODID, "ash").toString());
    public static final Tag<Item> WARPED_WART_TAG = ItemTags.makeWrapperTag(new ResourceLocation(MODID, "warped_wart").toString());

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MODID, Registry.ITEM_REGISTRY);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MODID, Registry.BLOCK_REGISTRY);
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(MODID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(MODID, Registry.ENTITY_TYPE_REGISTRY);
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(MODID, Registry.PARTICLE_TYPE_REGISTRY);

    public static final RegistrySupplier<SimpleParticleType> FLAME_PARTICLE = PARTICLES.register("flame", () -> new SimpleParticleType(false));

    public static final RegistrySupplier<BrazierBlock> BRAZIER = registerBlock("brazier", BrazierBlock::new, p -> p.group(CreativeModeTab.TAB_MISC));
    public static final RegistrySupplier<BlockEntityType<BrazierTile>> BRAZIER_TILE = TILES.register("brazier", () ->
            BlockEntityType.Builder.of(BrazierTile::new, BRAZIER.get()).build(null)
    );

    public static final RegistrySupplier<Block> LIVING_TORCH_BLOCK = BLOCKS.register("living_torch", () -> new LazyTorchBlock(FLAME_PARTICLE));
    public static final RegistrySupplier<Block> LIVING_TORCH_BLOCK_WALL = BLOCKS.register("living_wall_torch", () -> new LazyWallTorchBlock(FLAME_PARTICLE));

    public static final RegistrySupplier<Item> LIVING_FLAME = ITEMS.register("living_flame", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_BREWING).rarity(Rarity.UNCOMMON)));
    public static final RegistrySupplier<Item> LIVING_TORCH = ITEMS.register("living_torch", LivingTorch::new);

    public static final RegistrySupplier<Item> ASH = ITEMS.register("ash", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistrySupplier<Item> WARPED_NETHERWART = ITEMS.register("warped_nether_wart", () -> new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
    public static final RegistrySupplier<Block> SPAWN_POWDER = registerBlock("spawn_powder", SpawnPowder::new, p -> p.tab(CreativeModeTab.TAB_MATERIALS));

    public static final RegistrySupplier<EntityType<?>> CRAZED = ENTITIES.register("crazed",
            () -> EntityType.Builder.<Crazed>of(Crazed::new, MobCategory.MONSTER).fireImmune().build("crazed")
    );

    /*
    public static final RegistrySupplier<LazySpawnEgg> CRAZED_SPAWN_EGG = ITEMS.register("crazed_spawn_egg", () -> new LazySpawnEgg(CRAZED::get,
            new Color(9804699).getRGB(),
            new Color(0x89CB07).getRGB())
    );
    */

    public static final RegistrySupplier<EntityType<?>> CRAZED_FLAME = ENTITIES.register("crazed_flame", () -> EntityType.Builder.<CrazedFlame>of(CrazedFlame::new, MobCategory.MISC)
            .sized(0.6F, 0.6F)
            .fireImmune()
            .build("crazed_flame"));

    public static RegistrySupplier<Block> registerBlock(String name, Supplier<Block> supplier, Function<Item.Properties, Item.Properties> props) {
        RegistrySupplier<Block> block = BLOCKS.register(name, supplier);
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

    @SubscribeEvent
    public static void registerParticles(ParticleFactoryRegisterEvent event) {
        FLAME_PARTICLE.ifPresent(type -> Minecraft.getInstance().particles.registerFactory(type, FlameParticle.Factory::new));
    }


    @Environment(EnvType.CLIENT)
    @SubscribeEvent
    public static void itemColors(ColorHandlerEvent.Item event) {

        event.getItemColors().register((s, i) -> {
            if (s.getItem() instanceof LazySpawnEgg) {
                LazySpawnEgg egg = (LazySpawnEgg) s.getItem();
                return egg.getColor(i);
            } else return -1;
        }, CRAZED_SPAWN_EGG.get());

    }

    @Environment(EnvType.CLIENT)
    public static void clientSetup(Minecraft mc) {
        CRAZED.ifPresent(type -> mc.render().register(type, new CrazedRender(mc.getRenderManager())));
        CRAZED_FLAME.ifPresent(type -> mc.getEntityRenderDispatcher().register(type, new CrazedFlameRenderer(mc.getRenderManager())));

        Stream.of(BRAZIER, LIVING_TORCH_BLOCK, LIVING_TORCH_BLOCK_WALL)
                .filter(RegistrySupplier::isPresent)
                .map(RegistrySupplier::get)
                .forEach(b -> RenderTypeLookup.setRenderLayer(b, RenderType.getCutout()));

        BRAZIER_TILE.ifPresent(tile -> ClientRegistry.bindTileEntityRenderer(tile, BrazierRenderer::new));

        SPAWN_POWDER.ifPresent(b -> RenderTypeLookup.setRenderLayer(b, RenderType.getCutout()));
    }
}