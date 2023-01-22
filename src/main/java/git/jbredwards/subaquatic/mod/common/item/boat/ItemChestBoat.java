package git.jbredwards.subaquatic.mod.common.item.boat;

import git.jbredwards.subaquatic.mod.common.entity.item.AbstractBoatContainer;
import git.jbredwards.subaquatic.mod.common.entity.item.part.MultiPartChestPart;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
    public AbstractBoatContainer getBoatContainer(@Nonnull World world, double x, double y, double z) {
        return null;
    }

    @Nonnull
    @Override
    public String getRegexTarget() { return null; }

    @Nonnull
    @Override
    public String getRegexReplacement() { return null; }
}
