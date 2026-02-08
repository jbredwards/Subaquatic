/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.api.entity.bucketable;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.OptionalInt;

/**
 * Responsible for all bucketable entity behavior.
 *
 * @since 1.3.0
 * @author jbred
 *
 */
public interface BucketableEntityHandler<T extends Entity>
{
    /**
     * @return True if the entity can breathe while within the provided FluidStack.
     * @since 1.3.0
     */
    boolean breathable(@Nonnull final FluidStack fluidStack);

    /**
     * @return True if the entity is bucketable.
     * @throws NullPointerException If entity is null.
     * @since 1.3.0
     */
    boolean bucketable(@Nonnull final T entity);

    /**
     * @return The smallest bucket size able to store this handler, as described by
     * {@link BucketableEntityRegistry#BUCKET_REGISTRY}.
     * @since 1.3.0
     */
    int bucketSize();

    /**
     * Currently only used by Subaquatic's creative tab to properly sort all handlers.
     * @return The unique ID of this handler.
     * @since 1.3.0
     */
    int creationId();

    /**
     * Applies data from the bucket to the entity, when the entity is placed.
     * @throws NullPointerException If entity or compound are null.
     * @since 1.3.0
     */
    void read(@Nonnull final T entity, @Nonnull final NBTTagCompound compound);

    /**
     * Writes data from the entity to the bucket, when the entity is bucketed.
     * @throws NullPointerException If entity or compound are null.
     * @since 1.3.0
     */
    void write(@Nonnull final T entity, @Nonnull final NBTTagCompound compound);

    /**
     * @return The sound played when the entity is bucketed.
     * @throws NullPointerException If entity is null.
     * @since 1.3.0
     */
    @Nonnull
    SoundEvent sound(@Nonnull final T entity);

    /**
     * @return All NBT entity bucket subtypes, which is then merged with the item subtypes.
     * @since 1.3.0
     */
    @Nonnull
    List<NBTTagCompound> subtypes();

    /**
     * @return Additional tooltip info for the bucket item.
     * @throws NullPointerException If compound is null.
     * @since 1.3.0
     */
    @Nonnull
    @SideOnly(Side.CLIENT)
    List<String> tooltip(@Nonnull final NBTTagCompound compound, final boolean advanced);

    /**
     * @return An RGB color for the {@link net.minecraft.client.renderer.block.model.BakedQuad BakedQuad}
     * given its tintIndex, or empty for no color.
     * @throws NullPointerException If compound is null.
     * @since 1.3.0
     */
    @Nonnull
    @SideOnly(Side.CLIENT)
    OptionalInt overlayColor(@Nonnull final NBTTagCompound compound, final int tintIndex);

    /**
     * @return Location of the item model to be overlaid onto the bucket's item model.
     * @since 1.3.0
     */
    @Nonnull
    @SideOnly(Side.CLIENT)
    ModelResourceLocation overlayModel();
}
