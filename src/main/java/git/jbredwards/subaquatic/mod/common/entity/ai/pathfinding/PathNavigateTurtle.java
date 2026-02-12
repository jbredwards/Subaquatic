/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.ai.pathfinding;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.mod.common.entity.living.EntityTurtle;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class PathNavigateTurtle extends PathNavigateFish
{
    public PathNavigateTurtle(@Nonnull final EntityLiving entity, @Nonnull final World worldIn) {
        super(entity, worldIn);
    }

    @Override
    protected boolean canNavigate() {
        return true;
    }

    @Nonnull
    @Override
    protected PathFinder getPathFinder() {
        return new PathFinder(new WalkAndSwimNodeProcessor(false));
    }

    @Override
    public boolean canEntityStandOnPos(@Nonnull final BlockPos pos) {
        return entity instanceof EntityTurtle && ((EntityTurtle)entity).isTravelling()
                ? FluidloggedUtils.getFluidState(world, pos).getMaterial() == Material.WATER
                : !world.isAirBlock(pos.down());
    }
}
