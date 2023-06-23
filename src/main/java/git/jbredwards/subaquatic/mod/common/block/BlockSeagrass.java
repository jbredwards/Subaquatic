package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.fluidlogged_api.api.block.BlockWaterloggedPlant;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public class BlockSeagrass extends BlockWaterloggedPlant implements IShearable, IGrowable
{
    @Nonnull protected static final AxisAlignedBB SINGLE_BB = new AxisAlignedBB(0, 0, 0, 1, 0.75, 1);
    @Nonnull protected static final AxisAlignedBB TOP_BB = new AxisAlignedBB(0, 0, 0, 1, 0.875, 1);
    @Nonnull public static final PropertyEnum<Type> TYPE = PropertyEnum.create("type", Type.class);
    @Nonnull public final ThreadLocal<Boolean> isPlacing = ThreadLocal.withInitial(() -> false);

    public BlockSeagrass(@Nonnull Material materialIn) { super(materialIn); }
    public BlockSeagrass(@Nonnull Material materialIn, @Nonnull MapColor mapColorIn) {
        super(materialIn, mapColorIn);
        setTickRandomly(false);
        setDefaultState(getDefaultState().withProperty(TYPE, Type.SINGLE));
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, TYPE); }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(TYPE, Type.values()[meta % Type.values().length]);
    }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) { return state.getValue(TYPE).ordinal(); }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer) {
        return isEqualTo(worldIn.getBlockState(pos.down()).getBlock(), this) ? getDefaultState().withProperty(TYPE, Type.TOP) : getDefaultState();
    }

    @Override
    public void onBlockAdded(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if(state.getValue(TYPE) == Type.TOP) {
            final IBlockState down = worldIn.getBlockState(pos.down());
            if(isEqualTo(down.getBlock(), this) && down.getValue(TYPE) != Type.BOTTOM)
                worldIn.setBlockState(pos.down(), down.withProperty(TYPE, Type.BOTTOM), 2);
        }
    }

    @Override
    protected void checkAndDropBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if(!canBlockStay(worldIn, pos, state)) {
            worldIn.destroyBlock(pos, false);
            return;
        }

        final Type type = state.getValue(TYPE);
        if(type == Type.BOTTOM) {
            final IBlockState up = worldIn.getBlockState(pos.up());
            if(!isEqualTo(up.getBlock(), this)) worldIn.destroyBlock(pos, false);
            else if(up.getValue(TYPE) != Type.TOP) worldIn.setBlockState(pos, up.withProperty(TYPE, Type.TOP), 2);
        }

        else if(type == Type.TOP) {
            final IBlockState down = worldIn.getBlockState(pos.down());
            if(!isEqualTo(down.getBlock(), this)) worldIn.destroyBlock(pos, false);
            else if(down.getValue(TYPE) != Type.BOTTOM) worldIn.setBlockState(pos, down.withProperty(TYPE, Type.BOTTOM), 2);
        }
    }

    @Override
    public net.minecraftforge.common.EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
    {
        return net.minecraftforge.common.EnumPlantType.Water;
    }

    @Override
    protected boolean canSustainBush(@Nonnull IBlockState state) {
        if(isEqualTo(state.getBlock(), Blocks.MAGMA)) return false;
        else if(isEqualTo(state.getBlock(), this)) return state.getValue(TYPE) != Type.TOP;
        else return state.isTopSolid();
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos) {
        switch(state.getValue(TYPE)) {
            case SINGLE:
                return SINGLE_BB;
            case TOP:
                return TOP_BB;
            default:
                return FULL_BLOCK_AABB;
        }
    }

    @Override
    public boolean isShearable(@Nonnull ItemStack shears, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) { return true; }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack shears, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, int fortune) {
        return Collections.singletonList(new ItemStack(this));
    }

    @Override
    public boolean isReplaceable(@Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) { return !isPlacing.get(); }

    @Override
    public int quantityDropped(@Nonnull Random random) { return 0; }

    @Override
    public boolean canUseBonemeal(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        return true;
    }

    @Override
    public boolean canGrow(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean isClient) {
        if(state.getValue(TYPE) != Type.SINGLE) return false;
        final Fluid upFluid = FluidloggedUtils.getFluidFromState(worldIn.getBlockState(pos.up()));
        return upFluid != null && isFluidValid(state, worldIn, pos.up(), upFluid);
    }

    @Override
    public void grow(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        worldIn.setBlockState(pos.up(), state.withProperty(TYPE, Type.TOP));
    }

    public enum Type implements IStringSerializable
    {
        SINGLE("single"),
        TOP("top"),
        BOTTOM("bottom");

        @Nonnull
        final String name;
        Type(@Nonnull String nameIn) { name = nameIn; }

        @Nonnull
        @Override
        public String getName() { return name; }
    }
}
