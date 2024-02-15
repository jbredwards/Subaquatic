/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.capability;

import git.jbredwards.fluidlogged_api.api.capability.CapabilityProvider;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket.AbstractEntityBucketHandler;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author jbred
 *
 */
@SuppressWarnings("ConstantConditions")
public interface IEntityBucket
{
    @CapabilityInject(IEntityBucket.class)
    @Nonnull Capability<IEntityBucket> CAPABILITY = null;
    @Nonnull ResourceLocation CAPABILITY_ID = new ResourceLocation(Subaquatic.MODID, "fish_bucket");

    @Nullable
    AbstractEntityBucketHandler getHandler();
    void setHandler(@Nullable AbstractEntityBucketHandler handlerIn);

    static boolean canItemHoldCapability(@Nonnull Item item) {
        return item == Items.WATER_BUCKET || item instanceof UniversalBucket;
    }

    static boolean canStackHoldEntity(@Nonnull ItemStack stack) {
        if(!canItemHoldCapability(stack.getItem())) return false;

        final IFluidHandlerItem handler = FluidUtil.getFluidHandler(stack);
        if(handler == null || handler.getTankProperties().length != 1) return false;

        final FluidStack fluid = handler.drain(Fluid.BUCKET_VOLUME, false);
        return fluid != null && fluid.amount >= Fluid.BUCKET_VOLUME && isFluidValid(fluid.getFluid());
    }

    static boolean isFluidValid(@Nullable Fluid fluid) {
        return fluid != null && fluid.canBePlacedInWorld() // && !fluid.isLighterThanAir()
                && fluid.getBlock().getDefaultState().getMaterial() == Material.WATER
                && !SubaquaticConfigHandler.FISH_BUCKET_FLUID_BLACKLIST.contains(fluid);
    }

    @Nullable
    static IEntityBucket get(@Nullable ICapabilityProvider provider) {
        return provider != null && provider.hasCapability(CAPABILITY, null) ? provider.getCapability(CAPABILITY, null) : null;
    }

    @SubscribeEvent
    static void attachCapability(@Nonnull AttachCapabilitiesEvent<ItemStack> event) {
        if(canItemHoldCapability(event.getObject().getItem())) event.addCapability(CAPABILITY_ID, new CapabilityProvider<>(CAPABILITY));
    }

    @Nonnull
    List<Item> validBuckets = new LinkedList<>();
    static List<Item> getValidBuckets() {
        if(validBuckets.isEmpty()) ForgeRegistries.ITEMS.forEach(item -> {
            if(canItemHoldCapability(item)) validBuckets.add(item);
        });

        return validBuckets;
    }

    class Impl implements IEntityBucket
    {
        @Nullable
        protected AbstractEntityBucketHandler handler;

        @Nullable
        @Override
        public AbstractEntityBucketHandler getHandler() { return handler; }

        @Override
        public void setHandler(@Nullable AbstractEntityBucketHandler handlerIn) { handler = handlerIn; }
    }

    enum Storage implements Capability.IStorage<IEntityBucket>
    {
        INSTANCE;

        @Nonnull
        @Override
        public NBTBase writeNBT(@Nonnull Capability<IEntityBucket> capability, @Nonnull IEntityBucket instance, @Nullable EnumFacing side) {
            return instance.getHandler() != null ? instance.getHandler().serializeNBT() : new NBTTagByte((byte)0);
        }

        @Override
        public void readNBT(@Nonnull Capability<IEntityBucket> capability, @Nonnull IEntityBucket instance, @Nullable EnumFacing side, @Nullable NBTBase nbt) {
            if(nbt instanceof NBTTagCompound) instance.setHandler(AbstractEntityBucketHandler.createFromNBT((NBTTagCompound)nbt));
        }
    }
}
