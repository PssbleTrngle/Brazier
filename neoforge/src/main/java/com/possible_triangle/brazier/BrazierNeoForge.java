package com.possible_triangle.brazier;

import com.mojang.serialization.MapCodec;
import com.possible_triangle.brazier.index.BrazierContent;
import com.possible_triangle.brazier.platform.Services;
import com.possible_triangle.brazier.world.InjectLootModifier;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@Mod(BrazierConstants.MOD_ID)
public class BrazierNeoForge {

    public static final NeoForgeRegistrate REGISTRATE = new NeoForgeRegistrate();

    public static final RegistryEntry<MapCodec<? extends IGlobalLootModifier>, MapCodec<InjectLootModifier>> INJECT_MODIFIER = REGISTRATE
            .object("inject")
            .generic(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, () -> InjectLootModifier.CODEC)
            .register();

    private final String protocol = "1";


    public BrazierNeoForge(IEventBus modBus) {
        Services.CONFIGS.register();
        BrazierContent.init();

        REGISTRATE.registerEventListeners(modBus);
    }

}