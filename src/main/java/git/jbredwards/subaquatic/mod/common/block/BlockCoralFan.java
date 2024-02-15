/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.fluidlogged_api.api.util.FluidState;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class BlockCoralFan extends BlockCoralFin
{
    @Nonnull
    public static final PropertyDirection SIDE = PropertyDirection.create("side", side -> side != EnumFacing.DOWN);

    @Nonnull
    protected static final AxisAlignedBB[] AABB = new AxisAlignedBB[] {null,
            new AxisAlignedBB(0.125, 0,      0.125, 0.875, 0.25,   0.875),
            new AxisAlignedBB(0.125, 0.4375, 0.5,   0.875, 0.5625, 1),
            new AxisAlignedBB(0.125, 0.4375, 0,     0.875, 0.5625, 0.5),
            new AxisAlignedBB(0.5,   0.4375, 0.125, 1,     0.5625, 0.875),
            new AxisAlignedBB(0,     0.4375, 0.125, 0.5,   0.5625, 0.875)
    };

    public BlockCoralFan(@Nonnull Fluid neededFluidIn, @Nonnull Material materialIn) { super(neededFluidIn, materialIn); }
    public BlockCoralFan(@Nonnull Fluid neededFluidIn, @Nonnull Material materialIn, @Nonnull MapColor mapColorIn) {
        super(neededFluidIn, materialIn, mapColorIn);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, ALIVE, SIDE); }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) {
        return state.getValue(SIDE).getIndex() << 1 | (state.getValue(ALIVE) ? 0 : 1);
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState()
                .withProperty(SIDE, EnumFacing.byIndex(meta >> 1))
                .withProperty(ALIVE, (meta & 1) == 0);
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos) {
        return AABB[state.getValue(SIDE).getIndex()];
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer) {
        final IBlockState state = getDefaultState().withProperty(ALIVE, (meta & 1) == 0);
        if(canPlaceBlockOnSide(worldIn, pos, facing)) return state.withProperty(SIDE, facing);
        //search for valid side
        for(EnumFacing side : EnumFacing.values()) {
            if(side != EnumFacing.DOWN && canPlaceBlockOnSide(worldIn, pos, side))
                return state.withProperty(SIDE, side);
        }

        //should never pass
        throw new IllegalStateException("Could not place coral fan on illegal side");
    }

    @Override
    public boolean canPlaceBlockAt(@Nonnull World worldIn, @Nonnull BlockPos pos) {
        for(EnumFacing side : EnumFacing.values())
            if(side != EnumFacing.DOWN && canPlaceOnSide(worldIn, pos, side))
                return true;

        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
        return side == EnumFacing.DOWN ? canPlaceBlockAt(worldIn, pos) : canPlaceOnSide(worldIn, pos, side);
    }

    protected boolean canPlaceOnSide(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
        return worldIn.isSideSolid(pos.offset(side.getOpposite()), side);
    }

    @Override
    public void neighborChanged(@Nonnull IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Block blockIn, @Nonnull BlockPos fromPos) {
        if(!canPlaceBlockOnSide(worldIn, pos, state.getValue(SIDE))) worldIn.destroyBlock(pos, true);
    }

    @Override
    public boolean hasNeededFluid(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        return FluidloggedUtils.isCompatibleFluid(neededFluid, FluidState.get(world, pos).getFluid())
                || FluidloggedUtils.isCompatibleFluid(neededFluid, FluidState.get(world, pos.offset(state.getValue(SIDE))).getFluid());
    }
}
