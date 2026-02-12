/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.ai.task;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public class EntityAISwimTempt extends EntityAIBase
{
    @Nonnull protected final EntityAnimal animal;
    protected final double speed;

    @Nullable protected EntityPlayer temptingPlayer;
    protected int delayTemptCounter;

    public EntityAISwimTempt(@Nonnull final EntityAnimal animalIn, final double speedIn) {
        animal = animalIn;
        speed = speedIn;

        setMutexBits(Constants.AiMutexBits.MOVE | Constants.AiMutexBits.LOOK);
    }

    @Override
    public boolean shouldExecute() {
        if(delayTemptCounter > 0) {
            delayTemptCounter--;
            return false;
        }

        temptingPlayer = animal.world.getClosestPlayerToEntity(animal, 10);
        return temptingPlayer != null && (isTempting(temptingPlayer.getHeldItemMainhand()) || isTempting(temptingPlayer.getHeldItemOffhand()));
    }

    @Override
    public void resetTask() {
        temptingPlayer = null;
        animal.getNavigator().clearPath();
        delayTemptCounter = 100;
    }

    @Override
    public void updateTask() {
        if(temptingPlayer != null) {
            animal.getLookHelper().setLookPositionWithEntity(temptingPlayer, animal.getHorizontalFaceSpeed() + 20, animal.getVerticalFaceSpeed());

            if(animal.getDistanceSq(temptingPlayer) < 6.25) animal.getNavigator().clearPath();
            else animal.getNavigator().tryMoveToEntityLiving(temptingPlayer, speed);
        }
    }

    protected boolean isTempting(@Nonnull final ItemStack stack) {
        return !stack.isEmpty() && animal.isBreedingItem(stack);
    }
}
