package git.jbredwards.subaquatic.mod.common.item;

import git.jbredwards.subaquatic.mod.common.entity.item.EntityBoatContainer;
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
    public EntityBoatContainer getBoatToCreate(@Nonnull World world, double x, double y, double z, @Nonnull ItemStack createdFrom) {
        return null;
    }
}
