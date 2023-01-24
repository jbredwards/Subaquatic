package git.jbredwards.subaquatic.mod.common.compat.bop;

import biomesoplenty.api.biome.BOPBiomes;
import biomesoplenty.api.biome.IExtendedBiome;
import biomesoplenty.common.biome.overworld.BOPOverworldBiome;
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
    public static boolean doesBiomeSupportRivers(@Nullable Biome biome) {
        return !(biome instanceof BOPOverworldBiome) || ((BOPOverworldBiome)biome).canGenerateRivers;
    }

    public static boolean isExtendedBiome(@Nullable Biome biome) {
        return biome != null && BOPBiomes.REG_INSTANCE.getExtendedBiome(biome) != null;
    }

    @Nullable
    public static Biome getBOPBeachBiome(@Nullable Biome biome) {
        if(biome != null) {
            final @Nullable IExtendedBiome extBiome = BOPBiomes.REG_INSTANCE.getExtendedBiome(biome);
            if(extBiome != null) return BiomeUtils.getBiomeForLoc(extBiome.getBeachLocation());
        }

        return null;
    }
}
