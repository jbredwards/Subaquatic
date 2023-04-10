package git.jbredwards.subaquatic.mod.common.message;

import git.jbredwards.fluidlogged_api.api.network.message.AbstractMessage;
import git.jbredwards.subaquatic.mod.common.entity.item.AbstractBoatContainer;
import git.jbredwards.subaquatic.mod.common.entity.item.part.MultiPartAbstractInventoryPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public final class CMessageOpenBoatInventory extends AbstractMessage
{
    @Override
    public void read(@Nonnull PacketBuffer buf) {}

    @Override
    public void write(@Nonnull PacketBuffer buf) {}

    public enum Handler implements IMessageHandler<CMessageOpenBoatInventory, IMessage>
    {
        INSTANCE;

        @Nullable
        @Override
        public IMessage onMessage(@Nonnull CMessageOpenBoatInventory message, @Nonnull MessageContext ctx) {
            if(message.isValid) {
                final EntityPlayer player = ctx.getServerHandler().player;
                if(player.getRidingEntity() instanceof AbstractBoatContainer) {
                    final AbstractBoatContainer boat = (AbstractBoatContainer)player.getRidingEntity();
                    if(boat.containerPart instanceof MultiPartAbstractInventoryPart)
                        player.displayGUIChest(((MultiPartAbstractInventoryPart)boat.containerPart).getInventory(player));
                }
            }

            return null;
        }
    }
}
