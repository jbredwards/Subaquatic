/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.ai;

import git.jbredwards.subaquatic.mod.common.entity.living.EntityPufferfish;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class EntityAIPuff extends EntityAIBase
{
    @Nonnull
    protected final EntityPufferfish entity;
    public EntityAIPuff(@Nonnull EntityPufferfish entityIn) { entity = entityIn; }

    @Override
    public boolean shouldExecute() {
        return !entity.world.getEntitiesWithinAABB(EntityLivingBase.class, entity.getEntityBoundingBox().grow(2), entity::canAttackEntity).isEmpty();
    }

    @Override
    public void startExecuting() {
        entity.deflateTimer = 0;
        entity.puffTimer = 1;
    }

    @Override
    public void resetTask() { entity.puffTimer = 0; }
}
