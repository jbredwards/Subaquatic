/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.ai.task;

import git.jbredwards.subaquatic.mod.common.block.BlockTurtleEgg;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticItems;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public class EntityAIDestroyTurtleEggs extends EntityAIMoveToBlock
{
    protected int jumps;
    public EntityAIDestroyTurtleEggs(@Nonnull final EntityCreature creature, final double speedIn, final int length) {
        super(creature, speedIn, length);
    }

    @Override
    public boolean shouldExecute() {
        if(!creature.world.getGameRules().getBoolean("mobGriefing")) return false;
        else if(runDelay > 0) {
            --runDelay;
            return false;
        }
        else if(destinationBlock != null && shouldMoveTo(creature.world, destinationBlock) || searchForDestination()) {
            runDelay = 20;
            return true;
        }
        else {
            runDelay = 200 + creature.getRNG().nextInt(200);
            return false;
        }
    }

    @Override
    public void resetTask() {
        super.resetTask();
        creature.fallDistance = 1;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        jumps = 0;
    }

    @Override
    public void updateTask() {
        if(creature.getDistanceSqToCenter(destinationBlock.up()) > 1.4) {
            isAboveDestination = false;
            if(++timeoutCounter % 40 == 0) creature.getNavigator().tryMoveToXYZ(destinationBlock.getX() + 0.5, destinationBlock.getY() + 1, destinationBlock.getZ() + 0.5, movementSpeed);
        }
        else {
            isAboveDestination = true;
            timeoutCounter--;
        }


        @Nullable final BlockPos pos = roundPosToNearbyEgg(creature.world, new BlockPos(creature));
        if(pos != null && getIsAboveDestination()) {
            if(jumps > 0) {
                creature.motionY = 0.3;
                if(creature.world instanceof WorldServer) ((WorldServer)creature.world).spawnParticle(EnumParticleTypes.ITEM_CRACK, pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5, 3,
                        (creature.getRNG().nextDouble() - 0.5) * 0.08, (creature.getRNG().nextDouble() - 0.5) * 0.08, (creature.getRNG().nextDouble() - 0.5) * 0.08, 0.15, Item.getIdFromItem(SubaquaticItems.TURTLE_EGG));
            }

            if((jumps & 1) == 0) {
                creature.motionY = -0.3;
                if((jumps % 6) == 0) creature.world.playSound(null, pos, SubaquaticSounds.ENTITY_ZOMBIE_DESTROY_EGG, creature.getSoundCategory(), 0.5f, 0.9f + creature.getRNG().nextFloat() * 0.2f);
            }

            if(jumps > 60) {
                creature.world.destroyBlock(pos, false);
                if(creature.world instanceof WorldServer) ((WorldServer)creature.world).spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 20, 0.02, 0.02, 0.02, 0.15);
            }

            jumps++;
        }
    }

    @Nullable
    protected BlockPos roundPosToNearbyEgg(@Nonnull final World world, @Nonnull final BlockPos pos) {
        if(world.getBlockState(pos).getBlock() instanceof BlockTurtleEgg) return pos;
        for(@Nonnull final BlockPos neighborPos : new BlockPos[] { pos.down(), pos.west(), pos.east(), pos.north(), pos.south(), pos.down(2) })
            if(world.getBlockState(neighborPos).getBlock() instanceof BlockTurtleEgg) return neighborPos;

        return null;
    }

    @Override
    protected boolean shouldMoveTo(@Nonnull final World worldIn, @Nonnull final BlockPos pos) {
        return worldIn.isBlockLoaded(pos) && worldIn.getBlockState(pos).getBlock() instanceof BlockTurtleEgg && worldIn.isAirBlock(pos.up()) && worldIn.isAirBlock(pos.up(2));
    }

    @Override
    public boolean searchForDestination() {
        @Nonnull final BlockPos origin = new BlockPos(creature);
        @Nonnull final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for(int y = 0; y <= 3; y = y > 0 ? -y : 1 - y) {
            for(int length = 0; length < searchLength; ++length) {
                for(int x = 0; x <= length; x = x > 0 ? -x : 1 - x) {
                    for(int z = x < length && x > -length ? length : 0; z <= length; z = z > 0 ? -z : 1 - z) {
                        pos.setPos(origin.getX() + x, origin.getY() + y - 1, origin.getZ() + z);
                        if(creature.isWithinHomeDistanceFromPosition(pos) && shouldMoveTo(creature.world, pos)) {
                            destinationBlock = pos.toImmutable();
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
