/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.ai.task;

import git.jbredwards.subaquatic.mod.common.entity.living.EntityTurtle;
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
public class EntityAITurtleTravel extends EntityAIBase
{
    @Nonnull
    protected final EntityTurtle turtle;
    protected final double speed;

    protected boolean stuck;

    public EntityAITurtleTravel(@Nonnull final EntityTurtle turtleIn, final double speedIn) {
        turtle = turtleIn;
        speed = speedIn;
    }

    @Override
    public boolean shouldExecute() {
        return !turtle.isGoingHome() && !turtle.hasEgg() && turtle.isInWater();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !turtle.getNavigator().noPath() && !stuck && turtle.isGoingHome() && !turtle.isInLove() && !turtle.hasEgg();
    }

    @Override
    public void startExecuting() {
        final int x = turtle.getRNG().nextInt(1025) - 512;
        final int z = turtle.getRNG().nextInt(1025) - 512;

        int y = turtle.getRNG().nextInt(9) - 4;
        if(y + turtle.posY >= turtle.world.getSeaLevel()) y = 0;

        turtle.travelPos = new BlockPos(turtle.posX + x, turtle.posY + y, turtle.posZ + z);
        turtle.setTravelling(true);
        stuck = false;
    }

    @Override
    public void updateTask() {
        if(turtle.getNavigator().noPath()) {
            @Nonnull final Vec3d travelPos = new Vec3d(turtle.travelPos.getX() + 0.5, turtle.travelPos.getY(), turtle.travelPos.getZ() + 0.5);
            @Nullable Vec3d targetPos = RandomPositionGenerator.findRandomTargetBlockTowards(turtle, 16, 3, travelPos);

            if(targetPos == null) targetPos = RandomPositionGenerator.findRandomTargetBlockTowards(turtle, 8, 7, travelPos);
            if(targetPos != null && !turtle.world.isAreaLoaded(new BlockPos(targetPos), 34)) targetPos = null;

            if(targetPos == null) stuck = true;
            else turtle.getNavigator().tryMoveToXYZ(targetPos.x, targetPos.y, targetPos.z, speed);
        }
    }

    @Override
    public void resetTask() {
        turtle.setTravelling(false);
        super.resetTask();
    }
}
