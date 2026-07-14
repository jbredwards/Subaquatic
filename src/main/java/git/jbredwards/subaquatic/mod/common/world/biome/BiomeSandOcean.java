/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.world.biome;

import git.jbredwards.ocean_api.api.OceanAPI;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeOcean;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class BiomeSandOcean extends BiomeOcean
{
    public BiomeSandOcean(@Nonnull final BiomeProperties propertiesIn) {
        super(propertiesIn);
        OceanAPI.setOceanFloorBlock(this, Blocks.SAND.getDefaultState());
    }
}
