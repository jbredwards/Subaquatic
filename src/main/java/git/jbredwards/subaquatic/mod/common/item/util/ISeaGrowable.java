package git.jbredwards.subaquatic.mod.common.item.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public interface ISeaGrowable
{
    boolean canApplySeaBoneMeal(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EnumFacing side);
    void applySeaBoneMeal(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EnumFacing side);
}
