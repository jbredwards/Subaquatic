/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class BlockFroglight extends BlockRotatedPillar
{
    @Nonnull
    public static final PropertyInteger TYPE = PropertyInteger.create("type", 0, 2);

    public BlockFroglight(@Nonnull Material materialIn) { this(materialIn, materialIn.getMaterialMapColor()); }
    public BlockFroglight(@Nonnull Material materialIn, @Nonnull MapColor mapColorIn) { super(materialIn, mapColorIn); }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, TYPE, AXIS); }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return super.getStateFromMeta(meta).withProperty(TYPE, MathHelper.clamp(meta & 3, 0, 2));
    }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) {
        return super.getMetaFromState(state) | state.getValue(TYPE);
    }

    @Override
    public int damageDropped(@Nonnull IBlockState state) { return state.getValue(TYPE); }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer) {
        return getDefaultState().withProperty(TYPE, MathHelper.clamp(meta, 0, 2)).withProperty(AXIS, facing.getAxis());
    }

    @Nonnull
    @Override
    public MapColor getMapColor(@Nonnull IBlockState state, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        switch(state.getValue(TYPE)) {
            case 0: return MapColor.SAND;
            case 1: return MapColor.PINK;
            default: return SubaquaticBlocks.GLOW_LICHEN_MAP_COLOR;
        }
    }
}
