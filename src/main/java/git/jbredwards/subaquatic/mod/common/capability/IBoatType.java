package git.jbredwards.subaquatic.mod.common.capability;

import git.jbredwards.fluidlogged_api.api.capability.CapabilityProvider;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.capability.util.BoatType;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticBoatTypesConfig;
import git.jbredwards.subaquatic.mod.common.entity.item.AbstractBoatContainer;
import git.jbredwards.subaquatic.mod.common.item.ItemBoatContainer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Used for both ItemStacks & EntityBoatContainers
 * @author jbred
 *
 */
@SuppressWarnings("ConstantConditions")
public interface IBoatType
{
    @CapabilityInject(IBoatType.class)
    @Nonnull Capability<IBoatType> CAPABILITY = null;
    @Nonnull ResourceLocation CAPABILITY_ID = new ResourceLocation(Subaquatic.MODID, "boat_type");

    @Nonnull
    BoatType getType();
    void setType(@Nonnull BoatType typeIn);

    @Nullable
    static IBoatType get(@Nullable ICapabilityProvider p) {
        return p != null && p.hasCapability(CAPABILITY, null) ? p.getCapability(CAPABILITY, null) : null;
    }

    @SubscribeEvent
    static void attachEntity(@Nonnull AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof AbstractBoatContainer)
            event.addCapability(CAPABILITY_ID, new CapabilityProvider<>(CAPABILITY));
    }

    @SubscribeEvent
    static void attachItem(@Nonnull AttachCapabilitiesEvent<ItemStack> event) {
        if(event.getObject().getItem() instanceof ItemBoatContainer)
            event.addCapability(CAPABILITY_ID, new CapabilityProvider<>(CAPABILITY));
    }

    class Impl implements IBoatType
    {
        @Nonnull
        protected BoatType type = BoatType.DEFAULT;

        @Nonnull
        @Override
        public BoatType getType() { return type; }

        @Override
        public void setType(@Nonnull BoatType typeIn) { type = typeIn; }
    }

    enum Storage implements Capability.IStorage<IBoatType>
    {
        INSTANCE;

        @Nonnull
        @Override
        public NBTBase writeNBT(@Nonnull Capability<IBoatType> capability, @Nonnull IBoatType instance, @Nullable EnumFacing side) {
            return instance.getType().serializeNBT();
        }

        @Override
        public void readNBT(@Nonnull Capability<IBoatType> capability, @Nonnull IBoatType instance, @Nullable EnumFacing side, @Nullable NBTBase nbt) {
            if(nbt instanceof NBTTagCompound) {
                final BoatType type = SubaquaticBoatTypesConfig.getTypeFrom((NBTTagCompound)nbt);
                if(type != null) instance.setType(type);
            }
        }
    }
}
