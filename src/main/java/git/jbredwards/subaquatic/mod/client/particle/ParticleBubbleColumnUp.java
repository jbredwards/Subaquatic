package git.jbredwards.subaquatic.mod.client.particle;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.client.particle.factory.IParticleConstructor;
import git.jbredwards.subaquatic.mod.client.particle.factory.ParticleFactoryColorize;
import git.jbredwards.subaquatic.mod.common.compat.inspirations.InspirationsHandler;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticWaterColorConfig;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.ParticleBubble;
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
public class ParticleBubbleColumnUp extends ParticleBubble implements IParticleBubbleColumn
{
    @Nonnull
    public static final IParticleFactory FACTORY = new ParticleFactoryColorize((IParticleConstructor)ParticleBubbleColumnUp::new,
            Subaquatic.isInspirationsInstalled ? InspirationsHandler::getParticleColorAt : SubaquaticWaterColorConfig::getParticleColorAt);

    protected ParticleBubbleColumnUp(@Nonnull World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... args) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        particleMaxAge = (int)(40 / (Math.random() * 0.8 + 0.2));
        canCollide = false;
    }

    @Override
    public void onUpdate() {
        motionY += 0.003;
        super.onUpdate();
    }
}
