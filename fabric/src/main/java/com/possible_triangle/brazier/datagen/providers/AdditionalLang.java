package com.possible_triangle.brazier.datagen.providers;

import com.possible_triangle.brazier.index.BrazierTags;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import net.minecraft.tags.TagKey;

public class AdditionalLang {

    public static void generate(RegistrateLangProvider provider) {
        provider.add("description.brazier.brazier-1", "The brazier is a beacon-like multiblock which prevents hostile mob spawns in a certain radius.");
        provider.add("description.brazier.brazier-2", "\nYou can ignite a normal torch on it, creating a living torch.\nWhen holding a living torch, you are able to see the outline of the protected are when getting close to it");
        provider.add("category.brazier.light_on_brazier", "Light on a Brazier");

        add(provider, BrazierTags.ASH_TAG, "Ash");
        add(provider, BrazierTags.TORCHES, "Torches");
        add(provider, BrazierTags.BRAZIER_BASE_BLOCKS, "Brazier Base Blocks");
        add(provider, BrazierTags.BRAZIER_STRIPE_BLOCKS, "Brazier Stripe Blocks");
    }

    private static void add(RegistrateLangProvider provider, TagKey<?> tag, String translation) {
        var key = "tag.%s.%s.%s".formatted(tag.registry().location().getPath(), tag.location().getNamespace(), tag.location().getPath());
        provider.add(key, translation);
    }

}
