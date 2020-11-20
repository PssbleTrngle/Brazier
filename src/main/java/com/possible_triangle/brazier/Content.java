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
import com.possible_triangle.brazier.item.LazySpawnEgg;
import com.possible_triangle.brazier.item.LivingTorch;
import com.possible_triangle.brazier.particle.FlameParticle;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.possible_triangle.brazier.Brazier.MODID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Content {

    public static final ITag<Block> BRAZIER_BASE_BLOCKS = BlockTags.makeWrapperTag(new ResourceLocation(MODID, "brazier_base_blocks").toString());
    public static final ITag<EntityType<?>> BRAZIER_WHITELIST = EntityTypeTags.func_232896_a_(new ResourceLocation(MODID, "brazier_whitelist").toString());
    public static final ITag<EntityType<?>> BRAZIER_BLACKLIST = EntityTypeTags.func_232896_a_(new ResourceLocation(MODID, "brazier_blacklist").toString());
    public static final ITag<Item> TORCHES = ItemTags.makeWrapperTag(new ResourceLocation(MODID, "torches").toString());

    public static final ITag<Item> ASH_TAG = ItemTags.makeWrapperTag(new ResourceLocation(MODID, "ash").toString());
    public static final ITag<Item> WARPED_WART_TAG = ItemTags.makeWrapperTag(new ResourceLocation(MODID, "warped_wart").toString());

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MODID);

    public static final RegistryObject<BasicParticleType> FLAME_PARTICLE = PARTICLES.register("flame", () -> new BasicParticleType(false));

    public static final RegistryObject<BrazierBlock> BRAZIER = registerBlock("brazier", BrazierBlock::new, p -> p.group(ItemGroup.MISC));
    public static final RegistryObject<TileEntityType<BrazierTile>> BRAZIER_TILE = TILES.register("brazier", () ->
            TileEntityType.Builder.create(BrazierTile::new, BRAZIER.get()).build(null)
    );

    public static final RegistryObject<Block> LIVING_TORCH_BLOCK = BLOCKS.register("living_torch", () -> new LazyTorchBlock(FLAME_PARTICLE));
    public static final RegistryObject<Block> LIVING_TORCH_BLOCK_WALL = BLOCKS.register("living_wall_torch", () -> new LazyWallTorchBlock(FLAME_PARTICLE));

    public static final RegistryObject<Item> LIVING_FLAME = ITEMS.register("living_flame", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> LIVING_TORCH = ITEMS.register("living_torch", LivingTorch::new);

    public static final RegistryObject<Item> ASH = ITEMS.register("ash", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Item> WARPED_NETHERWART = ITEMS.register("warped_nether_wart", () -> new Item(new Item.Properties().group(ItemGroup.MATERIALS)));
    public static final RegistryObject<Block> SPAWN_POWDER = registerBlock("spawn_powder", SpawnPowder::new, p -> p.group(ItemGroup.MATERIALS));

    public static final RegistryObject<EntityType<Crazed>> CRAZED = ENTITIES.register("crazed", () -> EntityType.Builder.<Crazed>create(Crazed::new, EntityClassification.MONSTER)
            .setCustomClientFactory((s, w) -> new Crazed(w))
            .immuneToFire().build("crazed"));

    public static final RegistryObject<LazySpawnEgg> CRAZED_SPAWN_EGG = ITEMS.register("crazed_spawn_egg", () -> new LazySpawnEgg(CRAZED::get,
            new Color(9804699).getRGB(),
            new Color(0x89CB07).getRGB())
    );

    public static final RegistryObject<EntityType<CrazedFlame>> CRAZED_FLAME = ENTITIES.register("crazed_flame", () -> EntityType.Builder.<CrazedFlame>create(CrazedFlame::new, EntityClassification.MISC)
            .setCustomClientFactory((s, w) -> new CrazedFlame(w))
            .size(0.6F, 0.6F)
            .setShouldReceiveVelocityUpdates(false)
            .immuneToFire().build("crazed_flame"));

    public static <B extends Block> RegistryObject<B> registerBlock(String name, Supplier<B> supplier, Function<Item.Properties, Item.Properties> props) {
        RegistryObject<B> block = BLOCKS.register(name, supplier);
        ITEMS.register(name, () -> new BlockItem(block.get(), props.apply(new Item.Properties())));
        return block;
    }

    public static void init() {
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        PARTICLES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void setup() {
        Content.CRAZED.ifPresent(type -> {
            Crazed.init(type);
            EntitySpawnPlacementRegistry.register(type, EntitySpawnPlacementRegistry.PlacementType.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, Crazed::canSpawnHere);
        });
    }

    @SubscribeEvent
    public static void registerParticles(ParticleFactoryRegisterEvent event) {
        FLAME_PARTICLE.ifPresent(type -> Minecraft.getInstance().particles.registerFactory(type, FlameParticle.Factory::new));
    }


    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void itemColors(ColorHandlerEvent.Item event) {

        event.getItemColors().register((s, i) -> {
            if (s.getItem() instanceof LazySpawnEgg) {
                LazySpawnEgg egg = (LazySpawnEgg) s.getItem();
                return egg.getColor(i);
            } else return -1;
        }, CRAZED_SPAWN_EGG.get());

    }

    @OnlyIn(Dist.CLIENT)
    public static void clientSetup(Minecraft mc) {
        CRAZED.ifPresent(type -> mc.getRenderManager().register(type, new CrazedRender(mc.getRenderManager())));
        CRAZED_FLAME.ifPresent(type -> mc.getRenderManager().register(type, new CrazedFlameRenderer(mc.getRenderManager())));

        Stream.of(BRAZIER, LIVING_TORCH_BLOCK, LIVING_TORCH_BLOCK_WALL)
                .filter(RegistryObject::isPresent)
                .map(RegistryObject::get)
                .forEach(b -> RenderTypeLookup.setRenderLayer(b, RenderType.getCutout()));

        BRAZIER_TILE.ifPresent(tile -> ClientRegistry.bindTileEntityRenderer(tile, BrazierRenderer::new));

        SPAWN_POWDER.ifPresent(b -> RenderTypeLookup.setRenderLayer(b, RenderType.getCutout()));
    }
}