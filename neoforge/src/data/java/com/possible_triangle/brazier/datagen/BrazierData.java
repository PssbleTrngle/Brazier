package com.possible_triangle.brazier.datagen;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

import com.possible_triangle.brazier.BrazierConstants;
import com.possible_triangle.brazier.compat.ponder.BrazierPonders;
import com.possible_triangle.brazier.index.BrazierTags;
import com.possible_triangle.brazier.platform.Services;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber
public class BrazierData {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void setup(GatherDataEvent event) {
        var registrate = Services.PLATFORM.getRegistrate();

        event.createProvider(PackMetadata::new);

        registrate.addDataGenerator(ProviderType.LOOT, provider -> {
            provider.addLootAction(LootContextParamSets.EMPTY, consumer -> LootInjects.generate(consumer, provider.getProvider()));
        });

        BrazierPonders.register();
        PonderIndex.getLangAccess().provideLang(BrazierConstants.MOD_ID, registrate::addRawLang);

        registrate.addDataGenerator(ProviderType.ADVANCEMENT, Advancements::generate);
        registrate.addDataGenerator(ProviderType.LANG, AdditionalLang::generate);

        registrate.addDataGenerator(ProviderType.ITEM_TAGS, BrazierData::itemTags);
        registrate.addDataGenerator(ProviderType.BLOCK_TAGS, BrazierData::blockTags);
        registrate.addDataGenerator(ProviderType.ENTITY_TAGS, BrazierData::entityTags);
    }

    private static void itemTags(RegistrateItemTagsProvider provider) {
        provider.addTag(BrazierTags.ASH)
                .addOptional(fromNamespaceAndPath("supplementaries", "ash"));

        provider.addTag(BrazierTags.TORCHES)
                .add(Items.TORCH)
                .add(Items.SOUL_TORCH)
                .addOptional(fromNamespaceAndPath("endergetic", "ender_torch"));
    }

    private static void blockTags(RegistrateTagsProvider.IntrinsicImpl<Block> provider) {
        provider.addTag(BrazierTags.BRAZIER_BASE_BLOCKS)
                .add(Blocks.NETHERITE_BLOCK)
                .add(Blocks.CRYING_OBSIDIAN)
                .add(Blocks.ANCIENT_DEBRIS)
                .add(Blocks.GILDED_BLACKSTONE)
                .addOptional(fromNamespaceAndPath("caverns_and_chasms", "necromium_block"));

        provider.addTag(BrazierTags.BRAZIER_STRIPE_BLOCKS)
                .addTag(BrazierTags.BRAZIER_BASE_BLOCKS);
    }

    private static void entityTags(RegistrateTagsProvider.IntrinsicImpl<EntityType<?>> provider) {
        provider.addTag(BrazierTags.BRAZIER_BLACKLIST)
                .add(EntityType.SLIME)
                .add(EntityType.MAGMA_CUBE)
                .add(EntityType.HOGLIN);
        
        provider.addTag(BrazierTags.BRAZIER_WHITELIST)
                .add(EntityType.WITHER)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.ELDER_GUARDIAN)
                .add(EntityType.WARDEN);
    }

}
