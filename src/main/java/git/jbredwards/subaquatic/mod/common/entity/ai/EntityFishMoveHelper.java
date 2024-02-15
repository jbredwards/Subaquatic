/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.ai;

import git.jbredwards.subaquatic.mod.common.entity.living.AbstractFish;
import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class EntityFishMoveHelper extends EntityMoveHelper
{
    @Nonnull
    protected final AbstractFish fish;
    public EntityFishMoveHelper(@Nonnull AbstractFish fishIn) {
        super(fishIn);
        fish = fishIn;
    }

    @Override
    public void onUpdateMoveHelper() {
        if(fish.isInsideOfMaterial(Material.WATER)) fish.motionY += 0.005;
        if(action == Action.MOVE_TO && !fish.getNavigator().noPath()) {
            final double newSpeed = speed * fish.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
            fish.setAIMoveSpeed(fish.getAIMoveSpeed() + (float)(newSpeed - fish.getAIMoveSpeed()) * 0.125f);

            final double x = posX - fish.posX;
            final double y = posY - fish.posY;
            final double z = posZ - fish.posZ;

            if(y != 0) fish.motionY += fish.getAIMoveSpeed() * y / Math.sqrt(x * x + y * y + z * z) * 0.1;
            if(x != 0 || z != 0) {
                fish.rotationYaw = limitAngle(fish.rotationYaw, (float)(MathHelper.atan2(z, x) * 180 / Math.PI - 90), 90);
                fish.setRenderYawOffset(fish.rotationYaw);
            }
        }

        else fish.setAIMoveSpeed(0);
    }
}
