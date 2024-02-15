/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

/**
 * My lazy workaround to the block state metadata limit
 * @author jbred
 *
 */
public class TileEntityGlowLichen extends TileEntity
{
    public static final byte ALL_SIDE_DATA = 31;
    public byte attachedSideData = ALL_SIDE_DATA; //start with all sides active, as to easily tell when the side logic hasn't been properly handled

    public boolean isAttachedTo(@Nonnull EnumFacing side) { return (attachedSideData >> side.getIndex() & 1) != 0; }
    public void setAttachedTo(@Nonnull EnumFacing side, boolean isAttached) {
        final byte serialized = (byte)(1 << side.getIndex());
        attachedSideData &=~ serialized; //reset value
        if(isAttached) attachedSideData |= serialized; //put new value if present
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound compound) {
        if(compound.hasKey("AttachedSideData", Constants.NBT.TAG_BYTE))
            attachedSideData = compound.getByte("AttachedSideData");

        super.readFromNBT(compound);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound compound) {
        compound.setByte("AttachedSideData", attachedSideData);
        return super.writeToNBT(compound);
    }

    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag() { return writeToNBT(new NBTTagCompound()); }

    @Nonnull
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() { return new SPacketUpdateTileEntity(pos, -1, getUpdateTag()); }

    @Override
    public void onDataPacket(@Nonnull NetworkManager net, @Nonnull SPacketUpdateTileEntity pkt) {
        handleUpdateTag(pkt.getNbtCompound());
        world.markBlockRangeForRenderUpdate(pos, pos);
    }
}
