package git.jbredwards.subaquatic.mod.common.capability;

import git.jbredwards.fluidlogged_api.api.capability.CapabilityProvider;
import git.jbredwards.subaquatic.mod.Subaquatic;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;
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
 *
 * @author jbred
 *
 */
@SuppressWarnings("ConstantConditions")
public interface IBubbleColumn
{
    @CapabilityInject(IBubbleColumn.class)
    @Nonnull Capability<IBubbleColumn> CAPABILITY = null;
    @Nonnull ResourceLocation CAPABILITY_ID = new ResourceLocation(Subaquatic.MODID, "bubble_column");

    boolean isInBubbleColumn();
    void setInBubbleColumn(boolean isInBubbleColumnIn);

    @Nullable
    static IBubbleColumn get(@Nullable ICapabilityProvider provider) {
        return provider.hasCapability(CAPABILITY, null) ? provider.getCapability(CAPABILITY, null) : null;
    }

    @SubscribeEvent
    static void attach(@Nonnull AttachCapabilitiesEvent<Entity> event) {
        event.addCapability(CAPABILITY_ID, new CapabilityProvider<>(CAPABILITY));
    }

    class Impl implements IBubbleColumn
    {
        protected boolean isInBubbleColumn;

        @Override
        public boolean isInBubbleColumn() { return isInBubbleColumn; }

        @Override
        public void setInBubbleColumn(boolean isInBubbleColumnIn) { isInBubbleColumn = isInBubbleColumnIn; }
    }

    enum Storage implements Capability.IStorage<IBubbleColumn>
    {
        INSTANCE;

        @Nonnull
        @Override
        public NBTBase writeNBT(@Nonnull Capability<IBubbleColumn> capability, @Nonnull IBubbleColumn instance, @Nullable EnumFacing side) {
            return new NBTTagByte(instance.isInBubbleColumn() ? (byte)1 : 0);
        }

        @Override
        public void readNBT(@Nonnull Capability<IBubbleColumn> capability, @Nonnull IBubbleColumn instance, @Nullable EnumFacing side, @Nonnull NBTBase nbt) {
            if(nbt instanceof NBTPrimitive) instance.setInBubbleColumn(((NBTPrimitive)nbt).getByte() == 1);
        }
    }
}
