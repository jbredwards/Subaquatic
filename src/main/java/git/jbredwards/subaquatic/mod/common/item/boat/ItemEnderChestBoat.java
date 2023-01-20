package git.jbredwards.subaquatic.mod.common.item.boat;

import git.jbredwards.subaquatic.mod.common.entity.item.EntityBoatContainer;
import git.jbredwards.subaquatic.mod.common.entity.item.MultiPartEnderChestPart;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class ItemEnderChestBoat extends ItemBoatContainer
{
    @Nonnull
    @Override
    public MultiPartEnderChestPart createContainer(@Nonnull EntityBoatContainer boat, @Nonnull ItemStack boatStack) {
        return new MultiPartEnderChestPart(boat, "ender_chest", 0.875f, 0.875f);
    }

    @Nonnull
    @Override
    public String getRegexTarget() { return "TODO"; }

    @Nonnull
    @Override
    public String getRegexReplacement() { return "TODO"; }
}
