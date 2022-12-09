package git.jbredwards.subaquatic.common.init;

import git.jbredwards.subaquatic.Subaquatic;
import git.jbredwards.subaquatic.common.potion.PotionBase;
import net.minecraft.potion.Potion;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * stores all of this mod's potion effects
 * @author jbred
 *
 */
public final class ModPotions
{
    //potion init
    @Nonnull public static final List<Potion> INIT = new ArrayList<>();

    //potions
    @Nonnull public static final PotionBase CONDUIT = register("conduit", new PotionBase(false, 1950417));

    @Nonnull
    static <T extends PotionBase> T register(@Nonnull String name, boolean isBadEffect, int liquidColor, @Nonnull PotionSupplier<T> supplier) {
        return register(name, isBadEffect, liquidColor, supplier, potion -> {});
    }

    @Nonnull
    static <T extends PotionBase> T register(@Nonnull String name, boolean isBadEffect, int liquidColor, @Nonnull PotionSupplier<T> supplier, @Nonnull Consumer<T> consumer) {
        final T potion = supplier.get(new ResourceLocation(Constants.MODID, String.format("textures/potions/%s.png", name)), isBadEffect, liquidColor);
        INIT.add(potion.setRegistryName(Constants.MODID, name).setPotionName(String.format("%s.effect.%s", Constants.MODID, name)));
        consumer.accept(potion);
        return potion;
    }
}
