/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.world.biome;

import git.jbredwards.subaquatic.mod.common.init.SubaquaticBiomes;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public final class BiomeWarmOcean extends BiomeSandOcean
{
    public BiomeWarmOcean(@Nonnull final BiomeProperties propertiesIn) {
        super(propertiesIn, () -> SubaquaticBiomes.DEEP_WARM_OCEAN, () -> SubaquaticBiomes.WARM_OCEAN);
    }

    @Nonnull
    @Override
    public Biome getMixOceanBiome() { return SubaquaticBiomes.LUKEWARM_OCEAN; }
}
