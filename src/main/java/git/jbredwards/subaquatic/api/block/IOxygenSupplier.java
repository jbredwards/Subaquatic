/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.api.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Blocks should implement this if they can supply oxygen to entities (even when fluidlogged).
 *
 * @since 1.1.0
 * @author jbred
 *
 */
public interface IOxygenSupplier
{
    /**
     * If this returns false, the entity's oxygen bar will start to tick down (like a player underwater).
     * If this returns true, the entity's oxygen bar will start to recover.
     */
    default boolean canSupplyOxygenTo(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase entity) {
        return true;
    }

    /**
     * The rate at which the entity recovers oxygen (called if {@link IOxygenSupplier#canSupplyOxygenTo} returns true).
     */
    default int getOxygenToSupply(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityLivingBase entity) {
        return 4;
    }
}
