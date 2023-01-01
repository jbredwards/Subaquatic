package git.jbredwards.subaquatic.api.biome;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import javax.annotation.Nonnull;

/**
 * Custom ocean biomes don't have to implement this, but it's heavily recommended. Some reasons to implement this are:
 * <p></p>
 * -biomes that implement this will be automatically added to Forge's ocean biomes list
 * <p>
 * -biomes that implement this can have a custom surface block (other than gravel)
 * <p></p>
 *
 * @author jbred
 *
 */
public interface IOceanBiome
{
    @Nonnull
    default IBlockState getOceanSurface() { return Blocks.GRAVEL.getDefaultState(); }
}
