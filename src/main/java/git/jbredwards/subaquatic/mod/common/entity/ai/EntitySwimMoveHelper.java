/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class EntitySwimMoveHelper extends EntityMoveHelper
{
    protected final int maxTurnX, maxTurnY;
    protected final float inWaterSpeedModifier, outsideWaterSpeedModifier;
    protected final boolean applyGravity;

    public EntitySwimMoveHelper(@Nonnull EntityLiving entityLivingIn, int maxTurnXIn, int maxTurnYIn, float inWaterSpeedModifierIn, float outsideWaterSpeedModifierIn, boolean applyGravityIn) {
        super(entityLivingIn);
        maxTurnX = maxTurnXIn;
        maxTurnY = maxTurnYIn;
        inWaterSpeedModifier = inWaterSpeedModifierIn;
        outsideWaterSpeedModifier = outsideWaterSpeedModifierIn;
        applyGravity = applyGravityIn;
    }

    @Override
    public void onUpdateMoveHelper() {
        if(applyGravity && entity.isInWater()) entity.motionY += 0.005;
        if(action == Action.MOVE_TO && !entity.getNavigator().noPath()) {
            final double distX = posX - entity.posX;
            final double distY = posY - entity.posY;
            final double distZ = posZ - entity.posZ;

            if(distX * distX + distY * distY + distZ * distZ < 2.5000003E-7) {
                entity.setMoveForward(0);
                return;
            }

            entity.rotationYaw = limitAngle(entity.rotationYaw, (float)(MathHelper.atan2(distZ, distX) * 180 / Math.PI) - 90, maxTurnY);
            entity.renderYawOffset = entity.rotationYaw;
            entity.rotationYawHead = entity.rotationYaw;

            final float moveSpeed = (float)(speed * entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
            if(entity.isInWater()) {
                entity.setAIMoveSpeed(moveSpeed * inWaterSpeedModifier);

                final double distHorizontal = Math.sqrt(distX * distX + distZ * distZ);
                if(Math.abs(distY) > 1.0E-5 || distHorizontal > 1.0E-5) {
                    entity.rotationPitch = limitAngle(
                            entity.rotationPitch,
                            MathHelper.clamp((float)MathHelper.wrapDegrees(MathHelper.atan2(distY, distHorizontal) * -180 / Math.PI), -maxTurnX, maxTurnX),
                            5);
                }
            }

            else entity.setAIMoveSpeed(moveSpeed * outsideWaterSpeedModifier);
            return;
        }

        entity.setAIMoveSpeed(0);
        entity.setMoveStrafing(0);
        entity.setMoveVertical(0);
        entity.setMoveForward(0);
    }
}
