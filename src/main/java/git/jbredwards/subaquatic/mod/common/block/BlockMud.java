package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.fluidlogged_api.mod.asm.plugins.vanilla.block.PluginBlockBush;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class BlockMud extends Block
{
    @Nonnull
    public static final AxisAlignedBB COLLISION_BOX = new AxisAlignedBB(0, 0, 0, 1, 0.875, 1);

    public BlockMud(@Nonnull Material materialIn) { this(materialIn, materialIn.getMaterialMapColor()); }
    public BlockMud(@Nonnull Material materialIn, @Nonnull MapColor mapColorIn) { super(materialIn, mapColorIn); }

    @Nonnull
    @Override
    public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return COLLISION_BOX;
    }

    @Override
    public boolean canSustainPlant(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing direction, @Nonnull IPlantable plantable) {
        if(plantable instanceof PluginBlockBush.Accessor && ((PluginBlockBush.Accessor)plantable).canSustainBush_Public(state)) return true;
        final EnumPlantType plant = plantable.getPlantType(world, pos.offset(direction));
        return plant == EnumPlantType.Plains || plant == EnumPlantType.Beach && (
                   FluidloggedUtils.getFluidOrReal(world, pos.east()).getMaterial() == Material.WATER
                || FluidloggedUtils.getFluidOrReal(world, pos.west()).getMaterial() == Material.WATER
                || FluidloggedUtils.getFluidOrReal(world, pos.north()).getMaterial() == Material.WATER
                || FluidloggedUtils.getFluidOrReal(world, pos.south()).getMaterial() == Material.WATER
        );
    }

    @Override
    public void onPlantGrow(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockPos source) {}
}
