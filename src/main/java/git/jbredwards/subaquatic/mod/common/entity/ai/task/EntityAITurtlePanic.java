/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.ai.task;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public class EntityAITurtlePanic extends EntityAIPanic
{
    public EntityAITurtlePanic(@Nonnull final EntityCreature creature, final double speedIn) {
        super(creature, speedIn);
    }

    @Override
    public boolean shouldExecute() {
        if(creature.getRevengeTarget() == null && !creature.isBurning()) return false;

        @Nullable final BlockPos waterPos = getRandPos(creature.world, creature, 5, 4);
        if(waterPos != null) {
            randPosX = waterPos.getX();
            randPosY = waterPos.getY();
            randPosZ = waterPos.getZ();
            return true;
        }

        return findRandomPosition();
    }
}
