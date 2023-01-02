package git.jbredwards.subaquatic.mod.common.world.biome;

import git.jbredwards.subaquatic.api.biome.BiomeSubaquaticOcean;
import net.minecraft.init.Blocks;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class BiomeSandOcean extends BiomeSubaquaticOcean
{
    public BiomeSandOcean(@Nonnull BiomeProperties propertiesIn) {
        super(propertiesIn);
        surfaceBlock = Blocks.SAND.getDefaultState();
    }
}
