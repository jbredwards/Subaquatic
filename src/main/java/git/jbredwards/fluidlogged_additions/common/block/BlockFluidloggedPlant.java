package git.jbredwards.fluidlogged_additions.common.block;

import git.jbredwards.fluidlogged_api.common.block.AbstractFluidloggedBlock;
import net.minecraft.block.Block;
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
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public abstract class BlockFluidloggedPlant extends AbstractFluidloggedBlock
{
    protected BlockFluidloggedPlant(Fluid fluid, Material material, MapColor mapColor) {
        super(fluid, material, mapColor);
    }

    protected BlockFluidloggedPlant(Fluid fluid, Material material) {
        super(fluid, material);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return NULL_AABB;
    }

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

    @Override
    public void neighborChanged(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Block neighborBlock, @Nonnull BlockPos neighbourPos) {
        //if block below this is broken
        if(!canPlaceBlockAt(world, pos)) {
            //breaks the block
            dropBlockAsItem(world, pos, state, 0);
            world.playEvent(2001, pos, Block.getStateId(state));
            world.setBlockState(pos, fluid.getBlock().getDefaultState(), 11);
        }

        super.neighborChanged(state, world, pos, neighborBlock, neighbourPos);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos);
    }
}
