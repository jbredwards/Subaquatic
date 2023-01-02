package git.jbredwards.subaquatic.mod.common.world.gen.layer;

import git.jbredwards.subaquatic.api.event.GetOceanForGenEvent;
import git.jbredwards.subaquatic.mod.common.init.ModBiomes;
import git.jbredwards.subaquatic.mod.common.world.gen.NoiseGeneratorOceans;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
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
        final int[] biomeInts = parent.getInts(areaX, areaZ, areaWidth, areaHeight);
        final int[] out = IntCache.getIntCache(areaWidth * areaHeight);

        //apply base ocean biomes
        for(int x = 0; x < areaWidth; x++) {
            for(int z = 0; z < areaHeight; z++) {
                final int biomeId = biomeInts[x + z * areaHeight];
                if(biomeId == OCEAN || biomeId == DEEP_OCEAN) {
                    final double temperature = temperatureGenerator.getValue((areaX + x) / 16f, (areaZ + z) / 16f, 0);
                    final GetOceanForGenEvent event = biomeId == OCEAN
                            ? new GetOceanForGenEvent.Shallow(temperature)
                            : new GetOceanForGenEvent.Deep(temperature);

                    MinecraftForge.TERRAIN_GEN_BUS.post(event);
                    out[x + z * areaHeight] = Biome.getIdForBiome(event.getOcean());
                }

                else out[x + z * areaHeight] = biomeId;
            }
        }

        //TODO mix ocean biomes
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
        if(event.temperatureNoise > 0.4) event.setOcean(ModBiomes.DEEP_WARM_OCEAN);
        else if(event.temperatureNoise > 0.2) event.setOcean(ModBiomes.DEEP_LUKEWARM_OCEAN);
        else if(event.temperatureNoise < -0.4) event.setOcean(ModBiomes.DEEP_FROZEN_OCEAN);
        else if(event.temperatureNoise < -0.2) event.setOcean(ModBiomes.DEEP_COLD_OCEAN);
    }
}
