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
public final class BiomeLukewarmOcean extends BiomeSandOcean
{
    public BiomeLukewarmOcean(@Nonnull final BiomeProperties propertiesIn) {
        super(propertiesIn, () -> SubaquaticBiomes.DEEP_LUKEWARM_OCEAN, () -> SubaquaticBiomes.LUKEWARM_OCEAN);
    }
}
