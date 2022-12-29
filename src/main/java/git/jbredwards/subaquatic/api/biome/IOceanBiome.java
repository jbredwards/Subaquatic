package git.jbredwards.subaquatic.api.biome;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nullable;

/**
 * Biomes that implement this will be automatically added to Forge's ocean biomes list
 * @author jbred
 *
 */
public interface IOceanBiome
{
    /**
     * Returns -1 if this is a deep ocean.
     * This is called often during world gen, so it's recommended to return a constant instead of using Biome.getIdForBiome.
     */
    int getDeepOceanBiomeId();

    /**
     * Used during world gen to determine if this is a shallow ocean.
     */
    static boolean isShallowOcean(@Nullable Biome biome) {
        if(biome == Biomes.OCEAN || biome == Biomes.FROZEN_OCEAN) return true; //special cases for vanilla
        return biome instanceof IOceanBiome && ((IOceanBiome)biome).getDeepOceanBiomeId() != -1;
    }
}
