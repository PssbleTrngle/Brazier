package com.possible_triangle.brazier.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;
import java.util.function.Supplier;

public class LazySpawnEgg extends Item {

    private final Supplier<EntityType<?>> type;
    private final int primaryColor;
    private final int secondaryColor;

    public LazySpawnEgg(Supplier<EntityType<? extends Entity>> type, int primaryColor, int secondaryColor) {
        super((new Properties()).group(ItemGroup.MISC));
        this.type = type;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        if (context.getWorld().isRemote()) {
            return ActionResultType.SUCCESS;
        } else {
            ServerWorld world = (ServerWorld) context.getWorld();
            ItemStack itemstack = context.getItem();
            BlockPos pos = context.getPos();
            Direction direction = context.getFace();
            BlockState blockstate = world.getBlockState(pos);
            if (blockstate.isIn(Blocks.SPAWNER)) {
                TileEntity tileentity = world.getTileEntity(pos);
                if (tileentity instanceof MobSpawnerTileEntity) {
                    AbstractSpawner abstractspawner = ((MobSpawnerTileEntity)tileentity).getSpawnerBaseLogic();
                    abstractspawner.setEntityType(type.get());
                    tileentity.markDirty();
                    world.notifyBlockUpdate(pos, blockstate, blockstate, 3);
                    itemstack.shrink(1);
                    return ActionResultType.CONSUME;
                }
            }

            BlockPos spawnPos = blockstate.getCollisionShape(world, pos).isEmpty()
                    ? pos
                    : pos.offset(direction);

            if (type.get().spawn(world, itemstack, context.getPlayer(), spawnPos, SpawnReason.SPAWN_EGG, true, !Objects.equals(pos, spawnPos) && direction == Direction.UP) != null) {
                itemstack.shrink(1);
            }

            return ActionResultType.CONSUME;
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
