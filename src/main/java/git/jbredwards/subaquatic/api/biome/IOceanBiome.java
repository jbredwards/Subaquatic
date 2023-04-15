package git.jbredwards.subaquatic.api.biome;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager;

import javax.annotation.Nonnull;

/**
 * Custom ocean biomes don't have to implement this, but it's heavily recommended. Some reasons to implement this are:
 * <p>
 * -biomes that implement this will have their corresponding deep ocean biome generate
 * <p>
 * -biomes that implement this will have their corresponding mix ocean biome generate
 * <p>
 * -biomes that implement this will be automatically added to Forge's ocean biomes list
 * <p>
 * -biomes that implement this can have a custom surface block (other than gravel)
 * <p></p>
 *
 * @author jbred
 *
 */
public interface IOceanBiome extends IOceanSurfaceProvider
{
    /**
     * Returns -1 if this is a deep ocean.
     * This is called often during world gen, so it's recommended to return a constant instead of using Biome.getIdForBiome.
     */
    int getDeepOceanBiomeId();

    /**
     * Returns itself if this has no mix ocean biome.
     * Used to gradually transition from shore/beach biomes.
     */
    @Nonnull
    Biome getMixOceanBiome();

    /**
     * This is auto-generated at runtime, all ocean biomes are added.
     */
    @Nonnull
    IntSet OCEAN_IDS = new IntOpenHashSet();
    static boolean isOcean(int biome) { return OCEAN_IDS.contains(biome); }
    static boolean isOcean(@Nonnull Biome biome) { return BiomeManager.oceanBiomes.contains(biome); }

    /**
     * This is auto-generated at runtime, all ocean biomes that have their getDeepOceanBiomeId() not return -1 are added.
     */
    @Nonnull
    IntSet SHALLOW_OCEAN_IDS = new IntOpenHashSet(new int[] {0, 10});
    static boolean isShallowOcean(int biome) { return SHALLOW_OCEAN_IDS.contains(biome); }
    static boolean isShallowOcean(@Nonnull Biome biome) {
        if(biome instanceof IOceanBiome && ((IOceanBiome)biome).getDeepOceanBiomeId() != -1) return true;
        else return biome == Biomes.OCEAN; //vanilla special case
    }
}
