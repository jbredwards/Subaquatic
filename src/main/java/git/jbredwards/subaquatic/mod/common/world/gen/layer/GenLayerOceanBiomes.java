/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.world.gen.layer;

import git.jbredwards.subaquatic.api.biome.IOceanBiome;
import git.jbredwards.subaquatic.api.biome.OceanType;
import git.jbredwards.subaquatic.api.event.GetOceanForGenEvent;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import git.jbredwards.subaquatic.mod.common.world.gen.NoiseGeneratorOceans;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.event.terraingen.WorldTypeEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public final class GenLayerOceanBiomes extends GenLayer
{
    @Nonnull
    private final GenLayer wrapped;
    private NoiseGeneratorOceans temperatureGenerator;

    public GenLayerOceanBiomes(final long seed, @Nonnull final GenLayer wrappedIn) {
        super(seed);
        wrapped = wrappedIn;
    }

    @Nonnull
    @Override
    public int[] getInts(final int areaX, final int areaZ, final int areaWidth, final int areaHeight) {
        final int ocean = Biome.getIdForBiome(Biomes.OCEAN);
        final int deepOcean = Biome.getIdForBiome(Biomes.DEEP_OCEAN);
        final int[] biomeInts = wrapped.getInts(areaX-1, areaZ-1, areaWidth+2, areaHeight+2).clone();
        IntCache.resetIntCache();
        //create separate ocean biomes layer
        //this is merged into the main layer wherever the main layer has an ocean biome
        final int[] out = GenLayerZoom.magnify(2001,
                        new GenLayerOceanBiomeMask(baseSeed, temperatureGenerator),
                        SubaquaticConfigHandler.Server.World.General.oceanBiomeSize)
                .getInts(areaX, areaZ, areaWidth, areaHeight);

        //merge two layers
        for(int x = 0; x < areaWidth; x++) {
            for(int z = 0; z < areaHeight; z++) {
                final int biomeId = biomeInts[x + 1 + (z + 1) * (areaWidth + 2)];
                //convert ocean biomes to deep ocean ones if necessary
                if(biomeId == deepOcean) out[x + z * areaWidth] = handleDeepOceanGen(Biome.getBiomeForId(out[x + z * areaWidth]), deepOcean);
                //re-apply old layer data to the main layer
                else if(biomeId != ocean) out[x + z * areaWidth] = biomeId;
            }
        }

        return out;
    }

    //seed to test ocean gen: -4426319367184787986
    @Override
    public void initWorldGenSeed(final long seed) {
        super.initWorldGenSeed(seed);
        temperatureGenerator = new NoiseGeneratorOceans(new Random(seed));
    }

    static int handleDeepOceanGen(@Nullable final Biome ocean, final int deepOcean) {
        return ocean instanceof IOceanBiome ? Biome.getIdForBiome(((IOceanBiome)ocean).getAsDeepOcean()) : deepOcean;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void handleGenLayerWrappers(@Nonnull final WorldTypeEvent.InitBiomeGens event) {
        final GenLayer[] wrappedLayers = new GenLayer[event.getNewBiomeGens().length];
        for(int i = 0; i < wrappedLayers.length; i++) {
            final GenLayer layer = event.getNewBiomeGens()[i];
            if(layer instanceof GenLayerVoronoiZoom) {
                (layer.parent = new GenLayerOceanBiomes(2, layer.parent)).initWorldGenSeed(event.getSeed());
                wrappedLayers[i] = layer;
            }

            else (wrappedLayers[i] = new GenLayerOceanBiomes(2, layer)).initWorldGenSeed(event.getSeed());
        }

        event.setNewBiomeGens(wrappedLayers);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    static void handleShallowOceanGen(@Nonnull final GetOceanForGenEvent event) {
        event.setOcean(OceanType.getTypeForTemperature(event.temperatureNoise).getRandomBiome(event::getRandomInt));
    }
}
