package git.jbredwards.subaquatic.mod.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public class ParticleGlowSquidAura extends Particle
{
    public ParticleGlowSquidAura(@Nonnull World worldIn, double posXIn, double posYIn, double posZIn, boolean isPurple) {
        super(worldIn, posXIn, posYIn, posZIn, 0, 0, 0);
        applyColor(isPurple);

        motionX *= 0.1;
        motionY *= 0.2;
        motionZ *= 0.1;
        canCollide = false;

        setMaxAge((int)(8 / (rand.nextDouble() * 0.8 + 0.2)));
    }

    public void applyColor(boolean isPurple) {
        if(rand.nextBoolean()) {
            particleRed = isPurple ? : 0.6f;
            particleGreen = isPurple ? : 0;
            particleBlue = isPurple ? : 0.8f;
        }
        else {
            particleRed = isPurple ? : 0.08f;
            particleGreen = isPurple ? : 0.4f;
            particleBlue = isPurple ? : 0.4f;
        }
    }

    @Override
    public int getFXLayer() { return 1; }

    @Override
    public void setParticleTextureIndex(int particleTextureIndex) {}

    @Override
    public int getBrightnessForRender(float partialTick) { return 240; }
}
