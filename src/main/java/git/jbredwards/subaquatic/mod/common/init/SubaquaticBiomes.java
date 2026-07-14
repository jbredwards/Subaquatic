/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.ocean_api.api.OceanAPI;
import git.jbredwards.ocean_api.api.OceanType;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.world.biome.*;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeBeach;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 *
 * @author jbred
 *
 */
public final class SubaquaticBiomes
{
    // Init
    @Nonnull public static final List<Biome> INIT = new LinkedList<>();

    // Deep Biomes
    @Nonnull public static final BiomeSandOcean DEEP_WARM_OCEAN = register("deep_warm_ocean",
            new BiomeSandOcean(new Biome.BiomeProperties("Deep Warm Ocean").setBaseHeight(-1.8f).setHeightVariation(0.1f).setTemperature(0.5f).setRainfall(0.5f)));

    @Nonnull public static final BiomeSandOcean DEEP_LUKEWARM_OCEAN = register("deep_lukewarm_ocean",
            new BiomeSandOcean(new Biome.BiomeProperties("Deep Lukewarm Ocean").setBaseHeight(-1.8f).setHeightVariation(0.1f).setTemperature(0.5f).setRainfall(0.5f)));

    @Nonnull public static final BiomeSandOcean DEEP_COLD_OCEAN = register("deep_cold_ocean",
            new BiomeSandOcean(new Biome.BiomeProperties("Deep Cold Ocean").setBaseHeight(-1.8f).setHeightVariation(0.1f).setTemperature(0.5f).setRainfall(0.5f)));

    @Nonnull public static final BiomeFrozenOcean DEEP_FROZEN_OCEAN = register("deep_frozen_ocean",
            new BiomeFrozenOcean(new Biome.BiomeProperties("Deep Frozen Ocean").setBaseHeight(-1.8f).setHeightVariation(0.1f).setTemperature(0.5f).setRainfall(0.5f).setSnowEnabled()));

    // Shallow Biomes
    @Nonnull public static final BiomeSandOcean WARM_OCEAN = register("warm_ocean",
            new BiomeSandOcean(new Biome.BiomeProperties("Warm Ocean").setBaseHeight(-1).setHeightVariation(0.1f).setTemperature(0.5f).setRainfall(0.5f)));

    @Nonnull public static final BiomeSandOcean LUKEWARM_OCEAN = register("lukewarm_ocean",
            new BiomeSandOcean(new Biome.BiomeProperties("Lukewarm Ocean").setBaseHeight(-1).setHeightVariation(0.1f).setTemperature(0.5f).setRainfall(0.5f)));

    @Nonnull public static final BiomeSandOcean COLD_OCEAN = register("cold_ocean",
            new BiomeSandOcean(new Biome.BiomeProperties("Cold Ocean").setBaseHeight(-1).setHeightVariation(0.1f).setTemperature(0.5f).setRainfall(0.5f)));

    // Mangrove Swamp
    @Nonnull public static final BiomeMangroveSwamp MANGROVE_SWAMP = register("mangrove_swamp",
            new BiomeMangroveSwamp(new Biome.BiomeProperties("Mangrove Swamp").setBaseHeight(-0.2f).setHeightVariation(0.1f).setTemperature(0.8f).setRainfall(0.9f).setWaterColor(3832426)));

    // Biome Dictionary
    static void registerBiomeDictionary() {
        BiomeDictionary.addTypes(DEEP_WARM_OCEAN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.OCEAN);
        BiomeDictionary.addTypes(DEEP_LUKEWARM_OCEAN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.OCEAN);
        BiomeDictionary.addTypes(DEEP_COLD_OCEAN, BiomeDictionary.Type.COLD, BiomeDictionary.Type.OCEAN);
        BiomeDictionary.addTypes(DEEP_FROZEN_OCEAN, BiomeDictionary.Type.COLD, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.SNOWY);
        BiomeDictionary.addTypes(WARM_OCEAN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.OCEAN);
        BiomeDictionary.addTypes(LUKEWARM_OCEAN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.OCEAN);
        BiomeDictionary.addTypes(COLD_OCEAN, BiomeDictionary.Type.COLD, BiomeDictionary.Type.OCEAN);
        BiomeDictionary.addTypes(MANGROVE_SWAMP, BiomeDictionary.Type.SWAMP, BiomeDictionary.Type.WET);
        // Biome generation.
        BiomeManager.addBiome(BiomeManager.BiomeType.WARM, new BiomeManager.BiomeEntry(MANGROVE_SWAMP, 10));
        OceanAPI.registerOceanBiome(OceanType.FROZEN, new BiomeManager.BiomeEntry(Biomes.FROZEN_OCEAN, 1000), DEEP_FROZEN_OCEAN, COLD_OCEAN);
        OceanAPI.registerOceanBiome(OceanType.COLD, new BiomeManager.BiomeEntry(COLD_OCEAN, 1000), DEEP_COLD_OCEAN, null);
        OceanAPI.registerOceanBiome(OceanType.LUKEWARM, new BiomeManager.BiomeEntry(LUKEWARM_OCEAN, 1000), DEEP_LUKEWARM_OCEAN, null);
        OceanAPI.registerOceanBiome(OceanType.WARM, new BiomeManager.BiomeEntry(WARM_OCEAN, 1000), DEEP_WARM_OCEAN, LUKEWARM_OCEAN);
        // Ocean monument biomes.
        OceanAPI.registerOceanMonumentWaterBiome(DEEP_WARM_OCEAN);
        OceanAPI.registerOceanMonumentWaterBiome(DEEP_LUKEWARM_OCEAN);
        OceanAPI.registerOceanMonumentWaterBiome(DEEP_COLD_OCEAN);
        OceanAPI.registerOceanMonumentWaterBiome(DEEP_FROZEN_OCEAN);
        OceanAPI.registerOceanMonumentWaterBiome(WARM_OCEAN);
        OceanAPI.registerOceanMonumentWaterBiome(LUKEWARM_OCEAN);
        OceanAPI.registerOceanMonumentWaterBiome(COLD_OCEAN);
        OceanAPI.registerOceanMonumentSpawnBiome(DEEP_WARM_OCEAN);
        OceanAPI.registerOceanMonumentSpawnBiome(DEEP_LUKEWARM_OCEAN);
        OceanAPI.registerOceanMonumentSpawnBiome(DEEP_COLD_OCEAN);
        OceanAPI.registerOceanMonumentSpawnBiome(DEEP_FROZEN_OCEAN);
        // Make beach biomes have a sand ocean floor.
        for(@Nonnull final Biome biome : Biome.REGISTRY) if(biome instanceof BiomeBeach) OceanAPI.setOceanFloorBlock(biome, biome.topBlock);
    }

    //registry
    @Nonnull
    static <T extends Biome> T register(@Nonnull String name, @Nonnull T biome) {
        INIT.add(biome.setRegistryName(Subaquatic.MODID, name));
        return biome;
    }

    @Nonnull
    static <T extends Biome> T register(@Nonnull String name, @Nonnull T biome, @Nonnull final Consumer<T> properties) {
        properties.accept(biome);
        return register(name, biome);
    }
}
