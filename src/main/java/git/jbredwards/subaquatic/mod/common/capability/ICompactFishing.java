/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.capability;

import git.jbredwards.fluidlogged_api.api.capability.CapabilityProvider;
import git.jbredwards.subaquatic.mod.Subaquatic;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagInt;
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
public interface ICompactFishing
{
    @CapabilityInject(ICompactFishing.class)
    @Nonnull Capability<ICompactFishing> CAPABILITY = null;
    @Nonnull ResourceLocation CAPABILITY_ID = new ResourceLocation(Subaquatic.MODID, "compact_fishing");

    int getLevel();
    void setLevel(int levelIn);

    @Nullable
    static ICompactFishing get(@Nullable ICapabilityProvider provider) {
        return provider != null && provider.hasCapability(CAPABILITY, null) ? provider.getCapability(CAPABILITY, null) : null;
    }

    @SubscribeEvent
    static void attachCapability(@Nonnull AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof EntityFishHook) event.addCapability(CAPABILITY_ID, new CapabilityProvider<>(CAPABILITY));
    }

    class Impl implements ICompactFishing
    {
        protected int level;

        @Override
        public int getLevel() { return level; }

        @Override
        public void setLevel(int levelIn) { level = levelIn; }
    }

    enum Storage implements Capability.IStorage<ICompactFishing>
    {
        INSTANCE;

        @Nullable
        @Override
        public NBTBase writeNBT(@Nonnull Capability<ICompactFishing> capability, @Nonnull ICompactFishing instance, @Nullable EnumFacing side) {
            return new NBTTagInt(instance.getLevel());
        }

        @Override
        public void readNBT(@Nonnull Capability<ICompactFishing> capability, @Nonnull ICompactFishing instance, @Nullable EnumFacing side, @Nullable NBTBase nbt) {
            if(nbt instanceof NBTPrimitive) instance.setLevel(((NBTPrimitive)nbt).getInt());
        }
    }
}
