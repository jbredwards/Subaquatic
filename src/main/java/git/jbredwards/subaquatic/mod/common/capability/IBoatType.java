package git.jbredwards.subaquatic.mod.common.capability;

import git.jbredwards.fluidlogged_api.api.capability.CapabilityProvider;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticChestBoatConfig;
import git.jbredwards.subaquatic.mod.common.entity.item.EntityBoatContainer;
import git.jbredwards.subaquatic.mod.common.item.ItemBoatContainer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;

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
    Pair<Item, ResourceLocation> getType();
    void setType(@Nonnull Pair<Item, ResourceLocation> typeIn);

    @Nullable
    static IBoatType get(@Nullable ICapabilityProvider p) {
        return p != null && p.hasCapability(CAPABILITY, null) ? p.getCapability(CAPABILITY, null) : null;
    }

    @SubscribeEvent
    static void attachEntity(@Nonnull AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof EntityBoatContainer)
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
        protected Pair<Item, ResourceLocation> type = Pair.of(Items.BOAT, new ResourceLocation(""));

        @Nonnull
        @Override
        public Pair<Item, ResourceLocation> getType() { return type; }

        @Override
        public void setType(@Nonnull Pair<Item, ResourceLocation> typeIn) { type = typeIn; }
    }

    enum Storage implements Capability.IStorage<IBoatType>
    {
        INSTANCE;

        @Nonnull
        @Override
        public NBTBase writeNBT(@Nonnull Capability<IBoatType> capability, @Nonnull IBoatType instance, @Nullable EnumFacing side) {
            return new NBTTagString(instance.getType().getKey().delegate.name().toString());
        }

        @Override
        public void readNBT(@Nonnull Capability<IBoatType> capability, @Nonnull IBoatType instance, @Nullable EnumFacing side, @Nullable NBTBase nbt) {
            if(nbt instanceof NBTTagString) {
                final Pair<Item, ResourceLocation> type = SubaquaticChestBoatConfig.getTypeFrom((NBTTagString)nbt);
                if(type != null) instance.setType(type);
            }
        }
    }
}
