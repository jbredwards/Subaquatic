package git.jbredwards.subaquatic.mod.common.message;

import git.jbredwards.fluidlogged_api.api.network.IClientMessageHandler;
import git.jbredwards.fluidlogged_api.api.network.message.AbstractMessage;
import git.jbredwards.subaquatic.mod.common.entity.item.EntityBoatFurnace;
import git.jbredwards.subaquatic.mod.common.entity.item.part.MultiPartFurnacePart;
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
public class SMessageFurnacePart extends AbstractMessage
{
    public int entityId;
    public int burnTime;

    public SMessageFurnacePart() {}
    public SMessageFurnacePart(@Nonnull Entity entityIn, int burnTimeIn) {
        entityId = entityIn.getEntityId();
        burnTime = burnTimeIn;
        isValid = true;
    }

    @Override
    public void read(@Nonnull PacketBuffer buf) {
        entityId = buf.readVarInt();
        burnTime = buf.readVarInt();
    }

    @Override
    public void write(@Nonnull PacketBuffer buf) {
        buf.writeVarInt(entityId);
        buf.writeVarInt(burnTime);
    }

    public enum Handler implements IClientMessageHandler<SMessageFurnacePart>
    {
        INSTANCE;

        @SideOnly(Side.CLIENT)
        @Override
        public void handleMessage(@Nonnull SMessageFurnacePart message) {
            final Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.entityId);
            if(entity instanceof EntityBoatFurnace) ((MultiPartFurnacePart)((EntityBoatFurnace)entity).containerPart).setField(0, message.burnTime);
        }
    }
}
