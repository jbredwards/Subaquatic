package git.jbredwards.subaquatic.mod.client.particle.factory;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.function.ToIntBiFunction;

/**
 * Used to color particles prior to spawn
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public class ParticleFactoryColorize implements IParticleFactory
{
    @Nonnull public final IParticleColorGetter colorSupplier;
    @Nonnull public final IParticleFactory particleFactory;

    //gets the color from the particle's BlockPos
    public ParticleFactoryColorize(@Nonnull IParticleFactory factoryIn, @Nonnull ToIntBiFunction<World, BlockPos> colorIn) {
        this(factoryIn, (world, x, y, z) -> new Color(colorIn.applyAsInt(world, new BlockPos(x, y, z))));
    }

    //gets the color from the particle's exact position
    public ParticleFactoryColorize(@Nonnull IParticleFactory factoryIn, @Nonnull IParticleColorGetter colorIn) {
        colorSupplier = colorIn;
        particleFactory = factoryIn;
    }

    @Nullable
    @Override
    public Particle createParticle(int particleID, @Nonnull World worldIn, double x, double y, double z, double xSpeedIn, double ySpeedIn, double zSpeedIn, @Nonnull int... args) {
        final @Nullable Particle particle = particleFactory.createParticle(particleID, worldIn, x, y, z, xSpeedIn, ySpeedIn, zSpeedIn, args);
        if(particle != null) {
            final float[] rgb = colorSupplier.get(worldIn, x, y, z).getRGBColorComponents(null);
            particle.setRBGColorF(rgb[0], rgb[1], rgb[2]);
        }

        return particle;
    }
}
