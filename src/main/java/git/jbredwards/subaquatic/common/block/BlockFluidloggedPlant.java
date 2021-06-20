package git.jbredwards.subaquatic.common.block;

import git.jbredwards.fluidlogged_api.common.block.AbstractFluidloggedBlock;
import git.jbredwards.fluidlogged_api.common.block.IParticleColor;
import git.jbredwards.fluidlogged_api.util.FluidloggedUtils;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
@SuppressWarnings("NullableProblems")
public abstract class BlockFluidloggedPlant extends AbstractFluidloggedBlock implements IGrowable, IParticleColor
{
    protected BlockFluidloggedPlant(Fluid fluid, Material material, MapColor mapColor) {
        super(fluid, material, mapColor);
        setSoundType(SoundType.PLANT);
    }

    protected BlockFluidloggedPlant(Fluid fluid, Material material) {
        super(fluid, material);
        setSoundType(SoundType.PLANT);
    }

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos) {
        return PathNodeType.WATER;
    }

    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(@Nonnull IBlockAccess worldIn, @Nonnull IBlockState state, @Nonnull BlockPos pos, @Nonnull EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @SuppressWarnings("deprecation")
    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.DESTROY;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public void neighborChanged(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Block neighborBlock, @Nonnull BlockPos neighbourPos) {
        //if block below this is broken
        if(!canPlaceOnFloor(world, pos)) {
            //breaks the block
            dropBlockAsItem(world, pos, state, 0);
            world.playEvent(Constants.WorldEvents.BREAK_BLOCK_EFFECTS, pos, Block.getStateId(state));
            world.setBlockState(pos, fluid.getBlock().getDefaultState(), 11);
        }

        super.neighborChanged(state, world, pos, neighborBlock, neighbourPos);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        final IBlockState state = worldIn.getBlockState(pos);

        if(state.getBlock() == fluid.getBlock()) {
            final int level = state.getValue(LEVEL);
            if((level == 0 || level == 8) && fluid == FluidloggedUtils.getFluidFromBlock(state.getBlock())) {
                return canPlaceOnFloor(worldIn, pos);
            }
        }

        return false;
    }

    //same as canPlaceBlockAt, but doesn't check for fluid
    public boolean canPlaceOnFloor(World world, BlockPos pos) {
        final IBlockState down = world.getBlockState(pos.down());

        if(down.getBlockFaceShape(world, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID) return true;
        else return canPlaceOnPlant(down, world, pos.down());
    }

    public boolean canPlaceOnPlant(IBlockState plant, World world, BlockPos pos) {
        return plant.getBlock() == this;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return canGrow(worldIn, pos, state, worldIn.isRemote);
    }

    @Override
    public int getParticleColor(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos) {
        return -1;
    }
}
