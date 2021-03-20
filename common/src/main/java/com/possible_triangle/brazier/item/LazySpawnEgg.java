package com.possible_triangle.brazier.item;

import me.shedaniel.architectury.registry.CreativeTabs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.Objects;
import java.util.Properties;
import java.util.function.Supplier;

public class LazySpawnEgg extends Item {

    private final Supplier<EntityType<?>> type;
    private final int primaryColor;
    private final int secondaryColor;

    public LazySpawnEgg(Supplier<EntityType<? extends Entity>> type, int primaryColor, int secondaryColor) {
        super(new Properties().tab(CreativeModeTab.TAB_MISC));
        this.type = type;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (world.isClientSide()) {
            return InteractionResultHolder.SUCCESS;
        } else {
            ServerLevel serverWorld = (ServerLevel) world;
            ItemStack itemstack = player.getItemInHand(hand);
            BlockPos pos = context.getPos();
            Direction direction = context.getFace();
            BlockState blockstate = world.getBlockState(pos);
            if (blockstate.isIn(Blocks.SPAWNER)) {
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof MobSpawnerTileEntity) {
                    AbstractSpawner abstractspawner = ((MobSpawnerTileEntity) tileentity).getSpawnerBaseLogic();
                    abstractspawner.setEntityType(type.get());
                    tileentity.markDirty();
                    world.notifyBlockUpdate(pos, blockstate, blockstate, 3);
                    itemstack.shrink(1);
                    return InteractionResultHolder.CONSUME;
                }
            }

            BlockPos spawnPos = blockstate.getCollisionShape(world, pos).isEmpty()
                    ? pos
                    : pos.offset(direction);

            if (type.get().spawn(world, itemstack, context.getPlayer(), spawnPos, SpawnReason.SPAWN_EGG, true, !Objects.equals(pos, spawnPos) && direction == Direction.UP) != null) {
                itemstack.shrink(1);
            }

            return InteractionResultHolder.CONSUME;
        }
    }

    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack held = player.getHeldItem(hand);
        BlockRayTraceResult raytrace = rayTrace(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);
        if (raytrace.getType() != RayTraceResult.Type.BLOCK) {
            return ActionResult.resultPass(held);
        } else if (world.isRemote) {
            return ActionResult.resultSuccess(held);
        } else {
            BlockPos blockpos = raytrace.getPos();
            if (!(world.getBlockState(blockpos).getBlock() instanceof FlowingFluidBlock)) {
                return ActionResult.resultPass(held);
            } else if (world.isBlockModifiable(player, blockpos) && player.canPlayerEdit(blockpos, raytrace.getFace(), held)) {
                if (type.get().spawn((ServerWorld) world, held, player, blockpos, SpawnReason.SPAWN_EGG, false, false) == null) {
                    return ActionResult.resultPass(held);
                } else {
                    if (!player.abilities.isCreativeMode) {
                        held.shrink(1);
                    }

                    player.addStat(Stats.ITEM_USED.get(this));
                    return ActionResult.resultConsume(held);
                }
            } else {
                return ActionResult.resultFail(held);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public int getColor(int tintIndex) {
        return tintIndex == 0 ? this.primaryColor : this.secondaryColor;
    }

}
