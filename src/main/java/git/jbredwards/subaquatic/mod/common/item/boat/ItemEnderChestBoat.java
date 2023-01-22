package git.jbredwards.subaquatic.mod.common.item.boat;

import git.jbredwards.subaquatic.mod.common.entity.item.AbstractBoatContainer;
import git.jbredwards.subaquatic.mod.common.entity.item.EntityBoatEnderChest;
import net.minecraft.world.World;

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
    public AbstractBoatContainer getBoatContainer(@Nonnull World world, double x, double y, double z) {
        return new EntityBoatEnderChest(world, x, y, z);
    }

    @Nonnull
    @Override
    public String getRegexTarget() { return "TODO"; }

    @Nonnull
    @Override
    public String getRegexReplacement() { return "TODO"; }
}
