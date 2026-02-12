/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.ai;

import git.jbredwards.subaquatic.mod.common.entity.living.EntityTurtle;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class EntityTurtleMoveHelper extends EntityMoveHelper
{
    @Nonnull
    protected final EntityTurtle turtle;
    public EntityTurtleMoveHelper(@Nonnull final EntityTurtle turtleIn) {
        super(turtleIn);
        turtle = turtleIn;
    }

    @Override
    public void onUpdateMoveHelper() {
        // update water speed
        if(turtle.isInWater()) {
            turtle.motionY += 0.005;
            if(!turtle.isWithinHomePos(256)) turtle.setAIMoveSpeed(Math.max(turtle.getAIMoveSpeed() / 2, 0.08f));
            if(turtle.isChild()) turtle.setAIMoveSpeed(Math.max(turtle.getAIMoveSpeed() / 3, 0.06f));
        }

        // update ground speed
        else if(turtle.onGround) turtle.setAIMoveSpeed(Math.max(turtle.getAIMoveSpeed() / 2, 0.06f));

        // move to path
        if(action == Action.MOVE_TO && !turtle.getNavigator().noPath()) {
            final double distX = getX() - turtle.posX;
            final double distY = getY() - turtle.posY;
            final double distZ = getZ() - turtle.posZ;

            turtle.rotationYaw = limitAngle(turtle.rotationYaw, (float)MathHelper.atan2(distZ, distX) * 57.3f - 90, 90);
            turtle.renderYawOffset = turtle.rotationYaw;

            final float attributeSpeed = (float)(turtle.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * getSpeed());
            turtle.setAIMoveSpeed(turtle.getAIMoveSpeed() + 0.125f * (attributeSpeed - turtle.getAIMoveSpeed()));
            turtle.motionY += turtle.motionY * 0.1 * distY / Math.sqrt(distX * distX + distY * distY + distZ * distZ);
        }

        // idle
        else turtle.setAIMoveSpeed(0);
    }
}
