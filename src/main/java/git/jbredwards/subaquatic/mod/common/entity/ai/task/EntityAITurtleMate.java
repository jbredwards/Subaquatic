/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.ai.task;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.entity.living.EntityTurtle;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatList;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
@Mod.EventBusSubscriber(modid = Subaquatic.MODID)
public class EntityAITurtleMate extends EntityAIMate
{
    @Nonnull
    protected final EntityTurtle turtle;
    public EntityAITurtleMate(@Nonnull final EntityTurtle turtleIn, final double speedIn) {
        super(turtleIn, speedIn);
        turtle = turtleIn;
    }

    @Override
    public boolean shouldExecute() {
        return super.shouldExecute() && !turtle.hasEgg();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void spawnBaby(@Nonnull final BabyEntitySpawnEvent event) {
        if(event.getParentA() instanceof EntityTurtle) {
            @Nonnull final EntityTurtle turtle = (EntityTurtle)event.getParentA();
            @Nonnull final EntityTurtle targetMate = (EntityTurtle)event.getParentB();

            @Nullable final EntityPlayer causedByPlayer = event.getCausedByPlayer();
            if(causedByPlayer instanceof EntityPlayerMP) {
                causedByPlayer.addStat(StatList.ANIMALS_BRED);
                CriteriaTriggers.BRED_ANIMALS.trigger((EntityPlayerMP)causedByPlayer, turtle, targetMate, null); // Is this nullable?
            }

            // Reset the "inLove" state for the animals.
            turtle.setGrowingAge(6000);
            targetMate.setGrowingAge(6000);
            turtle.resetInLove();
            targetMate.resetInLove();

            // Spawn xp orbs.
            if(turtle.world.getGameRules().getBoolean("doMobLoot")) {
                turtle.world.spawnEntity(new EntityXPOrb(turtle.world, turtle.posX, turtle.posY, turtle.posZ, turtle.getRNG().nextInt(7) + 1));
            }

            // Delay child creation.
            turtle.setHasEgg(true);
            event.setChild(null);
        }
    }
}
