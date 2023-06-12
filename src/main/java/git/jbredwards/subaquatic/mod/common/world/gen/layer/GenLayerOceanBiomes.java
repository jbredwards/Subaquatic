package git.jbredwards.subaquatic.mod.common.world.gen.layer;

import git.jbredwards.subaquatic.api.biome.IOceanBiome;
import git.jbredwards.subaquatic.api.event.GetOceanForGenEvent;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBiomes;
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
    static final int OCEAN = Biome.getIdForBiome(Biomes.OCEAN);
    static final int DEEP_OCEAN = Biome.getIdForBiome(Biomes.DEEP_OCEAN);
    static final int DEEP_FROZEN_OCEAN = Biome.getIdForBiome(SubaquaticBiomes.DEEP_FROZEN_OCEAN);

    @Nonnull
    private final GenLayer wrapped;
    private NoiseGeneratorOceans temperatureGenerator;

    public GenLayerOceanBiomes(long seed, @Nonnull GenLayer wrappedIn) {
        super(seed);
        wrapped = wrappedIn;
    }

    @Nonnull
    @Override
    public int[] getInts(int areaX, int areaZ, int areaWidth, int areaHeight) {
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
                if(biomeId == DEEP_OCEAN) out[x + z * areaWidth] = handleDeepOceanGen(Biome.getBiomeForId(out[x + z * areaWidth]));
                //re-apply old layer data to the main layer
                else if(biomeId != OCEAN) out[x + z * areaWidth] = biomeId;
            }
        }

        return out;
    }

    //seed to test ocean gen: -4426319367184787986
    @Override
    public void initWorldGenSeed(long seed) {
        super.initWorldGenSeed(seed);
        temperatureGenerator = new NoiseGeneratorOceans(new Random(seed));
    }

    static int handleDeepOceanGen(@Nullable Biome shallowOcean) {
        //modded ocean biomes
        if(shallowOcean instanceof IOceanBiome) {
            final int deepOcean = ((IOceanBiome)shallowOcean).getDeepOceanBiomeId();
            if(deepOcean != -1) return deepOcean;
        }

        //vanilla ocean biomes
        return shallowOcean == Biomes.FROZEN_OCEAN ? DEEP_FROZEN_OCEAN : DEEP_OCEAN;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void handleGenLayerWrappers(@Nonnull WorldTypeEvent.InitBiomeGens event) {
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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void handleShallowOceanGen(@Nonnull GetOceanForGenEvent event) {
        if(event.temperatureNoise > 0.4) event.setOcean(SubaquaticBiomes.WARM_OCEAN);
        else if(event.temperatureNoise > 0.2) event.setOcean(SubaquaticBiomes.LUKEWARM_OCEAN);
        else if(event.temperatureNoise < -0.4) event.setOcean(Biomes.FROZEN_OCEAN);
        else if(event.temperatureNoise < -0.2) event.setOcean(SubaquaticBiomes.COLD_OCEAN);
    }
}
