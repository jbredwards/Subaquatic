package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.world.biome.BiomeFrozenOcean;
import git.jbredwards.subaquatic.mod.common.world.biome.BiomeSandOcean;
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
    @Nonnull public static final BiomeSandOcean WARM_OCEAN = register("warm_ocean", new BiomeSandOcean(new Biome.BiomeProperties("Warm Ocean").setBaseHeight(-1).setHeightVariation(0.1f).setTemperature(0.5f).setRainfall(0.5f)));
    @Nonnull public static final BiomeSandOcean LUKEWARM_OCEAN = register("lukewarm_ocean", new BiomeSandOcean(new Biome.BiomeProperties("Lukewarm Ocean").setBaseHeight(-1).setHeightVariation(0.1f).setTemperature(0.5f).setRainfall(0.5f)));
    @Nonnull public static final BiomeSandOcean COLD_OCEAN = register("cold_ocean", new BiomeSandOcean(new Biome.BiomeProperties("Cold Ocean").setBaseHeight(-1).setHeightVariation(0.1f).setTemperature(0.5f).setRainfall(0.5f)));
    @Nonnull public static final BiomeSandOcean DEEP_WARM_OCEAN = register("deep_warm_ocean", new BiomeSandOcean(new Biome.BiomeProperties("Deep Warm Ocean").setBaseHeight(-1.8f).setHeightVariation(0.1f).setTemperature(0.5f).setRainfall(0.5f)));
    @Nonnull public static final BiomeSandOcean DEEP_LUKEWARM_OCEAN = register("deep_lukewarm_ocean", new BiomeSandOcean(new Biome.BiomeProperties("Deep Lukewarm Ocean").setBaseHeight(-1.8f).setHeightVariation(0.1f).setTemperature(0.5f).setRainfall(0.5f)));
    @Nonnull public static final BiomeSandOcean DEEP_COLD_OCEAN = register("deep_cold_ocean", new BiomeSandOcean(new Biome.BiomeProperties("Deep Cold Ocean").setBaseHeight(-1.8f).setHeightVariation(0.1f).setTemperature(0.5f).setRainfall(0.5f)));
    @Nonnull public static final BiomeFrozenOcean DEEP_FROZEN_OCEAN = register("deep_frozen_ocean", new BiomeFrozenOcean(new Biome.BiomeProperties("Deep Frozen Ocean").setBaseHeight(-1.8f).setHeightVariation(0.1f).setTemperature(0.5f).setRainfall(0.5f).setSnowEnabled()));

    // Biome Dictionary
    public static void registerBiomeDictionary() {
        BiomeDictionary.addTypes(WARM_OCEAN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.OCEAN);
        BiomeDictionary.addTypes(LUKEWARM_OCEAN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.OCEAN);
        BiomeDictionary.addTypes(COLD_OCEAN, BiomeDictionary.Type.COLD, BiomeDictionary.Type.OCEAN);
        BiomeDictionary.addTypes(DEEP_WARM_OCEAN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.OCEAN);
        BiomeDictionary.addTypes(DEEP_LUKEWARM_OCEAN, BiomeDictionary.Type.HOT, BiomeDictionary.Type.OCEAN);
        BiomeDictionary.addTypes(DEEP_COLD_OCEAN, BiomeDictionary.Type.COLD, BiomeDictionary.Type.OCEAN);
        BiomeDictionary.addTypes(DEEP_FROZEN_OCEAN, BiomeDictionary.Type.COLD, BiomeDictionary.Type.OCEAN, BiomeDictionary.Type.SNOWY);
    }

    //registry
    @Nonnull
    static <T extends Biome> T register(@Nonnull String name, @Nonnull T biome) {
        INIT.add(biome.setRegistryName(Subaquatic.MODID, name));
        return biome;
    }
}
