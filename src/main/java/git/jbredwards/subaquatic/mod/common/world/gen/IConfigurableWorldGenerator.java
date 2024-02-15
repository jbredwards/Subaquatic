/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.world.gen;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public interface IConfigurableWorldGenerator extends IWorldGenerator
{
    default boolean isDimensionValid(@Nonnull World world, @Nonnull int[] validDims) {
        return validDims.length == 0 || ArrayUtils.contains(validDims, world.provider.getDimension());
    }

    default int getMaxForBiome(@Nonnull World world, int biomeX, int biomeZ, @Nonnull Object2IntMap<Biome> perBiomeRarity, int defaultValue) {
        return perBiomeRarity.getOrDefault(world.getBiome(new BlockPos(biomeX, 0, biomeZ)), defaultValue);
    }
}
