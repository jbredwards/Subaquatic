package git.jbredwards.subaquatic.mod.common.integration.biomesoplenty;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.biome.IExtendedBiome;
import biomesoplenty.common.util.biome.BiomeUtils;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public final class BiomesOPlentyHandler
{
    @Nullable
    public static Biome getExtendedBiome(@Nullable Biome biome) {
        if(biome != null) {
            final @Nullable IExtendedBiome extBiome = BOPBiomes.REG_INSTANCE.getExtendedBiome(biome);
            if(extBiome != null) return BiomeUtils.getBiomeForLoc(extBiome.getBeachLocation());
        }

        return null;
    }
}
