package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.fluidlogged_api.api.block.IFluidloggable;
import git.jbredwards.fluidlogged_api.api.util.FluidState;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.mod.common.item.util.IBlockCluster;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public class BlockSeaPickle extends BlockBush implements IGrowable, IFluidloggable, IBlockCluster
{
    @Nonnull public static final PropertyInteger PICKLES = PropertyInteger.create("pickles", 0, 3);
    @Nonnull public static final PropertyBool GLOWING = PropertyBool.create("glowing");
    @Nonnull protected static AxisAlignedBB[] aabb = new AxisAlignedBB[] {
            new AxisAlignedBB(0.375,  0, 0.375,  0.625,  0.375,  0.625),
            new AxisAlignedBB(0.1875, 0, 0.1875, 0.75,   0.375,  0.75),
            new AxisAlignedBB(0.125,  0, 0.125,  0.75,   0.375,  0.8125),
            new AxisAlignedBB(0.125,  0, 0.125,  0.8125, 0.4375, 0.875)
    };

    public BlockSeaPickle(@Nonnull Material materialIn) { this(materialIn, materialIn.getMaterialMapColor()); }
    public BlockSeaPickle(@Nonnull Material materialIn, @Nonnull MapColor mapColorIn) {
        super(materialIn, mapColorIn);
        setTickRandomly(false);
        setDefaultState(getDefaultState().withProperty(PICKLES, 0).withProperty(GLOWING, false));
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, PICKLES, GLOWING); }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(GLOWING, meta >> 2 > 0).withProperty(PICKLES, meta & 3);
    }

    @Nonnull
    @Override
    public IBlockState withAmount(@Nonnull IBlockState oldState, int newAmount) {
        return oldState.withProperty(PICKLES, newAmount);
    }

    @Override
    public int getAmount(@Nonnull IBlockState state) { return state.getValue(PICKLES); }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) {
        return (state.getValue(GLOWING) ? 4 : 0) | state.getValue(PICKLES);
    }

    @Override
    public int getLightValue(@Nonnull IBlockState state) {
        return state.getValue(GLOWING) ? 6 + 3 * state.getValue(PICKLES) : 0;
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer) {
        return getDefaultState().withProperty(GLOWING, FluidloggedUtils.getFluidState(worldIn, pos).getMaterial() == Material.WATER);
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos) {
        return aabb[state.getValue(PICKLES)];
    }

    @Nonnull
    @Override
    public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return blockState.getBoundingBox(worldIn, pos);
    }

    @Override
    protected boolean canSustainBush(@Nonnull IBlockState state) {
        return !isEqualTo(state.getBlock(), Blocks.MAGMA) && state.isTopSolid();
    }

    @Override
    public boolean canPlaceBlockAt(@Nonnull World worldIn, @Nonnull BlockPos pos) {
        if(!canSustainBush(worldIn.getBlockState(pos.down()))) return false;

        final FluidState fluidState = FluidloggedUtils.getFluidState(worldIn, pos);
        return fluidState.isEmpty() || isFluidValid(getDefaultState(), worldIn, pos, fluidState.getFluid());
    }

    @Nonnull
    @Override
    public EnumActionResult onFluidFill(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState here, @Nonnull FluidState newFluid, int blockFlags) {
        if(!isFluidValid(here, world, pos, newFluid.getFluid())) {
            dropBlockAsItem(world, pos, here, 0);
            world.setBlockState(pos, newFluid.getState(), blockFlags);
            return EnumActionResult.SUCCESS;
        }

        else if(!here.getValue(GLOWING)) world.setBlockState(pos, here.withProperty(GLOWING, true), blockFlags);
        return EnumActionResult.PASS;
    }

    @Nonnull
    @Override
    public EnumActionResult onFluidDrain(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState here, int blockFlags) {
        if(here.getValue(GLOWING)) world.setBlockState(pos, here.withProperty(GLOWING, false), blockFlags);
        return EnumActionResult.PASS;
    }

    @Override
    public boolean isFluidValid(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Fluid fluid) {
        return fluid.canBePlacedInWorld() && fluid.getBlock().getDefaultState().getMaterial() == Material.WATER;
    }

    @Override
    public boolean canRenderInLayer(@Nonnull IBlockState state, @Nonnull BlockRenderLayer layer) {
        return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.CUTOUT && state.getValue(GLOWING);
    }

    @Override
    public int quantityDropped(@Nonnull IBlockState state, int fortune, @Nonnull Random random) {
        return state.getValue(PICKLES) + 1;
    }

    @Override
    public boolean canUseBonemeal(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        return true;
    }

    @Override
    public boolean canGrow(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean isClient) {
        if(state.getValue(GLOWING)) {
            final IBlockState down = worldIn.getBlockState(pos.down());
            return down.getBlock() instanceof BlockCoral && down.getValue(BlockCoral.ALIVE);
        }

        return false;
    }

    @Override
    public void grow(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if(worldIn.isAreaLoaded(pos, 1)) {
            for(int x = -1; x <= 1; x++) {
                for(int y = -1; y <= 1; y++) {
                    for(int z = -1; z <= 1; z++) {
                        final BlockPos offset = pos.add(x, y, z);
                        if(!offset.equals(pos) && rand.nextInt(6) == 0 && worldIn.getBlockState(offset).getMaterial() == Material.WATER) {
                            final IBlockState soil = worldIn.getBlockState(offset.down());
                            if(soil.getBlock() instanceof BlockCoral && soil.getValue(BlockCoral.ALIVE))
                                worldIn.setBlockState(offset, getDefaultState()
                                        .withProperty(GLOWING, true)
                                        .withProperty(PICKLES, rand.nextInt(4)));
                        }
                    }
                }
            }
        }

        worldIn.setBlockState(pos, state.withProperty(PICKLES, 3), 2);
    }
}
