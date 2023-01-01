package git.jbredwards.subaquatic.api.biome;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.BiomeOcean;

import javax.annotation.Nonnull;

/**
 * Default implementation of IOceanBiome
 * @author jbred
 *
 */
public class BiomeSubaquaticOcean extends BiomeOcean implements IOceanBiome
{
    @Nonnull
    protected IBlockState surfaceBlock;
    public BiomeSubaquaticOcean(@Nonnull BiomeProperties propertiesIn) {
        super(propertiesIn);
        surfaceBlock = GRAVEL;
    }

    @Nonnull
    @Override
    public IBlockState getOceanSurface() { return surfaceBlock; }
}
