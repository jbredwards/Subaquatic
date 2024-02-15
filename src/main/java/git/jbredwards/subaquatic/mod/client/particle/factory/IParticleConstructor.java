/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client.particle.factory;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Allows the easy creation of an IParticleFactory from a Particle class's constructor.
 * @author jbred
 *
 */
@FunctionalInterface
@SideOnly(Side.CLIENT)
public interface IParticleConstructor extends IParticleFactory
{
    @Nonnull
    Particle generate(@Nonnull World world, double x, double y, double z, double speedX, double speedY, double speedZ, @Nonnull int... args);

    @Nullable
    @Override
    default Particle createParticle(int particleID, @Nonnull World world, double x, double y, double z, double speedX, double speedY, double speedZ, @Nonnull int... args) {
        return generate(world, x, y, z, speedX, speedY, speedZ, args);
    }
}
