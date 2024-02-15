/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.api.entity;

import net.minecraft.entity.Entity;

import javax.annotation.Nonnull;

/**
 * Whenever a villager or zombie villager spawns in the world, it chooses a random profession.
 * This interface serves as a way to give people more control of what professions can be randomly applied.
 *
 * @since 1.1.0
 * @author jbred
 *
 */
public interface IConditionalProfession
{
    boolean isProfessionApplicableTo(@Nonnull Entity entity);
}
