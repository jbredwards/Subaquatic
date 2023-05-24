package git.jbredwards.subaquatic.mod.common.entity.item.part;

import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public interface UIProviderPart
{
    void openUI(@Nonnull EntityPlayer player);
}
