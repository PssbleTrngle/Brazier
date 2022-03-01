package com.possible_triangle.brazier.item;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Objects;

public class LazySpawnEgg extends Item {

    private final int primary;
    private final int secondary;
    private final RegistrySupplier<EntityType<?>> type;

    public LazySpawnEgg(RegistrySupplier<EntityType<?>> type, int primary, int secondary) {
        super(new Properties().tab(CreativeModeTab.TAB_MISC));
        this.primary = primary;
        this.secondary = secondary;
        this.type = type;
    }

    public InteractionResult useOn(UseOnContext useOnContext) {
        Level world = useOnContext.getLevel();
        EntityType<?> type = this.type.get();

        if (!(world instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack stack = useOnContext.getItemInHand();
            BlockPos pos = useOnContext.getClickedPos();
            Direction direction = useOnContext.getClickedFace();
            BlockState state = world.getBlockState(pos);
            if (state.is(Blocks.SPAWNER)) {
                BlockEntity tile = world.getBlockEntity(pos);
                if (tile instanceof SpawnerBlockEntity) {
                    BaseSpawner baseSpawner = ((SpawnerBlockEntity) tile).getSpawner();
                    baseSpawner.setEntityId(type);
                    tile.setChanged();
                    world.sendBlockUpdated(pos, state, state, 3);
                    stack.shrink(1);
                    return InteractionResult.CONSUME;
                }
            }

            BlockPos spawnAt = state.getCollisionShape(world, pos).isEmpty() ? pos : pos.relative(direction);

            if (type.spawn((ServerLevel) world, stack, useOnContext.getPlayer(), spawnAt, MobSpawnType.SPAWN_EGG, true, !Objects.equals(pos, spawnAt) && direction == Direction.UP) != null) {
                stack.shrink(1);
            }

            return InteractionResult.CONSUME;
        }
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        EntityType<?> type = this.type.get();
        ItemStack itemStack = player.getItemInHand(interactionHand);
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemStack);
        } else if (!(level instanceof ServerLevel)) {
            return InteractionResultHolder.success(itemStack);
        } else {
            BlockPos blockPos = hitResult.getBlockPos();
            if (!(level.getBlockState(blockPos).getBlock() instanceof LiquidBlock)) {
                return InteractionResultHolder.pass(itemStack);
            } else if (level.mayInteract(player, blockPos) && player.mayUseItemAt(blockPos, hitResult.getDirection(), itemStack)) {

                if (type.spawn((ServerLevel) level, itemStack, player, blockPos, MobSpawnType.SPAWN_EGG, false, false) == null) {
                    return InteractionResultHolder.pass(itemStack);
                } else {
                    if (!player.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.consume(itemStack);
                }
            } else {
                return InteractionResultHolder.fail(itemStack);
            }
        }
    }

    public static int getColor(ItemStack stack, int index) {
        var item = stack.getItem();
        if (item instanceof LazySpawnEgg egg) {
            return index == 0 ? egg.primary : egg.secondary;
        }
        return -1;
    }

}
