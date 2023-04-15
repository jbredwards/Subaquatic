package git.jbredwards.subaquatic.mod.common.entity.util;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.*;

/**
 *
 * @author jbred
 *
 */
@Immutable
public class FrogData implements Comparable<FrogData>
{
    @Nonnull
    public static final LinkedList<FrogData> VARIANTS = new LinkedList<>();

    @Nonnull public final String name;
    @Nonnull public final ResourceLocation texture;
    @Nonnull public final Set<Biome> validBiomes;

    public FrogData(@Nonnull String nameIn, @Nonnull ResourceLocation textureIn, @Nonnull Set<Biome> validBiomesIn) {
        name = nameIn;
        texture = textureIn;
        validBiomes = Collections.unmodifiableSet(validBiomesIn);
    }

    @Override
    public int compareTo(@Nonnull FrogData o) { return name.compareTo(o.name); }

    @Nonnull
    public static FrogData getFromName(@Nonnull String name) {
        for(FrogData data : VARIANTS) if(data.name.equals(name)) return data;
        return VARIANTS.getFirst();
    }

    @Nonnull
    public static FrogData getRandomForBiome(@Nonnull Biome biome, @Nonnull Random rand) {
        final List<FrogData> shuffled = new ArrayList<>(VARIANTS);
        Collections.shuffle(shuffled, rand); //shuffle biomes, so multiple frog variants can exist in one biome if desired

        for(FrogData data : shuffled) if(data.validBiomes.contains(biome)) return data;
        return VARIANTS.getFirst();
    }
}
