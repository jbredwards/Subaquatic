package git.jbredwards.subaquatic.mod.client.particle;

import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleBubble;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public class ParticleBubbleColumnPop extends Particle
{
    @Nonnull
    public static final TextureAtlasSprite[] TEXTURES = new TextureAtlasSprite[5];
    public ParticleBubbleColumnPop(@Nonnull ParticleBubble parent) {
        super(parent.world, parent.posX, parent.posY, parent.posZ);
        particleScale = parent.particleScale * 2.75f;

        setMaxAge(4);
        setSize(0.02f, 0.02f);
        setParticleTexture(TEXTURES[0]);

        if(SubaquaticConfigHandler.playBubblePopSound || parent instanceof ParticleBubbleColumn)
            world.playSound(posX, posY, posZ, SubaquaticSounds.BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.BLOCKS, 1, parent.particleScale, false);
    }

    @Override
    public void onUpdate() {
        if(particleAge++ >= particleMaxAge) setExpired();
        else setParticleTexture(TEXTURES[particleAge]);
    }

    @Override
    public int getFXLayer() { return 1; }

    @Override
    public void setParticleTextureIndex(int particleTextureIndex) {}
}
