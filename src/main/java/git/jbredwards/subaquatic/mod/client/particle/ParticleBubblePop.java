package git.jbredwards.subaquatic.mod.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleBubble;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public class ParticleBubblePop extends Particle
{
    @Nonnull
    public static final TextureAtlasSprite[] TEXTURES = new TextureAtlasSprite[5];
    public ParticleBubblePop(@Nonnull ParticleBubble parent) {
        super(parent.world, parent.posX, parent.posY, parent.posZ);
        motionX = parent.motionX;
        motionY = parent.motionY;
        motionZ = parent.motionZ;

        setMaxAge(4);
        setSize(0.02f, 0.02f);
        setParticleTexture(TEXTURES[0]);

        particleGravity = 0.008f;
        particleScale = parent.particleScale;
    }

    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        motionY -= particleGravity;
        move(motionX, motionY, motionZ);

        if(particleAge++ >= particleMaxAge) setExpired();
        else setParticleTexture(TEXTURES[particleAge]);
    }

    @Override
    public int getFXLayer() { return 1; }

    @Override
    public void setParticleTextureIndex(int particleTextureIndex) {}
}
