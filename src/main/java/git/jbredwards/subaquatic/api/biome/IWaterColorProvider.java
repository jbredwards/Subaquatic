/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.api.biome;

import net.minecraft.world.biome.Biome;

/**
 * When a biome has a unique water surface color, and is not mentioned in this mod's config,
 * this mod uses an algorithm to try to adapt it to work with the colorless water texture.
 * <p>
 * Biomes that implement this have their custom water surface colors & water fog colors unchanged.
 * Implementing this is recommended for mods whose biomes should not have their water colors changed!
 * <p>
 * To only implement this while Subaquatic is installed, use forge's
 * {@link net.minecraftforge.fml.common.Optional.Interface @Optional.Interface} and
 * {@link net.minecraftforge.fml.common.Optional.Method @Optional.Method} annotations.
 *
 * @since 1.1.0
 * @author jbred
 *
 */
public interface IWaterColorProvider
{
    default int getWaterFogColor() { return getWaterSurfaceColor(); }
    default int getWaterSurfaceColor() { return ((Biome)this).waterColor; }
}
