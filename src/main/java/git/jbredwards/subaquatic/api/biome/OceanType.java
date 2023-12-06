package git.jbredwards.subaquatic.api.biome;

import com.google.common.base.Preconditions;
import net.minecraft.init.Biomes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoublePredicate;
import java.util.function.IntUnaryOperator;

/**
 *
 * @since 1.3.0
 * @author jbred
 *
 */
public enum OceanType implements IStringSerializable
{
    DEFAULT("default", -1, t -> t <= 0.2 && t >= -0.2),
    FROZEN("frozen", 3, t -> t < -0.4),
    WARM("warm", 4, t -> t > 0.4),
    COLD("cold", 0, t -> t < -0.2),
    LUKEWARM("lukewarm", 0, t -> t > 0.2);

    @Nonnull final String name;
    @Nonnull DoublePredicate temperatureChecker;
    final int fallbackIndex;

    @Nonnull
    final List<BiomeManager.BiomeEntry> biomes = new ArrayList<>();
    int totalBiomeWeight = -1;

    OceanType(@Nonnull final String nameIn, final int fallbackIndexIn, @Nonnull final DoublePredicate temperatureCheckerIn) {
        name = nameIn;
        fallbackIndex = fallbackIndexIn;
        temperatureChecker = temperatureCheckerIn;
    }

    @Nonnull
    @Override
    public String getName() { return name; }

    /**
     * A general utility function that throws an exception if the provided biome is not an ocean biome.
     * @param biome the biome to check.
     * @return the provided biome if it's an ocean biome.
     */
    @Nonnull
    public static Biome ensureIsOcean(@Nonnull final Biome biome) {
        Preconditions.checkArgument(Preconditions.checkNotNull(biome).delegate != null, "Biome \"%s\" is not registered!", biome.biomeName);
        Preconditions.checkArgument(BiomeManager.oceanBiomes.contains(biome), "Biome \"%s\" is not an ocean biome!", biome.delegate.name());
        return biome;
    }

    // ==================
    // REGISTRY FUNCTIONS
    // ==================

    /**
     * Registers a provided biome into this ocean type, along with a provided weight.
     * <p>Before calling this method, ensure that the biome you pass in is registered as a forge ocean biome.
     * To do this, call {@code BiomeManager.oceanBiomes.add(biome)}.
     * <p><b>Important:</b> Deep oceans should not be registered here!
     *
     * @param biome the biome to register.
     * @param weight the biome's weight. The higher this value, the more likely the biome is to generate compared to other biomes registered in this ocean type.
     * @return true if the biome was successfully registered into this ocean type, false otherwise.
     */
    public boolean registerBiome(@Nonnull final Biome biome, final int weight) { return registerBiome(new BiomeManager.BiomeEntry(biome, weight)); }
    public boolean registerBiome(@Nonnull final BiomeManager.BiomeEntry biomeEntry) {
        if(!removeBiome(ensureIsOcean(biomeEntry.biome))) // if the biome is already present, remove its old weight
            totalBiomeWeight = -1; // marks the registry as dirty
        return biomes.add(biomeEntry);
    }

    /**
     * Removes the provided biome from this ocean type.
     * @param biome the biome to be removed.
     * @return true if the biome was found in this ocean type, false otherwise.
     */
    public boolean removeBiome(@Nonnull final Biome biome) {
        final boolean removed = biomes.removeIf(biomeEntry -> biomeEntry.biome == biome);
        if(removed) totalBiomeWeight = -1; // marks the registry as dirty
        return removed;
    }

    /**
     * Removes all the biomes from this ocean type.
     */
    public void clear() { biomes.clear(); }

    /**
     * @return all of this ocean type's registered biomes.
     */
    @Nonnull
    public List<BiomeManager.BiomeEntry> getBiomeEntries() { return biomes; }

    /**
     * Exposed in case a mod dev or modpack dev wants to change an ocean type's temperature range.
     * @param temperatureRange the new ocean type's valid temperature range.
     */
    public void setTemperatureRange(@Nonnull final DoublePredicate temperatureRange) {
        temperatureChecker = Preconditions.checkNotNull(temperatureRange);
    }

    // ===================
    // WORLD GEN FUNCTIONS
    // ===================

    @Nonnull
    public static OceanType getTypeForTemperature(final double temperature) {
        for(@Nonnull final OceanType type : values()) if(type.temperatureChecker.test(temperature)) return type;
        return DEFAULT;
    }

    @Nonnull
    public Biome getRandomBiome(@Nonnull final IntUnaryOperator rand) {
        if(fallbackIndex < 0) { if(biomes.isEmpty()) return Biomes.OCEAN; }
        else if(biomes.isEmpty()) return OceanType.values()[fallbackIndex].getRandomBiome(rand);

        if(biomes.size() == 1) return biomes.get(0).biome;
        final int totalWeight = totalBiomeWeight < 0 ? totalBiomeWeight = WeightedRandom.getTotalWeight(biomes) : totalBiomeWeight;
        return WeightedRandom.getRandomItem(biomes, rand.applyAsInt(totalWeight)).biome;
    }
}
