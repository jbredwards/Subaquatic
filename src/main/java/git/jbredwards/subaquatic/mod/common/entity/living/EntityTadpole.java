/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.living;

import git.jbredwards.subaquatic.mod.common.entity.ai.EntitySwimLookHelper;
import git.jbredwards.subaquatic.mod.common.entity.ai.EntitySwimMoveHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class EntityTadpole extends AbstractFish
{
    protected int age;
    public EntityTadpole(@Nonnull World worldIn) {
        super(worldIn);
        moveHelper = new EntitySwimMoveHelper(this, 85, 10, 0.02f, 0.1f, true);
        lookHelper = new EntitySwimLookHelper(this, 10);
        experienceValue = 0;
    }

    @Nonnull
    @Override
    protected PathNavigate createNavigator(@Nonnull World worldIn) { return new PathNavigateSwimmer(this, worldIn); }

    @Override
    protected void initEntityAI() {
        tasks.addTask(0, new EntityAIPanic(this, 2));
        tasks.addTask(1, new EntityAILookIdle(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6);
        getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(1);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        //if(!world.isRemote) setAgeAndGrow(age + 1);
    }

    @Nonnull
    @Override
    protected SoundEvent getAmbientSound() {
        return super.getAmbientSound();
    }

    @Nonnull
    @Override
    protected SoundEvent getDeathSound() {
        return super.getDeathSound();
    }

    @Nonnull
    @Override
    protected SoundEvent getFlopSound() {
        return null;
    }

    @Nonnull
    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSourceIn) {
        return super.getHurtSound(damageSourceIn);
    }
}
