/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.client;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Spawn bubbles for underwater explosions
 * @author jbred
 *
 */
public final class PluginParticleExplosionHuge implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_189213_a" : "onUpdate"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * onUpdate: (changes are around line 36)
         * Old code:
         * this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d0, d1, d2, (double)((float)this.timeSinceStart / (float)this.maximumTime), 0.0D, 0.0D);
         *
         * New code:
         * //spawn bubbles for underwater explosions
         * Hooks.spawnParticle(this.world, EnumParticleTypes.EXPLOSION_LARGE, d0, d1, d2, (double)((float)this.timeSinceStart / (float)this.maximumTime), 0.0D, 0.0D, this);
         */
        if(checkMethod(insn, obfuscated ? "func_175688_a" : "spawnParticle")) {
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 0));
            instructions.insertBefore(insn, genMethodNode("spawnParticle", "(Lnet/minecraft/world/World;Lnet/minecraft/util/EnumParticleTypes;DDDDDD[ILnet/minecraft/client/particle/Particle;)V"));
            instructions.remove(insn);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @SideOnly(Side.CLIENT)
        public static void spawnParticle(@Nonnull World world, @Nonnull EnumParticleTypes particle, double xCoord, double yCoord, double zCoord, double xSpeedIn, double ySpeedIn, double zSpeedIn, int[] parameters, @Nonnull Particle parent) {
            final int underwaterExplosionBubbleCount = SubaquaticConfigHandler.Client.Particle.underwaterExplosionBubbleCount;
            if(underwaterExplosionBubbleCount != 0 && FluidloggedUtils.getFluidOrReal(world, new BlockPos(xCoord, yCoord, zCoord)).getMaterial() == Material.WATER) {
                final double xSpeed = xCoord - parent.posX;
                final double ySpeed = yCoord - parent.posY;
                final double zSpeed = zCoord - parent.posZ;
                for(int i = 0; i < underwaterExplosionBubbleCount; i++) world.spawnParticle(
                        EnumParticleTypes.WATER_BUBBLE,
                        xCoord, yCoord, zCoord,
                        xSpeed * Math.random() * 2, ySpeed * Math.random() * 2, zSpeed * Math.random() * 2);
            }

            world.spawnParticle(particle, xCoord, yCoord, zCoord, xSpeedIn, ySpeedIn, zSpeedIn, parameters);
        }
    }
}
