/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.ai.task;

import git.jbredwards.subaquatic.mod.common.block.BlockTurtleEgg;
import git.jbredwards.subaquatic.mod.common.entity.living.EntityTurtle;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class EntityAITurtleLayEgg extends EntityAIMoveToBlock
{
    @Nonnull
    protected final EntityTurtle turtle;
    public EntityAITurtleLayEgg(@Nonnull final EntityTurtle turtleIn, final double speedIn) {
        super(turtleIn, speedIn, 16);
        turtle = turtleIn;
    }

    @Override
    public boolean shouldExecute() {
        return turtle.hasEgg() && turtle.isWithinHomePos(81) && super.shouldExecute();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return turtle.hasEgg() && turtle.isWithinHomePos(81) && super.shouldContinueExecuting();
    }

    @Override
    public void updateTask() {
        super.updateTask();
        if(!turtle.isInWater() && getIsAboveDestination()) {
            if(!turtle.isDiggingSand()) {
                turtle.setDiggingSand(true);
                turtle.sandDiggingCounter = 0;
            }

            else if(++turtle.sandDiggingCounter > 200) {
                turtle.world.playSound(null, turtle.posX, turtle.posY, turtle.posZ, SubaquaticSounds.ENTITY_TURTLE_LAY_EGG, SoundCategory.BLOCKS, 0.3f, 0.9f + turtle.getRNG().nextFloat() * 0.2f);
                turtle.world.setBlockState(destinationBlock.up(), SubaquaticBlocks.TURTLE_EGG.getDefaultState().withProperty(BlockTurtleEgg.EGGS, turtle.getRNG().nextInt(4)));

                turtle.setHasEgg(false);
                turtle.setDiggingSand(false);
                turtle.inLove = 600;
            }

            if(turtle.hasEgg()) turtle.sandDiggingCounter++;
        }
    }

    @Override
    protected boolean shouldMoveTo(@Nonnull final World worldIn, @Nonnull final BlockPos pos) {
        return worldIn.isAirBlock(pos.up()) && turtle.isSand(worldIn.getBlockState(pos));
    }
}
