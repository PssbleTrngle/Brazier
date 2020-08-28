package com.possible_triangle.brazier.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.internal.$Gson$Preconditions;
import com.possible_triangle.brazier.Content;
import com.possible_triangle.brazier.block.tile.BrazierTile;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Collection;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BrazierBlock extends ContainerBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final Ingredient TORCH_INPUT = Ingredient.fromItems(Items.TORCH, Blocks.field_235339_cQ_);

    public BrazierBlock() {
        super(Properties.create(Material.IRON)
                .hardnessAndResistance(3.0F)
                .notSolid()
                .func_235838_a_(s -> s.get(LIT) ? 15 : 0));
        setDefaultState(super.getDefaultState().with(LIT, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(LIT);
    }

    private static final VoxelShape SHAPE = makeCuboidShape(0, 0, 0, 16, 4, 16);

    public static boolean prevents(Entity entity) {
        EntityType<?> type = entity.getType();
        return (
                entity instanceof MonsterEntity
                        && entity.isNonBoss()
                        && !Content.BRAZIER_WHITELIST.func_230235_a_(type)
        ) || Content.BRAZIER_BLACKLIST.func_230235_a_(type);
    }

    public static boolean prevents(SpawnReason reason) {
        switch (reason) {
            case CHUNK_GENERATION:
            case NATURAL:
            case PATROL:
                return true;
            default:
                return false;
        }
    }

    @SubscribeEvent
    public static void mobSpawn(LivingSpawnEvent.CheckSpawn event) {
        BlockPos pos = new BlockPos(event.getX(), event.getY(), event.getZ());
        if (prevents(event.getSpawnReason()) && prevents(event.getEntity()) && BrazierTile.inRange(pos))
            event.setResult(Event.Result.DENY);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new BrazierTile();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        return Content.LIVING_TORCH.filter(torch -> {
            ItemStack stack = player.getHeldItem(hand);
            if (TORCH_INPUT.test(stack)) {
                if (!player.isCreative()) stack.shrink(1);
                player.addItemStackToInventory(new ItemStack(torch, 1));
                return true;
            }
            return false;
        }).map($ -> ActionResultType.SUCCESS).orElse(ActionResultType.PASS);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
        if (!entity.func_230279_az_() && state.get(LIT) && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entity)) {
            entity.attackEntityFrom(DamageSource.IN_FIRE, 2F);
        }
        super.onEntityCollision(state, worldIn, pos, entity);
    }

}
