package git.jbredwards.subaquatic.mod.common.entity.util;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.text.translation.I18n;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
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

    public boolean hasTranslationKey() {
        return I18n.canTranslate("misc.subaquatic.tropical_fish.type." + (primaryShape & 255 | (secondaryShape & 255) << 8));
    }

    @Nonnull
    public String getLocalizedName() {
        return I18n.translateToLocal("misc.subaquatic.tropical_fish.type." + (primaryShape & 255 | (secondaryShape & 255) << 8));
    }

    @Nonnull
    public String getPrimaryLocalizedName() {
        return I18n.translateToLocal("misc.subaquatic.tropical_fish.type." + ((primaryShape & 255) | 65536));
    }

    @Nonnull
    public String getSecondaryLocalizedName() {
        return I18n.translateToLocal("misc.subaquatic.tropical_fish.type." + ((secondaryShape & 255) | 16777216));
    }
}
