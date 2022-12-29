package git.jbredwards.subaquatic.api.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeOcean;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Default implementation of IOceanBiome
 * @author jbred
 *
 */
public class BiomeSubaquaticOcean extends BiomeOcean implements IOceanBiome
{
    @Nullable
    protected final Biome deepOceanBiome; //null if this itself is a deep ocean biome
    protected int deepOceanBiomeIdCache;

    public BiomeSubaquaticOcean(@Nullable Biome deepOceanBiomeIn, @Nonnull BiomeProperties propertiesIn) {
        super(propertiesIn);
        deepOceanBiome = deepOceanBiomeIn;
    }

    @Override
    public int getDeepOceanBiomeId() {
        if(deepOceanBiome == null) return -1;
        else if(deepOceanBiomeIdCache != 0) return deepOceanBiomeIdCache;
        else return deepOceanBiomeIdCache = Biome.getIdForBiome(deepOceanBiome);
    }
}
