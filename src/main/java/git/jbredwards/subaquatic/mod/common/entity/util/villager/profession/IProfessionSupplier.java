/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.util.villager.profession;

import net.minecraftforge.fml.common.registry.VillagerRegistry;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@FunctionalInterface
public interface IProfessionSupplier<T extends VillagerRegistry.VillagerProfession>
{
    @Nonnull
    T get(@Nonnull String name, @Nonnull String texture, @Nonnull String zombieTexture);
}
