package git.jbredwards.subaquatic.mod.client.particle.factory;

import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.awt.*;

/**
 * Returns the color that should be applied to the particle given the position
 * @author jbred
 *
 */
@FunctionalInterface
public interface IParticleColorGetter
{
    @Nonnull
    Color get(@Nonnull World world, double x, double y, double z);
}
