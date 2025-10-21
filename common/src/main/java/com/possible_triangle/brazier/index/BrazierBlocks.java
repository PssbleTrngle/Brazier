package com.possible_triangle.brazier.index;

import static com.possible_triangle.brazier.BrazierConstants.createId;
import static com.possible_triangle.brazier.index.BrazierContent.REGISTRATE;
import static com.tterrag.registrate.providers.RegistrateRecipeProvider.has;

import com.possible_triangle.brazier.BrazierConstants;
import com.possible_triangle.brazier.platform.Services;
import com.possible_triangle.brazier.world.block.BrazierBlock;
import com.possible_triangle.brazier.world.block.LazyTorchBlock;
import com.possible_triangle.brazier.world.block.LazyWallTorchBlock;
import com.possible_triangle.brazier.world.block.SpawnPowder;
import com.possible_triangle.brazier.world.block.tile.BrazierBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import java.util.function.Supplier;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

public class BrazierBlocks {

    public static final BlockEntry<BrazierBlock> BRAZIER = REGISTRATE.object("brazier")
            .block(BrazierBlock::new)
            .properties(it -> it
                    .strength(1.5F, 6.0F)
                    .requiresCorrectToolForDrops()
                    .noOcclusion()
                    .lightLevel(s -> s.getValue(BrazierBlock.LIT) ? 15 : 0)
            )
            .addLayer(() -> RenderType::cutout)
            .blockstate(Services.DATAGEN::brazierBlockState)
            .item()
            .tab(CreativeModeTabs.FUNCTIONAL_BLOCKS)
            .recipe((context, provider) ->
                    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, context.get())
                            .pattern("BFB")
                            .pattern("SSS")
                            .define('B', Blocks.IRON_BARS)
                            .define('S', Blocks.BLACKSTONE)
                            .define('F', BrazierItems.LIVING_FLAME.get())
                            .unlockedBy("has_living_flame", has(BrazierItems.LIVING_FLAME))
                            .save(provider)
            )
            .build()
            .register();

    public static final BlockEntityEntry<BrazierBlockEntity> BRAZIER_TILE = REGISTRATE.object("brazier")
            .blockEntity(BrazierBlockEntity::new)
            .validBlock(BRAZIER)
            .register();

    public static final BlockEntry<LazyWallTorchBlock> LIVING_TORCH_BLOCK_WALL = REGISTRATE.object("living_wall_torch")
            .block(props -> new LazyWallTorchBlock(props, BrazierContent.FLAME_PARTICLE))
            .initialProperties(() -> Blocks.WALL_TORCH)
            .addLayer(() -> RenderType::cutout)
            .blockstate(Services.DATAGEN::wallTorchBlockstate)
            .register();

    public static final BlockEntry<LazyTorchBlock> LIVING_TORCH = REGISTRATE.object("living_torch")
            .block(props -> new LazyTorchBlock(props, BrazierContent.FLAME_PARTICLE))
            .initialProperties(() -> Blocks.TORCH)
            .addLayer(() -> RenderType::cutout)
            .blockstate(Services.DATAGEN::torchBlockState)
            .item((block, props) -> new StandingAndWallBlockItem(block, LIVING_TORCH_BLOCK_WALL.get(), props, Direction.DOWN))
            .transform(BrazierContent.conditionalTab(CreativeModeTabs.FUNCTIONAL_BLOCKS, Services.CONFIGS.server()::enableDecoration))
            .model((context, provider) -> provider.blockSprite(context))
            .recipe((context, provider) ->
                    ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, context.get(), 2)
                            .requires(context.get())
                            .requires(Ingredient.of(BrazierTags.TORCHES))
                            .unlockedBy("has_living_torch", has(context.get()))
                            .save(provider, createId("living_torch_duplication"))
            )
            .build()
            .register();

    public static final BlockEntry<LanternBlock> LIVING_LANTERN = REGISTRATE.object("living_lantern")
            .block(LanternBlock::new)
            .initialProperties(() -> Blocks.LANTERN)
            .addLayer(() -> RenderType::cutout)
            .blockstate(Services.DATAGEN::lanternBlockstate)
            .item()
            .transform(BrazierContent.conditionalTab(CreativeModeTabs.FUNCTIONAL_BLOCKS, Services.CONFIGS.server()::enableDecoration))
            .model((context, provider) -> provider.generated(context))
            .recipe((context, provider) ->
                    ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, context.get())
                            .pattern("xxx")
                            .pattern("xtx")
                            .pattern("xxx")
                            .define('x', Items.IRON_NUGGET)
                            .define('t', LIVING_TORCH)
                            .unlockedBy("has_living_torch", has(LIVING_TORCH))
                            .save(provider)
            )
            .build()
            .register();

    public static final BlockEntry<SpawnPowder> SPAWN_POWDER = REGISTRATE.object("spawn_powder")
            .block(SpawnPowder::new)
            .lang("Cursed Ash")
            .properties(it -> it
                    .noCollission()
                    .instabreak()
                    .lightLevel($ -> 1)
                    .sound(SoundType.SOUL_SAND)
            )
            .addLayer(() -> RenderType::cutout)
            .blockstate(Services.DATAGEN::existingModel)
            .item()
            .transform(BrazierContent.conditionalTab(CreativeModeTabs.FUNCTIONAL_BLOCKS, Services.CONFIGS.server()::enableSpawnPowder))
            .recipe((context, provider) -> {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, context.get(), 6)
                        .requires(Ingredient.of(BrazierTags.ASH_TAG), 4)
                        .requires(Items.CHARCOAL)
                        .requires(BrazierTags.WARPED_WART_TAG)
                        .unlockedBy("has_living_flame", has(BrazierItems.LIVING_FLAME))
                        .unlockedBy("has_ash", has(BrazierTags.ASH_TAG))
                        .unlockedBy("has_warped_wart", has(BrazierTags.WARPED_WART_TAG))
                        .save(provider);

                ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, Blocks.WARPED_WART_BLOCK)
                        .pattern("xxx")
                        .pattern("xxx")
                        .pattern("xxx")
                        .define('x', BrazierTags.WARPED_WART_TAG)
                        .unlockedBy("collected_wart", has(BrazierTags.WARPED_WART_TAG))
                        .save(provider, BrazierConstants.createId("warped_warp_block"));
            })
            .build()
            .register();

    @Nullable
    @SuppressWarnings("unchecked")
    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> candidate, Supplier<BlockEntityType<E>> desired, BlockEntityTicker<? super E> ticker) {
        return desired.get() == candidate ? (BlockEntityTicker<A>) ticker : null;
    }

    static void init() {
        // Load this class
    }

}
