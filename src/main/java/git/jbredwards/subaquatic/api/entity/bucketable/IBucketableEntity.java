/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.api.entity.bucketable;

import git.jbredwards.subaquatic.mod.Subaquatic;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import javax.annotation.Nonnull;

/**
 * A capability applied automatically to all bucketable entities.
 *
 * @since 1.3.0
 * @author jbred
 *
 */
public interface IBucketableEntity
{
    @CapabilityInject(IBucketableEntity.class)
    @Nonnull Capability<IBucketableEntity> CAPABILITY = null;
    @Nonnull ResourceLocation CAPABILITY_ID = new ResourceLocation(Subaquatic.MODID, "bucketable_entity");

    /**
     * @return True if this entity was placed using a bucket.
     * @since 1.3.0
     */
    boolean isFromBucket();

    /**
     * Setter for {@link IBucketableEntity#isFromBucket}.
     * @since 1.3.0
     */
    void setFromBucket(final boolean fromBucket);
}
