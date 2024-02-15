/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.potion.PotionBase;
import net.minecraft.potion.Potion;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * stores all of this mod's potion effects
 * @author jbred
 *
 */
public final class SubaquaticPotions
{
    //potion init
    @Nonnull public static final List<Potion> INIT = new LinkedList<>();

    //potions
    @Nonnull public static final PotionBase CONDUIT = register("conduit", new PotionBase(false, 1950417));

    @Nonnull
    static <T extends Potion> T register(@Nonnull String name, @Nonnull T potion) {
        return register(name, potion, potionIn -> {});
    }

    @Nonnull
    static <T extends Potion> T register(@Nonnull String name, @Nonnull T potion, @Nonnull Consumer<T> consumer) {
        INIT.add(potion.setRegistryName(Subaquatic.MODID, name).setPotionName(String.format("%s.effect.%s", Subaquatic.MODID, name)));
        consumer.accept(potion);
        return potion;
    }
}
