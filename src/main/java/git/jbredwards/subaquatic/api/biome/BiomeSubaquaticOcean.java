package git.jbredwards.subaquatic.api.biome;

import net.minecraft.block.state.IBlockState;
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
    @Nonnull
    protected IBlockState surfaceBlock;

    @Nullable
    protected final Biome deepOceanBiome; //null if this itself is a deep ocean biome
    protected int deepOceanBiomeIdCache;

    public BiomeSubaquaticOcean(@Nullable Biome deepOceanBiomeIn, @Nonnull BiomeProperties propertiesIn) {
        super(propertiesIn);
        surfaceBlock = GRAVEL;
        deepOceanBiome = deepOceanBiomeIn;
        spawnableWaterCreatureList.clear();
    }

    @Nonnull
    @Override
    public IBlockState getOceanSurface() { return surfaceBlock; }

    @Nonnull
    @Override
    public Biome getMixOceanBiome() { return this; }

    @Override
    public int getDeepOceanBiomeId() {
        if(deepOceanBiomeIdCache != 0) return deepOceanBiomeIdCache;
        else return deepOceanBiomeIdCache = deepOceanBiome != null ? getIdForBiome(deepOceanBiome) : -1;
    }
}
