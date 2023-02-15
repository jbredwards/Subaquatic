package git.jbredwards.subaquatic.mod.client.particle.factory;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.ToIntBiFunction;

/**
 * Used to color particles prior to spawn
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public class ParticleFactoryColorize implements IParticleFactory
{
    @Nonnull
    public final ToIntBiFunction<World, BlockPos> color;

    @Nonnull
    public final IParticleFactory factory;
    public ParticleFactoryColorize(@Nonnull IParticleFactory factoryIn, @Nonnull ToIntBiFunction<World, BlockPos> colorIn) {
        color = colorIn;
        factory = factoryIn;
    }

    @Nullable
    @Override
    public Particle createParticle(int particleID, @Nonnull World worldIn, double x, double y, double z, double xSpeedIn, double ySpeedIn, double zSpeedIn, @Nonnull int... args) {
        final @Nullable Particle particle = factory.createParticle(particleID, worldIn, x, y, z, xSpeedIn, ySpeedIn, zSpeedIn, args);
        if(particle != null) {
            final int rgb = color.applyAsInt(worldIn, new BlockPos(x, y, z));
            final float r = 0.6f * (rgb >> 16 & 255) / 255.0f;
            final float g = 0.6f * (rgb >> 8 & 255) / 255.0f;
            final float b = 0.6f * (rgb & 255) / 255.0f;
            particle.setRBGColorF(r, g, b);
        }

        return particle;
    }
}
