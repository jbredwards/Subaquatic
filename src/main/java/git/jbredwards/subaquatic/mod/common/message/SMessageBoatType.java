package git.jbredwards.subaquatic.mod.common.message;

import git.jbredwards.fluidlogged_api.api.network.IClientMessageHandler;
import git.jbredwards.fluidlogged_api.api.network.message.AbstractMessage;
import git.jbredwards.subaquatic.mod.common.capability.IBoatType;
import git.jbredwards.subaquatic.mod.common.capability.util.BoatType;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticBoatTypesConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public final class SMessageBoatType extends AbstractMessage
{
    public int boatTypeId;
    public int entityId;

    public SMessageBoatType() {}
    public SMessageBoatType(@Nonnull BoatType typeIn, @Nonnull Entity entityIn) {
        boatTypeId = SubaquaticBoatTypesConfig.getIndex(typeIn.boat, typeIn.boatMeta);
        entityId = entityIn.getEntityId();
        isValid = true;
    }

    @Override
    public void read(@Nonnull PacketBuffer buf) {
        boatTypeId = buf.readVarInt();
        entityId = buf.readVarInt();
    }

    @Override
    public void write(@Nonnull PacketBuffer buf) {
        buf.writeVarInt(boatTypeId);
        buf.writeVarInt(entityId);
    }

    public enum Handler implements IClientMessageHandler<SMessageBoatType>
    {
        INSTANCE;

        @SideOnly(Side.CLIENT)
        @Override
        public void handleMessage(@Nonnull SMessageBoatType message) {
            final IBoatType cap = IBoatType.get(Minecraft.getMinecraft().world.getEntityByID(message.entityId));
            if(cap != null) cap.setType(SubaquaticBoatTypesConfig.BOAT_TYPES_LOOKUP.get(message.boatTypeId));
        }
    }
}
