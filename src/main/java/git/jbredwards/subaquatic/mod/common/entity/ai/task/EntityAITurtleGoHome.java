/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.ai.task;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.mod.common.entity.living.EntityTurtle;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public class EntityAITurtleGoHome extends EntityAIBase
{
    @Nonnull
    protected final EntityTurtle turtle;
    protected final double speed;

    protected boolean stuck;
    protected int executingTicks;

    public EntityAITurtleGoHome(@Nonnull final EntityTurtle turtleIn, final double speedIn) {
        turtle = turtleIn;
        speed = speedIn;
    }

    @Override
    public boolean shouldExecute() {
        return !turtle.isChild() && (turtle.hasEgg() || turtle.getRNG().nextInt(700) == 0 && !turtle.isWithinHomePos(4096));
    }

    @Override
    public void startExecuting() {
        turtle.setGoingHome(true);
        stuck = false;
        executingTicks = 0;
    }

    @Override
    public void resetTask() {
        turtle.setGoingHome(false);
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !stuck && executingTicks <= 600 && !turtle.isWithinHomePos(49);
    }

    @Override
    public void updateTask() {
        final boolean close = turtle.isWithinHomePos(256);
        if(close) executingTicks++;
        if(turtle.getNavigator().noPath()) {
            @Nonnull final Vec3d travelPos = new Vec3d(turtle.homePos.getX() + 0.5, turtle.homePos.getY(), turtle.homePos.getZ() + 0.5);
            @Nullable Vec3d targetPos = RandomPositionGenerator.findRandomTargetBlockTowards(turtle, 16, 3, travelPos);

            if(targetPos == null) targetPos = RandomPositionGenerator.findRandomTargetBlockTowards(turtle, 8, 7, travelPos);
            if(targetPos != null && !close && FluidloggedUtils.getFluidState(turtle.world, new BlockPos(targetPos)).getMaterial() != Material.WATER)
                targetPos = RandomPositionGenerator.findRandomTargetBlockTowards(turtle, 16, 5, travelPos);

            if(targetPos == null) stuck = false;
            else turtle.getNavigator().tryMoveToXYZ(targetPos.x, targetPos.y, targetPos.z, speed);
        }
    }
}
