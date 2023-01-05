package git.jbredwards.subaquatic.mod.common.world.biome;

import git.jbredwards.subaquatic.api.biome.BiomeSubaquaticOcean;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public class BiomeSandOcean extends BiomeSubaquaticOcean
{
    public BiomeSandOcean(@Nullable Biome deepOceanBiomeIn, @Nonnull BiomeProperties propertiesIn) {
        super(deepOceanBiomeIn, propertiesIn);
        surfaceBlock = Blocks.SAND.getDefaultState();
    }
}
