/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.world.biome;

import git.jbredwards.subaquatic.mod.common.init.SubaquaticBiomes;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public final class BiomeColdOcean extends BiomeSandOcean
{
    public BiomeColdOcean(@Nonnull final BiomeProperties propertiesIn) {
        super(propertiesIn, () -> SubaquaticBiomes.DEEP_COLD_OCEAN, () -> SubaquaticBiomes.COLD_OCEAN);
    }
}
