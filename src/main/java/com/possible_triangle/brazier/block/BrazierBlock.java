package com.possible_triangle.brazier.block;

import com.google.common.collect.Lists;
import com.possible_triangle.brazier.block.tile.BrazierTile;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
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

    private static final Collection<ResourceLocation> BLACKLIST = Lists.newArrayList();
    private static final Collection<ResourceLocation> WHITELIST = Lists.newArrayList();

    public static boolean prevents(Entity entity) {
        ResourceLocation r = entity.getType().getRegistryName();
        return (entity instanceof MonsterEntity && entity.isNonBoss() && !WHITELIST.contains(r)) || BLACKLIST.contains(r);
    }

    public static boolean prevents(SpawnReason reason) {
        switch (reason) {
            case CHUNK_GENERATION:
            case MOB_SUMMONED:
            case NATURAL:
            case PATROL:
            case STRUCTURE:
                return true;
            default:
                return false;
        }
    }

    @SubscribeEvent
    public static void mobSpawn(LivingSpawnEvent.CheckSpawn event) {
        BlockPos pos = new BlockPos(event.getX(), event.getY(), event.getZ());
        if (prevents(event.getEntity()) && prevents(event.getSpawnReason()) && BrazierTile.inRange(pos))
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

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entity) {
        if (!entity.func_230279_az_() && state.get(LIT) && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entity)) {
            entity.attackEntityFrom(DamageSource.IN_FIRE, 2F);
        }
        super.onEntityCollision(state, worldIn, pos, entity);
    }

}
