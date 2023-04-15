package git.jbredwards.subaquatic.mod.common.entity.util;

import net.minecraft.item.EnumDyeColor;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 *
 * @author jbred
 *
 */
@Immutable
public final class TropicalFishData
{
    @Nonnull
    public final EnumDyeColor primaryColor, secondaryColor;
    public final int primaryShape, secondaryShape;

    public TropicalFishData(int primaryShapeIn, @Nonnull EnumDyeColor primaryColorIn, int secondaryShapeIn, @Nonnull EnumDyeColor secondaryColorIn) {
        primaryShape = primaryShapeIn;
        primaryColor = primaryColorIn;
        secondaryShape = secondaryShapeIn;
        secondaryColor = secondaryColorIn;
    }

    @Nonnull
    public static TropicalFishData deserialize(int serialized) {
        return new TropicalFishData(serialized & 255, EnumDyeColor.byMetadata(serialized >> 16 & 255), serialized >> 8 & 255, EnumDyeColor.byMetadata(serialized >> 24 & 255));
    }

    public int serialize() {
        return primaryShape & 255 | (secondaryShape & 255) << 8 | (primaryColor.getMetadata() & 255) << 16 | (secondaryColor.getMetadata() & 255) << 24;
    }

    public boolean hasTranslation(@Nonnull Predicate<String> translator) {
        return translator.test("tooltip.subaquatic.fish_bucket.tropical_fish.special_type." + serialize());
    }

    @Nonnull
    public String getTranslatedName(@Nonnull BiFunction<String, Object[], String> translator) {
        return translator.apply("tooltip.subaquatic.fish_bucket.tropical_fish.special_type." + serialize(), new Object[0]);
    }

    @Nonnull
    public String getTranslatedShape(@Nonnull BiFunction<String, Object[], String> translator) {
        return translator.apply("tooltip.subaquatic.fish_bucket.tropical_fish.type." + (primaryShape & 255 | (secondaryShape & 255) << 8), new Object[0]);
    }

    @Nonnull
    public String getTranslatedColor(@Nonnull BiFunction<String, Object[], String> translator) {
        return translator.apply("tooltip.subaquatic.fish_bucket.tropical_fish.colors", new Object[] {
                translator.apply("color.subaquatic." + primaryColor.getTranslationKey(), new Object[0]),
                translator.apply("color.subaquatic." + secondaryColor.getTranslationKey(), new Object[0])});
    }
}
