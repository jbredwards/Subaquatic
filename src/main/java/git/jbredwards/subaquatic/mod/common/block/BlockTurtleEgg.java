/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.subaquatic.mod.common.entity.living.EntityTurtle;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import git.jbredwards.subaquatic.mod.common.item.util.IBlockCluster;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public class BlockTurtleEgg extends Block implements IBlockCluster
{
    @Nonnull public static final PropertyInteger HATCH = PropertyInteger.create("hatch", 0, 2), EGGS = PropertyInteger.create("eggs", 0, 3);
    @Nonnull public static final AxisAlignedBB SINGLE_AABB = new AxisAlignedBB(0.1875, 0, 0.1875, 0.75, 0.4375, 0.75), MULTIPLE_AABB = new AxisAlignedBB(0.0625, 0, 0.0625, 0.9375, 0.4375, 0.9375);

    public BlockTurtleEgg(@Nonnull final Material materialIn) { this(materialIn, materialIn.getMaterialMapColor()); }
    public BlockTurtleEgg(@Nonnull final Material materialIn, @Nonnull final MapColor mapColorIn) {
        super(materialIn, mapColorIn);
        setTickRandomly(true);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HATCH, EGGS);
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return getDefaultState().withProperty(HATCH, (meta >> 2) % 3).withProperty(EGGS, meta & 3);
    }

    @Override
    public int getMetaFromState(@Nonnull final IBlockState state) {
        return state.getValue(HATCH) << 2 | state.getValue(EGGS);
    }

    @Override
    public void onEntityWalk(@Nonnull final World worldIn, @Nonnull final BlockPos pos, @Nonnull final Entity entityIn) {
        if(!entityIn.isSneaking() && worldIn.rand.nextInt(100) == 0) {
            @Nonnull final IBlockState state = worldIn.getBlockState(pos);
            if(state.getBlock() == this) breakEgg(entityIn, worldIn, pos, state);
        }

        super.onEntityWalk(worldIn, pos, entityIn);
    }

    @Override
    public void onFallenUpon(@Nonnull final World worldIn, @Nonnull final BlockPos pos, @Nonnull final Entity entityIn, final float fallDistance) {
        if(!(entityIn instanceof EntityZombie) && worldIn.rand.nextInt(3) == 0) {
            @Nonnull final IBlockState state = worldIn.getBlockState(pos);
            if(state.getBlock() == this) breakEgg(entityIn, worldIn, pos, state);
        }

        super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
    }

    @Override
    public void harvestBlock(@Nonnull final World worldIn, @Nonnull final EntityPlayer player, @Nonnull final BlockPos pos, @Nonnull final IBlockState state, @Nullable final TileEntity te, @Nonnull final ItemStack stack) {
        breakEgg(null, worldIn, pos, state);
        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

    @Override
    public void updateTick(@Nonnull final World worldIn, @Nonnull final BlockPos pos, @Nonnull final IBlockState state, @Nonnull final Random rand) {
        final float sunAngle = worldIn.getCelestialAngle(1);
        if((sunAngle < 0.69f && sunAngle > 0.65f || rand.nextInt(500) == 0) && isSand(worldIn.getBlockState(pos.down()))) hatchEggs(worldIn, pos, state);
    }

    @Override
    public void onBlockAdded(@Nonnull final World worldIn, @Nonnull final BlockPos pos, @Nonnull final IBlockState state) {
        if(isSand(worldIn.getBlockState(pos.down()))) worldIn.playEvent(Constants.WorldEvents.BONEMEAL_PARTICLES, pos, 0);
    }

    @Override
    public boolean isFullCube(@Nonnull final IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(@Nonnull final IBlockState state) {
        return false;
    }

    @Override
    public boolean isSideSolid(@Nonnull final IBlockState state, @Nonnull final IBlockAccess world, @Nonnull final BlockPos pos, @Nonnull final EnumFacing side) {
        return false;
    }

    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(@Nonnull final IBlockAccess worldIn, @Nonnull final IBlockState state, @Nonnull final BlockPos pos, @Nonnull final EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(@Nonnull final IBlockState state, @Nonnull final IBlockAccess source, @Nonnull final BlockPos pos) {
        return state.getValue(EGGS) == 0 ? SINGLE_AABB : MULTIPLE_AABB;
    }

    @Override
    public boolean canClusterWith(@Nonnull final ItemStack stack, @Nonnull final EntityPlayer player, @Nonnull final World world, @Nonnull final BlockPos pos, @Nonnull final EnumFacing side, @Nonnull final IBlockState oldState) {
        return oldState.getValue(EGGS) < 3;
    }

    @Override
    public boolean clusterWith(@Nonnull final ItemBlock itemBlock, @Nonnull final ItemStack stack, @Nonnull final EntityPlayer player, @Nonnull final World world, @Nonnull final BlockPos pos, @Nonnull final EnumFacing side, final float hitX, final float hitY, final float hitZ, @Nonnull final IBlockState oldState) {
        return itemBlock.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, oldState.cycleProperty(EGGS));
    }

    @Override
    public boolean createInitialCluster(@Nonnull final ItemBlock itemBlock, @Nonnull final ItemStack stack, @Nonnull final EntityPlayer player, @Nonnull final World world, @Nonnull final BlockPos pos, @Nonnull final EnumFacing side, final float hitX, final float hitY, final float hitZ, @Nonnull final IBlockState oldState) {
        return itemBlock.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, getDefaultState());
    }

    @Override
    public boolean canPlaceBlockAt(@Nonnull final World worldIn, @Nonnull final BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) || worldIn.getBlockState(pos).getBlock() == this;
    }

    public static void breakEgg(@Nullable final Entity entity, @Nonnull final World world, @Nonnull final BlockPos pos, @Nonnull final IBlockState state) {
        if(!world.isRemote && canBreakEgg(entity)) {
            world.playSound(null, pos, SubaquaticSounds.ENTITY_TURTLE_EGG_BREAK, SoundCategory.BLOCKS, 0.7f, 0.9f + world.rand.nextFloat() * 0.2f);

            final int eggs = state.getValue(EGGS);
            if(eggs == 0) world.destroyBlock(pos, false);

            else {
                world.playEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, pos, getStateId(state));
                world.setBlockState(pos, state.withProperty(EGGS, eggs - 1));
            }
        }
    }

    public static void hatchEggs(@Nonnull final World world, @Nonnull final BlockPos pos, @Nonnull final IBlockState state) {
        if(!world.isRemote) {
            if(state.getValue(HATCH) < 2) {
                world.playSound(null, pos, SubaquaticSounds.ENTITY_TURTLE_EGG_CRACK, SoundCategory.BLOCKS, 0.7f, 0.9f + world.rand.nextFloat() * 0.2f);
                world.setBlockState(pos, state.cycleProperty(HATCH), Constants.BlockFlags.SEND_TO_CLIENTS);
            }

            else {
                world.playSound(null, pos, SubaquaticSounds.ENTITY_TURTLE_EGG_HATCH, SoundCategory.BLOCKS, 0.7f, 0.9f + world.rand.nextFloat() * 0.2f);
                world.setBlockToAir(pos);

                final int eggs = state.getValue(EGGS) + 1;
                for(int egg = 0; egg < eggs; egg++) {
                    world.playEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, pos, getStateId(state));

                    @Nonnull final EntityTurtle turtle = new EntityTurtle(world);
                    turtle.setLocationAndAngles(pos.getX() + 0.3 + egg * 0.2, pos.getY(), pos.getZ() + 0.3, 0, 0);
                    turtle.setGrowingAge(-24000);

                    world.spawnEntity(turtle);
                }
            }
        }
    }

    public static boolean canBreakEgg(@Nullable final Entity entity) {
        if(entity == null) return true;
        else if(entity instanceof EntityTurtle || entity instanceof EntityBat || entity instanceof EntityFlying || entity instanceof net.minecraft.entity.passive.EntityFlying || entity.getEntityBoundingBox().getAverageEdgeLength() < 0.1) return false;
        else return entity instanceof EntityPlayer || (entity instanceof EntityLivingBase || !entity.getRecursivePassengersByType(EntityPlayer.class).isEmpty()) && ForgeEventFactory.getMobGriefingEvent(entity.world, entity);
    }

    public static boolean isSand(@Nonnull final IBlockState state) {
        return state.getMaterial() == Material.SAND && state.isTopSolid() && !state.getBlock().hasTileEntity(state);
    }
}
