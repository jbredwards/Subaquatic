/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.fluidlogged_api.api.block.IFluidloggable;
import git.jbredwards.fluidlogged_api.api.util.FluidState;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.mod.common.world.gen.feature.tree.WorldGenMangroveTree;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public class BlockMangroveSapling extends BlockBush implements IFluidloggable, IGrowable
{
    @Nonnull public static final PropertyInteger PROPERTY = PropertyInteger.create("property", 0, 5);
    @Nonnull public static final AxisAlignedBB[] AABB = new AxisAlignedBB[] {
            new AxisAlignedBB(0.4375, 0.8125, 0.4375, 0.5625, 1, 0.5625),
            new AxisAlignedBB(0.4375, 0.625 , 0.4375, 0.5625, 1, 0.5625),
            new AxisAlignedBB(0.4375, 0.4375, 0.4375, 0.5625, 1, 0.5625),
            new AxisAlignedBB(0.4375, 0.1875, 0.4375, 0.5625, 1, 0.5625),
            new AxisAlignedBB(0.4375, 0     , 0.4375, 0.5625, 1, 0.5625)
    };

    public BlockMangroveSapling(@Nonnull final Material materialIn) { this(materialIn, materialIn.getMaterialMapColor()); }
    public BlockMangroveSapling(@Nonnull final Material materialIn, @Nonnull final MapColor mapColorIn) {
        super(materialIn, mapColorIn);
    }

    public static boolean canHangingGrow(@Nonnull final IBlockState state) {
        return getHangingAge(state) != 4;
    }

    public static boolean isHanging(@Nonnull final IBlockState state) {
        return state.getValue(PROPERTY) != 0;
    }

    public static int getHangingAge(@Nonnull final IBlockState state) {
        return state.getValue(PROPERTY) - 1;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockSapling.STAGE, PROPERTY);
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return getDefaultState().withProperty(BlockSapling.STAGE, meta & 1).withProperty(PROPERTY, Math.min(meta >> 1, 5));
    }

    @Override
    public int getMetaFromState(@Nonnull final IBlockState state) {
        return state.getValue(BlockSapling.STAGE) | state.getValue(PROPERTY) << 1;
    }

    @Override
    protected boolean canSustainBush(@Nonnull final IBlockState state) {
        return super.canSustainBush(state) || isEqualTo(state.getBlock(), Blocks.CLAY);
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull final World worldIn, @Nonnull final BlockPos pos, @Nonnull final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, @Nonnull final EntityLivingBase placer) {
        return facing == EnumFacing.DOWN && canPlaceBlockHanging(worldIn, pos) ? getDefaultState().withProperty(PROPERTY, 1) : getDefaultState();
    }

    @Nonnull
    @Override
    public EnumActionResult onFluidFill(@Nonnull final World world, @Nonnull final BlockPos pos, @Nonnull final IBlockState here, @Nonnull final FluidState newFluid, final int blockFlags) {
        if(isHanging(here) && canHangingGrow(here) && newFluid.isSource() && FluidloggedUtils.isCompatibleFluid(newFluid.getFluid(), FluidRegistry.WATER)) {
            world.setBlockState(pos, here.withProperty(PROPERTY, 5), blockFlags);
        }

        return EnumActionResult.PASS;
    }

    @Nonnull
    @Override
    public EnumOffsetType getOffsetType() {
        return EnumOffsetType.XZ;
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(@Nonnull final IBlockState state, @Nonnull final IBlockAccess source, @Nonnull final BlockPos pos) {
        return AABB[isHanging(state) ? getHangingAge(state) : 4].offset(state.getOffset(source, pos));
    }

    @Override
    public boolean canBlockStay(@Nonnull final World worldIn, @Nonnull final BlockPos pos, @Nonnull final IBlockState state) {
        return isHanging(state) ? canPlaceBlockHanging(worldIn, pos) : super.canBlockStay(worldIn, pos, state);
    }

    @Override
    public boolean canPlaceBlockAt(@Nonnull final World worldIn, @Nonnull final BlockPos pos) {
        return canPlaceBlockHanging(worldIn, pos) || super.canPlaceBlockAt(worldIn, pos);
    }

    public boolean canPlaceBlockHanging(@Nonnull final IBlockAccess access, @Nonnull final BlockPos pos) {
        @Nonnull final IBlockState above = access.getBlockState(pos.up());
        return above.getBlock().canSustainPlant(above, access, pos.up(), EnumFacing.DOWN, this);
    }

    @Override
    public void updateTick(@Nonnull final World worldIn, @Nonnull final BlockPos pos, @Nonnull final IBlockState state, @Nonnull final Random rand) {
        if(isHanging(state) || rand.nextInt(7) == 0) grow(worldIn, rand, pos, state);
    }

    @Override
    public boolean canGrow(@Nonnull final World worldIn, @Nonnull final BlockPos pos, @Nonnull final IBlockState state, final boolean isClient) {
        return !isHanging(state) || canHangingGrow(state);
    }

    EntityVillager

    @Override
    public boolean canUseBonemeal(@Nonnull final World worldIn, @Nonnull final Random rand, @Nonnull final BlockPos pos, @Nonnull final IBlockState state) {
        return isHanging(state) ? canHangingGrow(state) : rand.nextFloat() < 0.45;
    }

    @Override
    public void grow(@Nonnull final World worldIn, @Nonnull final Random rand, @Nonnull final BlockPos pos, @Nonnull final IBlockState state) {
        if(isHanging(state)) { if(canHangingGrow(state)) worldIn.setBlockState(pos, state.cycleProperty(PROPERTY), Constants.BlockFlags.SEND_TO_CLIENTS); }
        else if(state.getValue(BlockSapling.STAGE) == 0) worldIn.setBlockState(pos, state.cycleProperty(BlockSapling.STAGE), Constants.BlockFlags.NO_RERENDER);
        else if(TerrainGen.saplingGrowTree(worldIn, rand, pos)) new WorldGenMangroveTree(true).generate(worldIn, rand, pos);
    }
}
