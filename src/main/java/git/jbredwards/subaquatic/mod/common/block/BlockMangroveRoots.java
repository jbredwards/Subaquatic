/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.fluidlogged_api.api.block.IFluidloggable;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public class BlockMangroveRoots extends BlockMud implements IFluidloggable
{
    @Nonnull
    public static final PropertyBool HAS_MUD = PropertyBool.create("has_mud");

    public BlockMangroveRoots(@Nonnull Material materialIn) { this(materialIn, materialIn.getMaterialMapColor()); }
    public BlockMangroveRoots(@Nonnull Material materialIn, @Nonnull MapColor mapColorIn) {
        super(materialIn, mapColorIn);
        setDefaultState(getDefaultState().withProperty(HAS_MUD, false));
        setHarvestLevel("axe", 0, getDefaultState());
        setHarvestLevel("shovel", 0, getDefaultState().withProperty(HAS_MUD, true));
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HAS_MUD);
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(HAS_MUD, (meta & 1) != 0);
    }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) {
        return state.getValue(HAS_MUD) ? 1 : 0;
    }

    @Override
    public boolean isMud(@Nonnull IBlockState state) {
        return state.getValue(HAS_MUD);
    }

    @Nonnull
    @Override
    public SoundType getSoundType(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nullable Entity entity) {
        return isMud(state) ? SubaquaticSounds.MANGROVE_ROOTS_MUD : SubaquaticSounds.MANGROVE_ROOTS;
    }

    @Nonnull
    @Override
    public MapColor getMapColor(@Nonnull final IBlockState state, @Nonnull final IBlockAccess worldIn, @Nonnull final BlockPos pos) {
        return isMud(state) ? super.getMapColor(state, worldIn, pos) : BlockDirt.DirtType.PODZOL.getColor();
    }

    @Override
    public boolean isFullCube(@Nonnull final IBlockState state) {
        return isMud(state);
    }

    @Override
    public boolean isOpaqueCube(@Nonnull final IBlockState state) {
        return isMud(state);
    }

    @Override
    public boolean isToolEffective(@Nullable final String type, @Nonnull final IBlockState state) {
        return type != null && (type.equals("axe") || isMud(state) && type.equals("shovel"));
    }

    @Override
    public boolean canRenderInLayer(@Nonnull final IBlockState state, @Nonnull final BlockRenderLayer layer) {
        return isMud(state) ? layer == BlockRenderLayer.SOLID : layer == BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isFluidloggable(@Nonnull final IBlockState state, @Nonnull final World world, @Nonnull final BlockPos pos) {
        return !isMud(state);
    }

    @Override
    public boolean canFluidConnect(@Nonnull final IBlockAccess world, @Nonnull final BlockPos pos, @Nonnull final IBlockState here, @Nonnull final EnumFacing side) {
        return !isMud(here);
    }

    @Nonnull
    @Override
    public AxisAlignedBB getCollisionBoundingBox(@Nonnull final IBlockState state, @Nonnull final IBlockAccess worldIn, @Nonnull final BlockPos pos) {
        return FULL_BLOCK_AABB;
    }
}
