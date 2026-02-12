/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.ai.task;

import git.jbredwards.subaquatic.mod.common.entity.living.EntityTurtle;
import net.minecraft.entity.ai.EntityAIWander;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class EntityAITurtleWanderToLand extends EntityAIWander
{
    @Nonnull
    protected final EntityTurtle turtle;
    public EntityAITurtleWanderToLand(@Nonnull final EntityTurtle turtleIn, final double speedIn, final int chance) {
        super(turtleIn, speedIn, chance);
        turtle = turtleIn;
    }

    @Override
    public boolean shouldExecute() {
        return (!turtle.isInWater() && !turtle.isGoingHome() && !turtle.hasEgg()) && super.shouldExecute();
    }
}
