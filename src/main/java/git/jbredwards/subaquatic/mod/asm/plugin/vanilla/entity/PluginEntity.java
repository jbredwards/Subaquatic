/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.entity;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.fluidlogged_api.api.util.FluidState;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Check for no collision instead of air when falling on a block (MC-1691)
 * @author jbred
 *
 */
public final class PluginEntity implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_70091_d" : "move"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * move: (changes are around line 989)
         * Old code:
         * int i1 = MathHelper.floor(this.posY - 0.20000000298023224D);
         *
         * New code:
         * //remove y offset
         * int i1 = MathHelper.floor(this.posY);
         */
        if(insn.getOpcode() == LDC && ((LdcInsnNode)insn).cst.equals(0.20000000298023224D))
            removeFrom(instructions, insn, 1);
        /*
         * move: (changes are around line 994)
         * Old code:
         * if (iblockstate.getMaterial() == Material.AIR)
         * {
         *     ...
         * }
         *
         * New code:
         * //check for no collision instead of air
         * if (hasNoCollision(iblockstate, blockpos, this))
         * {
         *     ...
         * }
         */
        else if(checkField(insn, obfuscated ? "field_151579_a" : "AIR")) {
            ((JumpInsnNode)insn.getNext()).setOpcode(IFEQ);
            final InsnList list = new InsnList();
            list.add(new VarInsnNode(ALOAD, obfuscated ? 71 : 26));
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(genMethodNode("hasNoCollision", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)Z"));

            instructions.insert(insn, list);
            removeFrom(instructions, insn, -1);
        }
        /*
         * move: (changes are around line 1000)
         * Old code:
         * if (block1 instanceof BlockFence || block1 instanceof BlockWall || block1 instanceof BlockFenceGate)
         * {
         *     ...
         * }
         *
         * New code:
         * //if this gets called, the block below should always be used
         * if (true)
         * {
         *     ...
         * }
         */
        else if(insn.getOpcode() == INSTANCEOF && ((TypeInsnNode)insn).desc.equals("net/minecraft/block/BlockFenceGate")) {
            instructions.insert(insn, new InsnNode(ICONST_1));
            removeFrom(instructions, insn, -7);
            return true;
        }

        return false;
    }

    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * New code:
         * //fix block running particles
         * protected void createRunningParticles()
         * {
         *     Hooks.createRunningParticles(this);
         * }
         */
        overrideMethod(classNode, method -> method.name.equals(obfuscated ? "func_174808_Z" : "createRunningParticles"),
            "createRunningParticles", "(Lnet/minecraft/entity/Entity;)V", generator -> generator.visitVarInsn(ALOAD, 0));

        return true;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static void createRunningParticles(@Nonnull Entity entity) {
            BlockPos pos = new BlockPos(entity);
            IBlockState state = entity.world.getBlockState(pos);

            if(!canCollideCheck(entity.world, pos, state, true)) return;
            else if(hasNoCollision(state, pos, entity)) {
                pos = pos.down();
                state = entity.world.getBlockState(pos);
            }

            if(!state.getBlock().addRunningEffects(state, entity.world, pos, entity)
            && state.getRenderType() != EnumBlockRenderType.INVISIBLE //prevent barrier particles & the like
            && canCollideCheck(entity.world, pos, state, false)) { //prevent water breaking particles & the like
                final Random rand = entity.world.rand;
                entity.world.spawnParticle(EnumParticleTypes.BLOCK_CRACK,
                        entity.posX + (rand.nextFloat() - 0.5) * entity.width,
                        entity.getEntityBoundingBox().minY + 0.1,
                        entity.posZ + (rand.nextFloat() - 0.5) * entity.width,
                        -entity.motionX * 4, 1.5, -entity.motionZ * 4, Block.getStateId(state));
            }
        }

        public static boolean hasNoCollision(@Nonnull IBlockState state, @Nonnull BlockPos pos, @Nonnull Entity entity) {
            if(state.getBlock().isAir(state, entity.world, pos) || !state.getBlock().canCollideCheck(state, false)) return true;
            final double xz = entity.width / 2;

            return !state.getBoundingBox(entity.world, pos).offset(pos).intersects(entity.posX - xz, entity.posY - 0.2, entity.posZ - xz, entity.posX + xz, entity.posY + 0.0001, entity.posZ + xz);
        }

        //helper
        public static boolean canCollideCheck(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean allowAir) {
            if(allowAir && state.getBlock().isAir(state, world, pos)) return true;
            else if(!state.getBlock().canCollideCheck(state, false)) return false;

            final FluidState fluidState = FluidState.get(world, pos);
            return fluidState.isEmpty() || fluidState.getBlock().canCollideCheck(fluidState.getState(), false);
        }
    }
}
