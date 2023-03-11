package git.jbredwards.subaquatic.mod.common.capability;

import git.jbredwards.fluidlogged_api.api.capability.CapabilityProvider;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.capability.util.FishBucketData;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
@SuppressWarnings("ConstantConditions")
public interface IFishBucket
{
    @CapabilityInject(IFishBucket.class)
    @Nonnull Capability<IFishBucket> CAPABILITY = null;
    @Nonnull ResourceLocation CAPABILITY_ID = new ResourceLocation(Subaquatic.MODID, "fish_bucket");

    @Nonnull
    FishBucketData getData();
    void setData(@Nonnull FishBucketData dataIn);

    static boolean canItemHoldCapability(@Nonnull Item item) {
        return item == Items.WATER_BUCKET || item instanceof UniversalBucket;
    }

    static boolean canStackHoldFish(@Nonnull ItemStack stack) {
        if(!canItemHoldCapability(stack.getItem())) return false;

        final IFluidHandlerItem handler = FluidUtil.getFluidHandler(stack);
        if(handler == null || handler.getTankProperties().length != 1) return false;

        final FluidStack fluid = handler.drain(Fluid.BUCKET_VOLUME, false);
        return fluid != null && fluid.getFluid() != null
                && fluid.amount >= Fluid.BUCKET_VOLUME && fluid.getFluid().canBePlacedInWorld()
                && fluid.getFluid().getBlock().getDefaultState().getMaterial() == Material.WATER
                && !SubaquaticConfigHandler.FISH_BUCKET_FLUID_BLACKLIST.contains(fluid.getFluid());
    }

    @Nullable
    static IFishBucket get(@Nullable ICapabilityProvider provider) {
        return provider != null && provider.hasCapability(CAPABILITY, null) ? provider.getCapability(CAPABILITY, null) : null;
    }

    @SubscribeEvent
    static void attachCapability(@Nonnull AttachCapabilitiesEvent<ItemStack> event) {
        if(canItemHoldCapability(event.getObject().getItem())) event.addCapability(CAPABILITY_ID, new CapabilityProvider<>(CAPABILITY));
    }

    class Impl implements IFishBucket
    {
        @Nonnull
        protected FishBucketData data = FishBucketData.EMPTY;

        @Nonnull
        @Override
        public FishBucketData getData() { return data; }

        @Override
        public void setData(@Nonnull FishBucketData dataIn) { data = dataIn; }
    }

    enum Storage implements Capability.IStorage<IFishBucket>
    {
        INSTANCE;

        @Nullable
        @Override
        public NBTBase writeNBT(@Nonnull Capability<IFishBucket> capability, @Nonnull IFishBucket instance, @Nullable EnumFacing side) {
            return instance.getData().serializeNBT();
        }

        @Override
        public void readNBT(@Nonnull Capability<IFishBucket> capability, @Nonnull IFishBucket instance, @Nullable EnumFacing side, @Nullable NBTBase nbt) {
            if(nbt instanceof NBTTagCompound) instance.setData(FishBucketData.deserializeNBT((NBTTagCompound)nbt));
        }
    }
}
