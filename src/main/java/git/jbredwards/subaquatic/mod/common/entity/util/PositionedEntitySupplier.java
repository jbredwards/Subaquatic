package git.jbredwards.subaquatic.mod.common.entity.util;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Generates a new entity instance at the given x y z pos. This does not spawn the entity
 * @author jbred
 *
 */
@FunctionalInterface
public interface PositionedEntitySupplier<T extends Entity>
{
    @Nonnull
    T newInstance(@Nonnull World world, double x, double y, double z);
}
