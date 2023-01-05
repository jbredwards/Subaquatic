package git.jbredwards.subaquatic.mod.common.world.gen.layer;

import git.jbredwards.subaquatic.api.event.GetOceanForGenEvent;
import git.jbredwards.subaquatic.mod.common.init.ModBiomes;
import git.jbredwards.subaquatic.mod.common.world.gen.NoiseGeneratorOceans;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerZoom;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.Arrays;
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

    private NoiseGeneratorOceans temperatureGenerator;
    public GenLayerOceanBiomes(long seed, @Nonnull GenLayer parentIn) {
        super(seed);
        parent = parentIn;
    }

    @Nonnull
    @Override
    public int[] getInts(int areaX, int areaZ, int areaWidth, int areaHeight) {
        final int[] oldInts = parent.getInts(areaX, areaZ, areaWidth, areaHeight);
        final int[] oldIntsCache = Arrays.copyOf(oldInts, oldInts.length);
        IntCache.resetIntCache();

        //create separate ocean biomes layer
        //this is merged into the main layer wherever the main layer has an ocean biome
        final int[] out = GenLayerZoom.magnify(2001, new GenLayer(baseSeed) {
            @Nonnull
            @Override
            public int[] getInts(int areaX, int areaZ, int areaWidth, int areaHeight) {
                final int[] out = IntCache.getIntCache(areaWidth * areaHeight);
                for(int x = 0; x < areaWidth; x++) {
                    for(int z = 0; z < areaHeight; z++) {
                        final double temperature = temperatureGenerator.getValue((areaX + x) / 8f, (areaZ + z) / 8f, 0);
                        final GetOceanForGenEvent event = new GetOceanForGenEvent.Shallow(temperature);

                        if(!MinecraftForge.TERRAIN_GEN_BUS.post(event)) out[x + z * areaHeight] = OCEAN;
                        else out[x + z * areaHeight] = Biome.getIdForBiome(event.getOcean());
                    }
                }

                return out;
            }
        }, 1).getInts(areaX, areaZ, areaWidth, areaHeight);

        //re-apply old layer data to the main layer
        for(int i = 0; i < oldIntsCache.length; i++) {
            final int biomeId = oldIntsCache[i];
            if(biomeId == DEEP_OCEAN) { //convert modded ocean biomes to their deep ocean counterparts
                final GetOceanForGenEvent event = new GetOceanForGenEvent.Deep(Biome.getBiome(out[i], Biomes.DEEP_OCEAN));
                if(MinecraftForge.TERRAIN_GEN_BUS.post(event)) out[i] = Biome.getIdForBiome(event.getOcean());
            }

            else if(biomeId != OCEAN) out[i] = biomeId;
        }

        return out;
    }

    @Override
    public void initWorldGenSeed(long seed) {
        super.initWorldGenSeed(seed);
        temperatureGenerator = new NoiseGeneratorOceans(new Random(seed));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void handleShallowOceanGen(@Nonnull GetOceanForGenEvent.Shallow event) {
        if(event.temperatureNoise > 0.4) event.setOcean(ModBiomes.WARM_OCEAN);
        else if(event.temperatureNoise > 0.2) event.setOcean(ModBiomes.LUKEWARM_OCEAN);
        else if(event.temperatureNoise < -0.4) event.setOcean(Biomes.FROZEN_OCEAN);
        else if(event.temperatureNoise < -0.2) event.setOcean(ModBiomes.COLD_OCEAN);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void handleDeepOceanGen(@Nonnull GetOceanForGenEvent.Deep event) {
        if(event.startingOcean == ModBiomes.WARM_OCEAN) event.setOcean(ModBiomes.DEEP_WARM_OCEAN);
        else if(event.startingOcean == ModBiomes.LUKEWARM_OCEAN) event.setOcean(ModBiomes.DEEP_LUKEWARM_OCEAN);
        else if(event.startingOcean == Biomes.FROZEN_OCEAN) event.setOcean(ModBiomes.DEEP_FROZEN_OCEAN);
        else if(event.startingOcean == ModBiomes.COLD_OCEAN) event.setOcean(ModBiomes.DEEP_COLD_OCEAN);
    }
}
