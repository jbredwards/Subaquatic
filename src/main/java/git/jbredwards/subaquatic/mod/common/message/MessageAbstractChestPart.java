package git.jbredwards.subaquatic.mod.common.message;

import git.jbredwards.fluidlogged_api.api.network.IClientMessageHandler;
import git.jbredwards.fluidlogged_api.api.network.message.AbstractMessage;
import git.jbredwards.subaquatic.mod.common.entity.item.AbstractBoatContainer;
import git.jbredwards.subaquatic.mod.common.entity.item.part.MultiPartAbstractChestPart;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Sync numPlayersUsing with all players tracking the parent boat entity
 * @author jbred
 *
 */
public final class MessageAbstractChestPart extends AbstractMessage
{
    public int numPlayersUsing, entityId;

    public MessageAbstractChestPart() {}
    public MessageAbstractChestPart(int numPlayersUsingIn, int entityIdIn) {
        numPlayersUsing = numPlayersUsingIn;
        entityId = entityIdIn;
        isValid = true;
    }

    @Override
    public void read(@Nonnull PacketBuffer buf) {
        numPlayersUsing = buf.readVarInt();
        entityId = buf.readVarInt();
    }

    @Override
    public void write(@Nonnull PacketBuffer buf) {
        buf.writeVarInt(numPlayersUsing);
        buf.writeVarInt(entityId);
    }

    public enum Handler implements IClientMessageHandler<MessageAbstractChestPart>
    {
        INSTANCE;

        @SideOnly(Side.CLIENT)
        @Override
        public void handleMessage(@Nonnull MessageAbstractChestPart message) {
            final Entity entity = Minecraft.getMinecraft().world.getEntityByID(message.entityId);
            if(entity instanceof AbstractBoatContainer) {
                final AbstractBoatContainer boat = (AbstractBoatContainer)entity;
                if(boat.containerPart instanceof MultiPartAbstractChestPart)
                    ((MultiPartAbstractChestPart)boat.containerPart).setNumPlayersUsing(message.numPlayersUsing);
            }
        }
    }
}
