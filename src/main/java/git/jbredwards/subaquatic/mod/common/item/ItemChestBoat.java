package git.jbredwards.subaquatic.mod.common.item;

import git.jbredwards.subaquatic.mod.common.entity.item.EntityBoatContainer;
import git.jbredwards.subaquatic.mod.common.entity.item.MultiPartChestPart;
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
    public String getRegexTarget() {
        return null;
    }

    @Nonnull
    @Override
    public String getRegexReplacement() {
        return null;
    }

    @Override
    public void buildBoatContainerPart(@Nonnull EntityBoatContainer boat, @Nonnull ItemStack boatStack) {
        boat.containerPart = new MultiPartChestPart(boat, "chest", 1, 1);
    }
}
