package git.jbredwards.subaquatic.mod.common.world.gen.layer;

import git.jbredwards.subaquatic.api.event.GetOceanForGenEvent;
import git.jbredwards.subaquatic.mod.common.init.ModBiomes;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public final class GenLayerOceanBiomes extends GenLayer
{
    static final int OCEAN = Biome.getIdForBiome(Biomes.OCEAN);
    static final int DEEP_OCEAN = Biome.getIdForBiome(Biomes.DEEP_OCEAN);

    public GenLayerOceanBiomes(long seed, @Nonnull GenLayer parentIn) {
        super(seed);
        parent = parentIn;
    }

    @Nonnull
    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        final int[] biomeInts = parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        final int[] out = IntCache.getIntCache(areaWidth * areaHeight);
        final boolean[] ocean = new boolean[areaWidth * areaHeight];

        for(int x = 0; x < areaWidth; x++) {
            for(int z = 0; z < areaHeight; z++) {

            }
        }

        return out;
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
