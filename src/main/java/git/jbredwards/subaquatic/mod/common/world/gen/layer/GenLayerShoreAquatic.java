package git.jbredwards.subaquatic.mod.common.world.gen.layer;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerShore;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This overrides vanilla's class at runtime
 * @author jbred
 *
 */
public class GenLayerShoreAquatic extends GenLayerShore
{
    public GenLayerShoreAquatic(long seed, @Nonnull GenLayer parent) { super(seed, parent); }

    @Nonnull
    @Override
    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        final int[] parentInts = parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        final int[] biomeNoise = IntCache.getIntCache(areaWidth * areaHeight);
        for(int x = 0; x < areaWidth; x++) {
            for(int z = 0; z < areaHeight; z++) {

            }
        }

        return biomeNoise;
    }

    protected boolean isJungleCompatibleWith(int biomeId) {
        final @Nullable Biome biome = Biome.getBiomeForId(biomeId);
        if(biome == null) return false;

        return BiomeDictionary.hasType(biome, BiomeDictionary.Type.JUNGLE)
                || biome == Biomes.FOREST || biome == Biomes.TAIGA
                || BiomeManager.oceanBiomes.contains(biome);
    }

    protected boolean isMesa(int biomeId) {
        final @Nullable Biome biome = Biome.getBiomeForId(biomeId);
        return biome != null && BiomeDictionary.hasType(biome, BiomeDictionary.Type.MESA);
    }
}
