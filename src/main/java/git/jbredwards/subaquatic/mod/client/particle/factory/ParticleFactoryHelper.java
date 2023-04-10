package git.jbredwards.subaquatic.mod.client.particle.factory;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Utility class that creates an IParticleFactory from a Particle class's constructor
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public final class ParticleFactoryHelper
{
    @Nonnull
    public static IParticleFactory fromConstructor(@Nonnull IParticleConstructor constructor) {
        return (id, world, x, y, z, speedX, speedY, speedZ, args) -> constructor.generate(world, x, y, z, speedX, speedY, speedZ, args);
    }

    @FunctionalInterface
    public interface IParticleConstructor
    {
        @Nonnull
        Particle generate(@Nonnull World world, double x, double y, double z, double speedX, double speedY, double speedZ, int... args);
    }
}
