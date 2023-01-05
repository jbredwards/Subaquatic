package git.jbredwards.subaquatic.mod.common.config.util;

import git.jbredwards.fluidlogged_api.mod.common.config.FluidloggedAPIConfigHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * Same as Fluidlogged API's, but falls back on a water material check
 * @author jbred
 *
 */
public class BubbleColumnPredicate extends FluidloggedAPIConfigHandler.ConfigPredicate
{
    public BubbleColumnPredicate(@Nonnull Block block, @Nonnull int[] metadata, @Nonnull Set<Fluid> validFluids) {
        super(block, metadata, validFluids);
    }

    @Override
    public boolean test(@Nonnull IBlockState soilIn, @Nullable Fluid fluidIn) {
        //check fluidIn
        if(fluidIn != null) {
            if(!validFluids.isEmpty() && !validFluids.contains(fluidIn)) return false;
            if(!fluidIn.canBePlacedInWorld() || fluidIn.getBlock().getDefaultState().getMaterial() != Material.WATER)
                return false;
        }

        //check soilIn
        if(metadata.length == 0) return true;
        final int meta = block.getMetaFromState(soilIn);
        return metadata.length > meta && metadata[meta];
    }
}
