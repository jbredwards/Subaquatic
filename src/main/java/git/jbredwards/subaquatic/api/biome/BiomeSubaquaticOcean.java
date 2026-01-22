/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.api.biome;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeOcean;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * Default implementation of {@link IOceanBiome}.
 *
 * @since 1.0.0
 * @author jbred
 *
 */
public class BiomeSubaquaticOcean extends BiomeOcean implements IOceanBiome
{
    @Nonnull protected IBlockState surfaceBlock;
    @Nonnull private final Supplier<Biome> shallowOcean, deepOcean;

    public BiomeSubaquaticOcean(@Nonnull final BiomeProperties propertiesIn,
                                @Nonnull final Supplier<Biome> deepOceanIn,
                                @Nonnull final Supplier<Biome> shallowOceanIn) {
        super(propertiesIn);
        surfaceBlock = GRAVEL;
        deepOcean = deepOceanIn;
        shallowOcean = shallowOceanIn;
    }

    @Nonnull
    @Override
    public IBlockState getOceanSurface() { return surfaceBlock; }

    @Nonnull
    @Override
    public Biome getMixOceanBiome() { return this; }

    @Nonnull
    @Override
    public Biome getAsDeepOcean() { return deepOcean.get(); }

    @Nonnull
    @Override
    public Biome getAsShallowOcean() { return shallowOcean.get(); }
}
