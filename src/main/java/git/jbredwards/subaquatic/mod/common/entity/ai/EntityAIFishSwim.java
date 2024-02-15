/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.ai;

import git.jbredwards.subaquatic.mod.common.entity.living.AbstractFish;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class EntityAIFishSwim extends EntityAIWanderSwim
{
    @Nonnull
    protected final AbstractFish fish;
    public EntityAIFishSwim(@Nonnull AbstractFish fishIn, double speedIn, int chance) {
        super(fishIn, speedIn, chance);
        fish = fishIn;
    }

    @Override
    public boolean shouldExecute() { return fish.hasNoGroup() && super.shouldExecute(); }
}
