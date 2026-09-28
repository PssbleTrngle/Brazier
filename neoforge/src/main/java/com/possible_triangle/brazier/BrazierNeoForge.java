package com.possible_triangle.brazier;

import com.mojang.serialization.MapCodec;
import com.possible_triangle.brazier.compat.ponder.BrazierPonders;
import com.possible_triangle.brazier.index.BrazierContent;
import com.possible_triangle.brazier.platform.Services;
import com.possible_triangle.brazier.world.InjectLootModifier;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@Mod(BrazierConstants.MOD_ID)
public class BrazierNeoForge {

    public static final NeoForgeRegistrate REGISTRATE = new NeoForgeRegistrate();

    public static final RegistryEntry<MapCodec<? extends IGlobalLootModifier>, MapCodec<InjectLootModifier>> INJECT_MODIFIER = REGISTRATE
            .object("inject")
            .generic(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, () -> InjectLootModifier.CODEC)
            .register();

    public BrazierNeoForge(IEventBus modBus) {
        modBus.register(this);

        Services.CONFIGS.register();
        BrazierContent.init();

        REGISTRATE.registerEventListeners(modBus);
    }

    @SubscribeEvent
    private void clientSetup(FMLClientSetupEvent event) {
        if (Services.PLATFORM.isModLoaded("ponder")) {
            BrazierPonders.register();
        }
    }

}