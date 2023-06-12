package git.jbredwards.subaquatic.mod.common.world.gen.layer;

import git.jbredwards.subaquatic.api.event.GetOceanForGenEvent;
import git.jbredwards.subaquatic.mod.common.world.gen.NoiseGeneratorOceans;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class GenLayerOceanBiomeMask extends GenLayer
{
    @Nonnull
    protected final NoiseGeneratorOceans temperatureGenerator;
    public GenLayerOceanBiomeMask(long seed, @Nonnull NoiseGeneratorOceans temperatureGeneratorIn) {
        super(seed);
        temperatureGenerator = temperatureGeneratorIn;
    }

    @Nonnull
    @Override
    public int[] getInts(int areaX, int areaZ, int areaWidth, int areaHeight) {
        final int[] out = IntCache.getIntCache(areaWidth * areaHeight);
        for(int x = 0; x < areaWidth; x++) {
            for(int z = 0; z < areaHeight; z++) {
                final GetOceanForGenEvent event = new GetOceanForGenEvent(temperatureGenerator.getValue((areaX + x) / 8f, (areaZ + z) / 8f, 0));
                if(!MinecraftForge.TERRAIN_GEN_BUS.post(event)) out[x + z * areaHeight] = 0;
                else out[x + z * areaHeight] = Biome.getIdForBiome(event.getOcean());
            }
        }

        return out;
    }
}
