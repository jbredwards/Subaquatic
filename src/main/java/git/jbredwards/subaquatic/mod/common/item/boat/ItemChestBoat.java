package git.jbredwards.subaquatic.mod.common.item.boat;

import git.jbredwards.subaquatic.mod.common.entity.item.EntityBoatContainer;
import git.jbredwards.subaquatic.mod.common.entity.item.MultiPartChestPart;
import git.jbredwards.subaquatic.mod.common.item.boat.ItemBoatContainer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class ItemChestBoat extends ItemBoatContainer
{
    @Nonnull
    @Override
    public MultiPartChestPart createContainer(@Nonnull EntityBoatContainer boat, @Nonnull ItemStack boatStack) {
        return new MultiPartChestPart(boat, "chest", 0.875f, 0.875f);
    }

    @Nonnull
    @Override
    public String getRegexTarget() { return null; }

    @Nonnull
    @Override
    public String getRegexReplacement() { return null; }
}
