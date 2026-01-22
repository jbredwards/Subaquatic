/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.api.biome;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Custom ocean biomes don't have to implement this, but it's heavily recommended. Some reasons to implement this are:
 * <p>
 * -biomes that implement this will have their corresponding deep ocean biome generate
 * <p>
 * -biomes that implement this will have their corresponding mix ocean biome generate
 * <p>
 * -biomes that implement this will be automatically added to Forge's ocean biomes list
 * <p>
 * -biomes that implement this can have a custom surface block (other than gravel)
 * <p></p>
 *
 * @since 1.0.0
 * @author jbred
 *
 */
public interface IOceanBiome extends IOceanSurfaceProvider
{
    /**
     * @return This biome as a deep ocean.
     * @since 1.3.0
     */
    @Nonnull
    Biome getAsDeepOcean();

    /**
     * @return This biome as a shallow ocean.
     * @since 1.3.0
     */
    @Nonnull
    Biome getAsShallowOcean();

    /**
     * Used to gradually transition from shore/beach biomes.
     * @return Itself if this has no mix ocean biome.
     * @since 1.0.0
     */
    @Nonnull
    Biome getMixOceanBiome();

    /**
     * @return True if this is a valid biome for Ocean Monuments structure generation.
     * @since 1.3.0
     */
    default boolean generatesOceanMonument() { return getAsDeepOcean() == this; }

    /**
     * @return The provided biome as a deep ocean.
     * @since 1.3.0
     */
    @Nullable
    static Biome getDeepOcean(@Nonnull final Biome biome) {
        if(biome instanceof IOceanBiome) return ((IOceanBiome)biome).getAsDeepOcean();
        else return biome == Biomes.OCEAN || biome == Biomes.DEEP_OCEAN ? Biomes.DEEP_OCEAN : null;
    }

    /**
     * @return The provided biome as a shallow ocean.
     * @since 1.3.0
     */
    @Nullable
    static Biome getShallowOcean(@Nonnull final Biome biome) {
        if(biome instanceof IOceanBiome) return ((IOceanBiome)biome).getAsShallowOcean();
        else return biome == Biomes.OCEAN || biome == Biomes.DEEP_OCEAN ? Biomes.OCEAN : null;
    }

    /**
     * Use {@link IOceanBiome#getAsDeepOcean()} instead.
     * @since 1.0.0
     */
    @Deprecated
    default int getDeepOceanBiomeId() {
        @Nonnull final Biome deepOcean = getAsDeepOcean();
        return deepOcean != this ? Biome.getIdForBiome(deepOcean) : -1;
    }

    /**
     * Use {@link BiomeManager#oceanBiomes} instead.
     * @since 1.0.0
     */
    @Deprecated
    static boolean isOcean(final int biomeId) {
        return isOcean(Biome.getBiomeForId(biomeId));
    }

    /**
     * Use {@link BiomeManager#oceanBiomes} instead.
     * @since 1.0.0
     */
    @Deprecated
    static boolean isOcean(@Nullable final Biome biome) {
        return biome != null && BiomeManager.oceanBiomes.contains(biome);
    }

    /**
     * Use {@link IOceanBiome#getShallowOcean(Biome)} instead.
     * @since 1.0.0
     */
    @Deprecated
    static boolean isShallowOcean(final int biomeId) {
        return isShallowOcean(Biome.getBiomeForId(biomeId));
    }

    /**
     * Use {@link IOceanBiome#getShallowOcean(Biome)} instead.
     * @since 1.0.0
     */
    @Deprecated
    static boolean isShallowOcean(@Nullable final Biome biome) {
        if(biome != null && getShallowOcean(biome) == biome) return true;
        else return biome != Biomes.DEEP_OCEAN && !(biome instanceof IOceanBiome) && isOcean(biome);
    }
}
