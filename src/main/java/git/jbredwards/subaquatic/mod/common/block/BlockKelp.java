package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.fluidlogged_api.api.block.BlockWaterloggedPlant;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public class BlockKelp extends BlockWaterloggedPlant implements IGrowable
{
    @Nonnull public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
    @Nonnull public static final PropertyBool TOP = PropertyBool.create("top");
    @Nonnull protected static final AxisAlignedBB KELP_TOP_BB = new AxisAlignedBB(0, 0, 0, 1, 0.625, 1);

    public BlockKelp(@Nonnull Material materialIn) { this(materialIn, materialIn.getMaterialMapColor()); }
    public BlockKelp(@Nonnull Material materialIn, @Nonnull MapColor mapColorIn) {
        super(materialIn, mapColorIn);
        setDefaultState(getDefaultState().withProperty(AGE, 0).withProperty(TOP, false));
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, AGE, TOP); }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) { return state.getValue(AGE); }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) { return getDefaultState().withProperty(AGE, meta & 15); }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer) {
        final IBlockState down = worldIn.getBlockState(pos.down());
        return getDefaultState().withProperty(AGE, isEqualTo(down.getBlock(), this) ? down.getValue(AGE) : worldIn.rand.nextInt(4));
    }

    @Nonnull
    @Override
    public IBlockState getActualState(@Nonnull IBlockState state, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return state.withProperty(TOP, isKelpTop(worldIn, pos));
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos) {
        return isKelpTop(source, pos) ? KELP_TOP_BB : FULL_BLOCK_AABB;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getSelectedBoundingBox(@Nonnull IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos) {
        return state.getBoundingBox(worldIn, pos).offset(pos).offset(state.getOffset(worldIn, pos));
    }

    @Override
    public void updateTick(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
        final int age = state.getValue(AGE);
        if(age < 15) {
            final Fluid upFluid = FluidloggedUtils.getFluidFromState(worldIn.getBlockState(pos.up()));
            if(upFluid != null && isFluidValid(state, worldIn, pos.up(), upFluid) && rand.nextDouble() < 0.14)
                worldIn.setBlockState(pos.up(), state.withProperty(AGE, age + 1));
        }
    }

    @Override
    protected void checkAndDropBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if(!canBlockStay(worldIn, pos, state)) worldIn.destroyBlock(pos, true);
    }

    @Override
    protected boolean canSustainBush(@Nonnull IBlockState state) {
        if(isEqualTo(state.getBlock(), Blocks.MAGMA)) return false;
        return isEqualTo(state.getBlock(), this) || state.isTopSolid();
    }

    public boolean isKelpTop(@Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        return !isEqualTo(world.getBlockState(pos.up()).getBlock(), this);
    }

    @Nullable
    public BlockPos getTop(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState kelp, @Nonnull IBlockState here) {
        while(isEqualTo(here.getBlock(), this)) {
            pos = pos.up();
            here = world.getBlockState(pos);
        }

        final Fluid fluid = FluidloggedUtils.getFluidFromState(here);
        return fluid != null && isFluidValid(kelp, world, pos, fluid) ? pos : null;
    }

    @Nonnull
    @Override
    public EnumOffsetType getOffsetType() { return EnumOffsetType.XZ; }

    @Override
    public boolean canGrow(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean isClient) {
        return getTop(worldIn, pos, state, state) != null;
    }

    @Override
    public boolean canUseBonemeal(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        return true;
    }

    @Override
    public void grow(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        final BlockPos posToPlant = getTop(worldIn, pos, state, state);
        if(posToPlant != null) worldIn.setBlockState(posToPlant, state.withProperty(AGE,
                worldIn.getBlockState(posToPlant.down()).getValue(AGE)));
    }
}
