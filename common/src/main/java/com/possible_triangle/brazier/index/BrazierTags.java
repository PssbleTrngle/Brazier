package com.possible_triangle.brazier.index;

import static com.possible_triangle.brazier.BrazierConstants.createId;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class BrazierTags {
    public static final TagKey<Block> BRAZIER_BASE_BLOCKS = TagKey.create(Registries.BLOCK, createId("brazier_base_blocks"));
    public static final TagKey<Block> BRAZIER_STRIPE_BLOCKS = TagKey.create(Registries.BLOCK, createId("brazier_stripe_blocks"));
    public static final TagKey<EntityType<?>> BRAZIER_WHITELIST = TagKey.create(Registries.ENTITY_TYPE, createId("brazier_whitelist"));
    public static final TagKey<EntityType<?>> BRAZIER_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, createId("brazier_blacklist"));
    public static final TagKey<Item> TORCHES = TagKey.create(Registries.ITEM, createId("torches"));
    public static final TagKey<Item> ASH_TAG = TagKey.create(Registries.ITEM, createId("ash"));
    public static final TagKey<Item> RANGE_INDICATOR = TagKey.create(Registries.ITEM, createId("range_indicator"));
    public static final TagKey<Item> WARPED_WART_TAG = TagKey.create(Registries.ITEM, createId("warped_wart"));
}
