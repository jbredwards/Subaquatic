/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.api.entity;

/**
 * Entities should implement this if they recover air differently than default vanilla 1.13.
 *
 * @since 1.1.0
 * @author jbred
 *
 */
public interface INextAirEntity
{
    /**
     * Serves as the counterpart to {@link net.minecraft.entity.EntityLivingBase#decreaseAirSupply EntityLivingBase::decreaseAirSupply}.
     */
    int increaseAirSupply(int totalAir, int airToGain, boolean instantReplenish);
}
