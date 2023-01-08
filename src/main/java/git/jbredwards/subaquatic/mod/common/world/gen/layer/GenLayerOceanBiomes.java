package git.jbredwards.subaquatic.mod.common.world.gen.layer;

import git.jbredwards.subaquatic.api.biome.IOceanBiome;
import git.jbredwards.subaquatic.api.event.GetOceanForGenEvent;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBiomes;
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
import javax.annotation.Nullable;
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
    static final int DEEP_FROZEN_OCEAN = Biome.getIdForBiome(SubaquaticBiomes.DEEP_FROZEN_OCEAN);

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
                        final GetOceanForGenEvent event = new GetOceanForGenEvent(
                                temperatureGenerator.getValue((areaX + x) / 8f, (areaZ + z) / 8f, 0));

                        if(!MinecraftForge.TERRAIN_GEN_BUS.post(event)) out[x + z * areaHeight] = OCEAN;
                        else out[x + z * areaHeight] = Biome.getIdForBiome(event.getOcean());
                    }
                }

                return out;
            }
        }, 6).getInts(areaX, areaZ, areaWidth, areaHeight);

        //re-apply old layer data to the main layer
        for(int i = 0; i < oldIntsCache.length; i++) {
            final int biomeId = oldIntsCache[i];
            if(biomeId == DEEP_OCEAN) out[i] = handleDeepOceanGen(Biome.getBiomeForId(out[i]));
            else if(biomeId != OCEAN) out[i] = biomeId;
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
    static void handleShallowOceanGen(@Nonnull GetOceanForGenEvent event) {
        if(event.temperatureNoise > 0.4) event.setOcean(SubaquaticBiomes.WARM_OCEAN);
        else if(event.temperatureNoise > 0.2) event.setOcean(SubaquaticBiomes.LUKEWARM_OCEAN);
        else if(event.temperatureNoise < -0.4) event.setOcean(Biomes.FROZEN_OCEAN);
        else if(event.temperatureNoise < -0.2) event.setOcean(SubaquaticBiomes.COLD_OCEAN);
    }
}
