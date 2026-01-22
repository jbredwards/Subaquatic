/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.world.biome;

import git.jbredwards.subaquatic.api.biome.BiomeSubaquaticOcean;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 *
 * @author jbred
 *
 */
public class BiomeSandOcean extends BiomeSubaquaticOcean
{
    public BiomeSandOcean(@Nonnull final BiomeProperties propertiesIn,
                          @Nonnull final Supplier<Biome> deepOceanIn,
                          @Nonnull final Supplier<Biome> shallowOceanIn) {
        super(propertiesIn, deepOceanIn, shallowOceanIn);
        surfaceBlock = Blocks.SAND.getDefaultState();
    }
}
