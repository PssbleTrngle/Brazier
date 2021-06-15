package com.possible_triangle.brazier.block;

import com.possible_triangle.brazier.Brazier;
import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.block.tile.BrazierTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BrazierBlock extends BaseEntityBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private static final VoxelShape SHAPE = box(0, 0, 0, 16, 4, 16);

    public BrazierBlock() {
        super(Properties.of(Material.METAL)
                .strength(3.0F)
                .requiresCorrectToolForDrops()
                .noOcclusion()
                .lightLevel(s -> s.getValue(LIT) ? 15 : 0));
        registerDefaultState(super.defaultBlockState().setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
    }

    private static boolean prevents(Entity entity) {
        EntityType<?> type = entity.getType();
        return (
                entity instanceof Monster
                        && !type.is(Content.BRAZIER_WHITELIST)
        ) || type.is(Content.BRAZIER_BLACKLIST);
    }

    private static boolean prevents(MobSpawnType reason) {
        switch (reason) {
            case CHUNK_GENERATION:
            case NATURAL:
            case PATROL:
                return true;
            default:
                return false;
        }
    }

    public static boolean prevents(Entity entity, LevelAccessor world, MobSpawnType reason) {
        BlockPos pos = entity.blockPosition();

        // Check for spawn powder
        if (Brazier.SERVER_CONFIG.get().SPAWN_POWDER) {
            Block block = world.getBlockState(pos).getBlock();
            if (Content.SPAWN_POWDER.toOptional().filter(block::equals).isPresent()) {
                return false;
            }
        }

        return prevents(reason) && prevents(entity) && BrazierTile.inRange(pos);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockGetter world) {
        return new BrazierTile();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return Content.LIVING_TORCH.toOptional().filter(torch -> {
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.isEmpty() && stack.getItem().is(Content.TORCHES)) {
                if (!player.isCreative()) stack.shrink(1);
                player.addItem(new ItemStack(torch, 1));
                return true;
            }
            return false;
        }).map($ -> InteractionResult.SUCCESS).orElse(InteractionResult.PASS);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (!entity.fireImmune() && state.getValue(LIT) && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entity)) {
            entity.hurt(DamageSource.IN_FIRE, 2F);
        }
        super.entityInside(state, world, pos, entity);
    }

}
