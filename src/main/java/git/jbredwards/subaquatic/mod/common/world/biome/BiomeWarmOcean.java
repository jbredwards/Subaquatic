/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.world.biome;

import git.jbredwards.subaquatic.api.biome.OceanType;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBiomes;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public class BiomeWarmOcean extends BiomeSandOcean
{
    public BiomeWarmOcean(@Nullable Biome deepOceanBiomeIn, @Nonnull BiomeProperties propertiesIn) {
        super(deepOceanBiomeIn, propertiesIn);
    }

    @Nonnull
    @Override
    public Biome getMixOceanBiome() { return SubaquaticBiomes.LUKEWARM_OCEAN; }

    @Nullable
    @Override
    public OceanType getOceanType() { return OceanType.WARM; }
}
