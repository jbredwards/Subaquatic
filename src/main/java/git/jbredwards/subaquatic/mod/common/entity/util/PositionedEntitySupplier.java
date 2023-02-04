package git.jbredwards.subaquatic.mod.common.entity.util;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@FunctionalInterface
public interface PositionedEntitySupplier<T extends Entity>
{
    @Nonnull
    T generate(@Nonnull World world, double x, double y, double z);
}
