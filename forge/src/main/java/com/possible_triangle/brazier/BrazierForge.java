package com.possible_triangle.brazier;

import com.mojang.serialization.Codec;
import com.possible_triangle.brazier.config.SyncConfigMessage;
import com.possible_triangle.brazier.data.InjectLootModifier;
import com.possible_triangle.brazier.world.item.BrazierIndicator;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(Brazier.MOD_ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class BrazierForge {

    public static final ForgeRegistrate REGISTRATE = new ForgeRegistrate();

    private static final RegistryEntry<Codec<InjectLootModifier>> INJECT_MODIFIER = REGISTRATE
            .object("inject")
            .generic(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, () -> InjectLootModifier.CODEC)
            .register();

    private final String protocol = "1";
    private final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
            Brazier.createId("network"),
            () -> protocol, protocol::equals, protocol::equals
    );

    public BrazierForge() {
        Brazier.init();

        REGISTRATE.registerEventListeners(MinecraftForge.EVENT_BUS);

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

}