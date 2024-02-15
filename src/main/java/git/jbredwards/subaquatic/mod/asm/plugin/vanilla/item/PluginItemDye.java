/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.item;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Random;

/**
 * Fix multiple issues with bonemeal particles
 * @author jbred
 *
 */
public final class PluginItemDye implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * New code:
         * //fix multiple issues with bonemeal particles
         * @SideOnly(Side.CLIENT)
         * public static void spawnBonemealParticles(World world, BlockPos pos, int amount)
         * {
         *     Hooks.spawnBonemealParticles(world, pos, amount);
         * }
         */
        overrideMethod(classNode, method -> method.name.equals(obfuscated ? "func_180617_a" : "spawnBonemealParticles"),
            "spawnBonemealParticles", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;I)V", generator -> {
                generator.visitVarInsn(ALOAD, 0);
                generator.visitVarInsn(ALOAD, 1);
                generator.visitVarInsn(ILOAD, 2);
            }
        );

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static void spawnBonemealParticles(@Nonnull World world, @Nonnull BlockPos pos, int amount) {
            final IBlockState state = world.getBlockState(pos);
            final AxisAlignedBB bb = state.getBoundingBox(world, pos);
            //use the size of the box to determine how many particles are to spawn
            if(amount == 0) amount = Math.max(1, (int)(bb.getAverageEdgeLength() * 10));

            //renders particles on each side
            //fixes bonemeal particles not being visible with grass blocks
            if(state.isOpaqueCube()) {
                //don't spawn particles in sides that won't be visible
                final EnumFacing[] possibleSides = Arrays.stream(EnumFacing.VALUES)
                        .filter(side -> !world.getBlockState(pos.offset(side)).isOpaqueCube())
                        .toArray(EnumFacing[]::new);

                amount *= possibleSides.length; //so the amount of visible sides doesn't influence how many particles get rendered per side
                while(amount --> 0) {
                    final EnumFacing side = possibleSides[world.rand.nextInt(possibleSides.length)];

                    final double xOffset = getOffset(world.rand, bb.minX, bb.maxX, side.getXOffset());
                    final double yOffset = getOffset(world.rand, bb.minY, bb.maxY, side.getYOffset());
                    final double zOffset = getOffset(world.rand, bb.minZ, bb.maxZ, side.getZOffset());

                    final double xSpeed = world.rand.nextGaussian() * 0.002;
                    final double ySpeed = world.rand.nextGaussian() * 0.002;
                    final double zSpeed = world.rand.nextGaussian() * 0.002;

                    world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, xSpeed, ySpeed, zSpeed);
                }
            }

            //renders particles at random positions within the BB
            //fixes weird bonemeal particles on blocks with unique hitboxes
            else while(amount --> 0) {
                final double xOffset = MathHelper.nextDouble(world.rand, bb.minX, bb.maxX);
                final double yOffset = MathHelper.nextDouble(world.rand, bb.minY, bb.maxY);
                final double zOffset = MathHelper.nextDouble(world.rand, bb.minZ, bb.maxZ);

                final double xSpeed = world.rand.nextGaussian() * 0.002;
                final double ySpeed = world.rand.nextGaussian() * 0.002;
                final double zSpeed = world.rand.nextGaussian() * 0.002;

                world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, xSpeed, ySpeed, zSpeed);
            }
        }

        //helper
        public static double getOffset(@Nonnull Random rand, double min, double max, int offset) {
            switch(offset) {
                case -1: return min - 0.0625;
                case  1: return max + 0.0625;
                default: return MathHelper.nextDouble(rand, min, max);
            }
        }
    }
}
