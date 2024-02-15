/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client.particle;

import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.client.Minecraft;
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
public class ParticleBubblePop extends Particle
{
    @Nonnull
    public static final TextureAtlasSprite[] TEXTURES = new TextureAtlasSprite[5];
    public ParticleBubblePop(@Nonnull ParticleBubble parent) {
        super(parent.world, parent.posX, parent.posY, parent.posZ);
        particleScale = parent.particleScale * 2.75f;
        canCollide = false;

        setMaxAge(4);
        setSize(0.02f, 0.02f);
        setParticleTexture(TEXTURES[0]);
        setRBGColorF(parent.getRedColorF(), parent.getGreenColorF(), parent.getBlueColorF());
        setAlphaF(parent.particleAlpha); //compat with dynamic surroundings' translucent air bubbles

        if((SubaquaticConfigHandler.Client.Particle.playBubblePopSound || parent instanceof IParticleBubbleColumn) && Minecraft.getMinecraft().player.getPositionEyes(1).squareDistanceTo(posX, posY, posZ) < 8)
            world.playSound(posX, posY, posZ, SubaquaticSounds.BUBBLE_COLUMN_BUBBLE_POP, SoundCategory.BLOCKS, 0.5f, Math.max(0, 2 - (parent.particleScale * 2 - 1) - 0.5f), false);
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
