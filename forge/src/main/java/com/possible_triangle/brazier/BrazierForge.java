package com.possible_triangle.brazier;

import com.possible_triangle.brazier.config.SyncConfigMessage;
import com.possible_triangle.brazier.data.InjectLootModifier;
import com.possible_triangle.brazier.world.item.BrazierIndicator;
import com.possible_triangle.brazier.world.item.LazySpawnEgg;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod(Brazier.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrazierForge {

    public static final NonNullSupplier<Registrate> REGISTRATE = NonNullSupplier.lazy(() -> Registrate.create(Brazier.MOD_ID));

    private final String protocol = "1";
    private final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Brazier.MOD_ID, "network"),
            () -> protocol, protocol::equals, protocol::equals
    );

    public BrazierForge() {
        Brazier.init();

        MinecraftForge.EVENT_BUS.addListener((TickEvent.PlayerTickEvent event) -> BrazierIndicator.playerTick(event.player));

        setupNetwork();
    }

    private void setupNetwork() {
        NETWORK.registerMessage(0, SyncConfigMessage.class, SyncConfigMessage::encode, SyncConfigMessage::decode, (packet, contextSupplier) -> {
            var context = contextSupplier.get();
            context.enqueueWork(packet::handle);
            context.setPacketHandled(true);
        });

        MinecraftForge.EVENT_BUS.addListener((PlayerEvent.PlayerLoggedInEvent event) -> {
            if (event.getEntity() instanceof ServerPlayer player) {
                NETWORK.send(PacketDistributor.PLAYER.with(() -> player), SyncConfigMessage.create());
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void itemColors(RegisterColorHandlersEvent.Item event) {
        Content.CRAZED_SPAWN_EGG.ifPresent(egg ->
                event.getItemColors().register(LazySpawnEgg::getColor, egg)
        );
    }

    @SubscribeEvent
    public static void setup(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.ITEM) {
            Brazier.setup();
        }
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        Content.FLAME_PARTICLE.ifPresent(type -> event.registerSpriteSet(type, FlameParticle.Provider::new));
    }

    @SubscribeEvent
    public static void doClientStuff(FMLClientSetupEvent event) {
        Brazier.clientSetup();
    }

    @SubscribeEvent
    public static void onRegisterLootModifiers(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, new ResourceLocation(Brazier.MOD_ID, "inject"), () -> InjectLootModifier.CODEC);
    }

}