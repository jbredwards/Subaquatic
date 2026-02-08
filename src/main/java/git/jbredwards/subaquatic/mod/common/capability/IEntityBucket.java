/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.capability;

import git.jbredwards.fluidlogged_api.api.capability.CapabilityProvider;
import git.jbredwards.fluidlogged_api.api.util.FluidState;
import git.jbredwards.subaquatic.api.entity.bucketable.BucketableEntityHandler;
import git.jbredwards.subaquatic.api.entity.bucketable.BucketableEntityRegistry;
import git.jbredwards.subaquatic.api.entity.bucketable.IBucketableEntity;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 * @author jbred
 *
 */
public interface IEntityBucket
{
    @CapabilityInject(IEntityBucket.class)
    @Nonnull Capability<IEntityBucket> CAPABILITY = null;
    @Nonnull ResourceLocation CAPABILITY_ID = new ResourceLocation(Subaquatic.MODID, "fish_bucket");

    class Impl implements IEntityBucket
    {
        @Nonnull
        public final ItemStack stack;
        public Impl(@Nonnull final ItemStack stackIn) { stack = stackIn; }
    }

    enum Storage implements Capability.IStorage<IEntityBucket>
    {
        INSTANCE;

        @Nonnull
        @Override
        public NBTBase writeNBT(@Nonnull Capability<IEntityBucket> capability, @Nonnull IEntityBucket instance, @Nullable EnumFacing side) {
            return new NBTTagByte((byte)0);
        }

        @Override
        public void readNBT(@Nonnull Capability<IEntityBucket> capability, @Nonnull IEntityBucket instance, @Nullable EnumFacing side, @Nullable NBTBase nbt) {
            if(nbt instanceof NBTTagCompound) ((Impl)instance).stack.setTagInfo(BucketableEntityRegistry.NBT_ROOT, nbt.copy());
        }
    }

    enum StorageBucketable implements Capability.IStorage<IBucketableEntity>
    {
        INSTANCE;

        public static class Impl implements IBucketableEntity
        {
            public boolean isFromBucket;

            @Override
            public boolean isFromBucket() { return isFromBucket; }

            @Override
            public void setFromBucket(final boolean fromBucket) { isFromBucket = fromBucket; }
        }

        static class ImplWrapped implements IBucketableEntity
        {
            @Nonnull
            public final IBucketableEntity bucketable;
            public ImplWrapped(@Nonnull final Entity bucketableIn) { bucketable = (IBucketableEntity)bucketableIn; }

            @Override
            public boolean isFromBucket() { return bucketable.isFromBucket(); }

            @Override
            public void setFromBucket(final boolean fromBucket) { bucketable.setFromBucket(fromBucket); }
        }

        @Nonnull
        @Override
        public NBTBase writeNBT(@Nonnull final Capability<IBucketableEntity> capability, @Nonnull final IBucketableEntity instance, @Nullable final EnumFacing side) {
            return instance instanceof ImplWrapped ? new NBTTagString() : new NBTTagByte((byte)(instance.isFromBucket() ? 1 : 0));
        }

        @Override
        public void readNBT(@Nonnull final Capability<IBucketableEntity> capability, @Nonnull final IBucketableEntity instance, @Nullable final EnumFacing side, @Nullable final NBTBase nbt) {
            if(nbt instanceof NBTPrimitive) instance.setFromBucket(((NBTPrimitive)nbt).getByte() == 1);
        }

        @SubscribeEvent
        static void attachCapability(@Nonnull final AttachCapabilitiesEvent<Entity> event) {
            if(event.getObject() instanceof IBucketableEntity) event.addCapability(IBucketableEntity.CAPABILITY_ID, new CapabilityProvider<>(IBucketableEntity.CAPABILITY, new ImplWrapped(event.getObject())));
            else if(BucketableEntityRegistry.REGISTRY.containsKey(event.getObject().getClass())) event.addCapability(IBucketableEntity.CAPABILITY_ID, new CapabilityProvider<>(IBucketableEntity.CAPABILITY));
        }
    }

    /**
     * @return True if the bucket can hold the provided handler.
     */
    static boolean isBucketableHolder(@Nonnull final ItemStack bucket, @Nonnull final BucketableEntityHandler<?> handler) {
        return BucketableEntityRegistry.BUCKET_REGISTRY.getInt(bucket.getItem()) >= handler.bucketSize() // Entity can fit within bucket.
                && BucketableEntityRegistry.getHandler(bucket) == null // No entity already contained within bucket.
                && isFluidStackValid(FluidUtil.getFluidContained(bucket), handler::breathable); // Entity can breathe within contained fluid.
    }

    /**
     * @return True if the fluid can hold the provided handler.
     */
    static boolean isFluidStackValid(@Nullable final FluidStack fluidStack, @Nonnull final Predicate<FluidStack> breathable) {
        if(fluidStack == null || fluidStack.amount < Fluid.BUCKET_VOLUME) return false;
        else return fluidStack.getFluid().canBePlacedInWorld() && breathable.test(fluidStack);
    }

    /**
     * Use {@link BucketableEntityRegistry#BUCKET_REGISTRY} instead.
     */
    @Deprecated
    static boolean canItemHoldCapability(@Nonnull Item item) {
        return BucketableEntityRegistry.BUCKET_REGISTRY.containsKey(item);
    }

    /**
     * Use {@link IEntityBucket#isBucketableHolder} instead.
     */
    @Deprecated
    static boolean canStackHoldEntity(@Nonnull ItemStack stack) {
        return canItemHoldCapability(stack.getItem()) && isFluidStackValid(FluidUtil.getFluidContained(stack),
                fluidStack -> FluidState.of(fluidStack.getFluid()).getMaterial() == Material.WATER
                && !SubaquaticConfigHandler.FISH_BUCKET_FLUID_BLACKLIST.contains(fluidStack.getFluid()));
    }

    /**
     * Use {@link IEntityBucket#isFluidStackValid} instead.
     */
    @Deprecated
    static boolean isFluidValid(@Nullable Fluid fluid) {
        return fluid != null && isFluidStackValid(new FluidStack(fluid, Fluid.BUCKET_VOLUME),
                fluidStack -> FluidState.of(fluidStack.getFluid()).getMaterial() == Material.WATER
                && !SubaquaticConfigHandler.FISH_BUCKET_FLUID_BLACKLIST.contains(fluidStack.getFluid()));
    }

    /**
     * Use {@link BucketableEntityRegistry#BUCKET_REGISTRY} instead.
     */
    @Deprecated
    static List<Item> getValidBuckets() {
        return new LinkedList<>(BucketableEntityRegistry.BUCKET_REGISTRY.keySet());
    }

    /**
     * Apply {@link Storage} to remap old capability data.
     */
    @Deprecated
    @SubscribeEvent
    static void attachCapability(@Nonnull AttachCapabilitiesEvent<ItemStack> event) {
        if(canItemHoldCapability(event.getObject().getItem())) event.addCapability(CAPABILITY_ID, new CapabilityProvider<>(CAPABILITY, new Impl(event.getObject())));
    }
}
