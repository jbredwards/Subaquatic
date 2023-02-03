package git.jbredwards.subaquatic.mod.common.entity.living;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.client.particle.ParticleGlowSquidAura;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class EntityEnderGlowSquid extends EntityGlowSquid
{
    public EntityEnderGlowSquid(@Nonnull World worldIn) { super(worldIn); }

    @Nonnull
    @Override
    protected SoundEvent getAmbientSound() { return SubaquaticSounds.ENTITY_ENDER_GLOW_SQUID_AMBIENT; }

    @Nonnull
    @Override
    protected SoundEvent getDeathSound() { return SubaquaticSounds.ENTITY_ENDER_GLOW_SQUID_DEATH; }

    @Nonnull
    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource source) { return SubaquaticSounds.ENTITY_ENDER_GLOW_SQUID_HURT; }

    @Nonnull
    @Override
    protected ResourceLocation getLootTable() { return new ResourceLocation(Subaquatic.MODID, "entities/ender_glow_squid"); }

    @SideOnly(Side.CLIENT)
    public void spawnParticles() {
        final double x = posX + width * (rand.nextDouble() - 0.5);
        final double y = posY + height * rand.nextDouble();
        final double z = posZ + width * (rand.nextDouble() - 0.5);
        Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleGlowSquidAura(world, x, y, z, true));
    }
}
