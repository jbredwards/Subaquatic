package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.fluidlogged_api.api.block.IFluidloggable;
import git.jbredwards.fluidlogged_api.api.util.FluidState;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public class BlockCoralFin extends AbstractBlockCoral implements IFluidloggable, IShearable
{
    @Nonnull
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.1, 0, 0.1, 0.9, 0.8, 0.9);

    public BlockCoralFin(@Nonnull Fluid neededFluidIn, @Nonnull Material materialIn) { super(neededFluidIn, materialIn); }
    public BlockCoralFin(@Nonnull Fluid neededFluidIn, @Nonnull Material materialIn, @Nonnull MapColor mapColorIn) {
        super(neededFluidIn, materialIn, mapColorIn);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, ALIVE); }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) { return getDefaultState().withProperty(ALIVE, (meta & 1) == 0); }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) { return state.getValue(ALIVE) ? 0 : 1; }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos) {
        return AABB;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean canPlaceBlockAt(@Nonnull World worldIn, @Nonnull BlockPos pos) {
        return worldIn.isSideSolid(pos.down(), EnumFacing.UP);
    }

    @Override
    public void neighborChanged(@Nonnull IBlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Block blockIn, @Nonnull BlockPos fromPos) {
        if(!canPlaceBlockAt(worldIn, pos)) worldIn.destroyBlock(pos, true);
    }

    @Override
    public boolean isOpaqueCube(@Nonnull IBlockState state) { return false; }

    @Override
    public boolean isFullCube(@Nonnull IBlockState state) { return false; }

    @Override
    public boolean isSideSolid(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(@Nonnull IBlockAccess worldIn, @Nonnull IBlockState state, @Nonnull BlockPos pos, @Nonnull EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Nullable
    @Override
    public PathNodeType getAiPathNodeType(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        return PathNodeType.OPEN;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() { return BlockRenderLayer.CUTOUT; }

    @Override
    public int quantityDropped(@Nonnull Random random) { return 0; }

    @Override
    public boolean isShearable(@Nonnull ItemStack item, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        return SubaquaticConfigHandler.Server.Block.coralPlantsShearable;
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack item, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, int fortune) {
        return Collections.singletonList(getSilkTouchDrop(world.getBlockState(pos)));
    }

    @Override
    public boolean hasNeededFluid(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        return FluidloggedUtils.isCompatibleFluid(neededFluid, FluidState.get(world, pos).getFluid())
                || FluidloggedUtils.isCompatibleFluid(neededFluid, FluidState.get(world, pos.down()).getFluid());
    }

    @Override
    public float getPlayerRelativeBlockHardness(@Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull World worldIn, @Nonnull BlockPos pos) {
        if(state.getValue(ALIVE)) return Float.MAX_VALUE;

        final ItemStack tool = player.getHeldItemMainhand();
        return tool.getItem() instanceof ItemShears && isShearable(tool, worldIn, pos) ? Float.MAX_VALUE : ForgeHooks.blockStrength(state, player, worldIn, pos);
    }
}
