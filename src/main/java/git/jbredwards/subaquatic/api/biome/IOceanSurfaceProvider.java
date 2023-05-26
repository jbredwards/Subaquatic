package git.jbredwards.subaquatic.api.biome;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import javax.annotation.Nonnull;

/**
 * Biomes that implement this can specify a custom block for their ocean surface.
 * This is used by beach biomes & warm ocean biomes to generate sand instead of gravel.
 *
 * @since 1.0.0
 * @author jbred
 *
 */
public interface IOceanSurfaceProvider
{
    /**
     * Returns the block that generates on ocean surfaces.
     */
    @Nonnull
    default IBlockState getOceanSurface() { return Blocks.SAND.getDefaultState(); }
}
