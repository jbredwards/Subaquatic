package git.jbredwards.subaquatic.mod.common.item.util;

import net.minecraft.block.state.IBlockState;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public interface IBlockCluster
{
    @Nonnull
    IBlockState withAmount(@Nonnull IBlockState oldState, int newAmount);
    int getAmount(@Nonnull IBlockState state);

    default int maxAmount() { return 3; }
}
