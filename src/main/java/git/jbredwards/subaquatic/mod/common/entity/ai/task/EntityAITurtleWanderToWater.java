/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.ai.task;

import git.jbredwards.fluidlogged_api.api.util.FluidState;
import git.jbredwards.subaquatic.mod.common.entity.living.EntityTurtle;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class EntityAITurtleWanderToWater extends EntityAIMoveToBlock
{
    @Nonnull
    protected final EntityTurtle turtle;
    public EntityAITurtleWanderToWater(@Nonnull final EntityTurtle turtleIn, final double speedIn) {
        super(turtleIn, speedIn, 24);
        turtle = turtleIn;
    }

    @Override
    public boolean shouldExecute() {
        if(!creature.isInWater() && creature.isChild()) return super.shouldExecute();
        else return !creature.isInWater() && !turtle.isGoingHome() && !turtle.hasEgg() && super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !turtle.isInWater() && timeoutCounter <= 1200 && shouldMoveTo(creature.world, destinationBlock);
    }

    @Override
    public void startExecuting() {
        creature.getNavigator().tryMoveToXYZ(destinationBlock.getX() + 0.5, destinationBlock.getY() + 1, destinationBlock.getZ() + 0.5, getMovementSpeed());
        timeoutCounter = 0;
    }

    @Override
    public void updateTask() {
        if(creature.getDistanceSqToCenter(destinationBlock.up()) > 1) {
            isAboveDestination = false;
            timeoutCounter++;

            if(timeoutCounter % 160 == 0) creature.getNavigator().tryMoveToXYZ(destinationBlock.getX() + 0.5, destinationBlock.getY() + 1, destinationBlock.getZ() + 0.5, getMovementSpeed());
        }

        else {
            isAboveDestination = true;
            timeoutCounter--;
        }
    }

    @Override
    protected boolean shouldMoveTo(@Nonnull final World worldIn, @Nonnull final BlockPos pos) {
        @Nonnull final IBlockState state = worldIn.getBlockState(pos);
        return state.getMaterial() == Material.WATER || state.getCollisionBoundingBox(worldIn, pos) == null && FluidState.get(worldIn, pos).getMaterial() == Material.WATER;
    }

    protected double getMovementSpeed() {
        return creature.isChild() ? movementSpeed * 2 : movementSpeed;
    }
}
