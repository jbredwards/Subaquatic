package git.jbredwards.subaquatic.mod.common.capability;

import git.jbredwards.fluidlogged_api.api.capability.CapabilityProvider;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.client.item.model.BakedFishBucketModel;
import git.jbredwards.subaquatic.mod.common.capability.util.FishBucketData;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
        return item instanceof ItemBucket || item instanceof UniversalBucket;
    }

    static boolean canStackHoldFish(@Nonnull ItemStack stack) {
        if(!canItemHoldCapability(stack.getItem())) return false;

        final IFluidHandlerItem handler = FluidUtil.getFluidHandler(stack);
        if(handler == null) return false;

        final FluidStack fluid = handler.drain(new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME), false);
        return fluid != null && fluid.getFluid() == FluidRegistry.WATER && fluid.amount >= Fluid.BUCKET_VOLUME;
    }

    @Nullable
    static IFishBucket get(@Nullable ICapabilityProvider provider) {
        return provider != null && provider.hasCapability(CAPABILITY, null) ? provider.getCapability(CAPABILITY, null) : null;
    }

    @SubscribeEvent
    static void attachCapability(@Nonnull AttachCapabilitiesEvent<ItemStack> event) {
        if(canItemHoldCapability(event.getObject().getItem())) event.addCapability(CAPABILITY_ID, new CapabilityProvider<>(CAPABILITY));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void applyFishBucketModelOverrides(@Nonnull ModelBakeEvent event) {
        for(Item item : ForgeRegistries.ITEMS) {
            if(canItemHoldCapability(item)) {
                for(String variant : event.getModelLoader().getVariantNames(item)) {
                    final ModelResourceLocation modelLocation = ModelLoader.getInventoryVariant(variant);
                    final IBakedModel bucketModel = event.getModelRegistry().getObject(modelLocation);
                    if(bucketModel != null) event.getModelRegistry().putObject(modelLocation, new BakedFishBucketModel(bucketModel));
                }
            }
        }
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
