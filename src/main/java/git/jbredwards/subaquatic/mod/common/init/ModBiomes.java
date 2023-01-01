package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.api.biome.BiomeSubaquaticOcean;
import git.jbredwards.subaquatic.mod.Subaquatic;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jbred
 *
 */
public final class ModBiomes
{
    // Init
    @Nonnull public static final List<Biome> INIT = new ArrayList<>();

    // Biomes
    @Nonnull public static final BiomeSubaquaticOcean DEEP_FROZEN_OCEAN = register("deep_frozen_ocean", new BiomeSubaquaticOcean(new Biome.BiomeProperties("Deep Frozen Ocean").setBaseHeight(-1.8f).setHeightVariation(0.1f).setTemperature(0).setRainfall(0.5f).setSnowEnabled()));

    // Biome Dictionary
    public static void registerBiomeDictionary() {
        BiomeDictionary.addTypes(DEEP_FROZEN_OCEAN, BiomeDictionary.Type.COLD, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.SNOWY);
    }

    //registry
    @Nonnull
    static <T extends Biome> T register(@Nonnull String name, @Nonnull T biome) {
        INIT.add(biome.setRegistryName(Subaquatic.MODID, name));
        return biome;
    }
}
