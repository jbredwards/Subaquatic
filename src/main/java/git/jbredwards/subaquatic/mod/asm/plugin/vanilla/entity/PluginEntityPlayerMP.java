/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.entity;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Check for no collision instead of air when falling on a block (MC-1691)
 * @author jbred
 *
 */
public final class PluginEntityPlayerMP implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_71122_b" : "handleFalling"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * move: (changes are around line 885)
         * Old code:
         * int j = MathHelper.floor(this.posY - 0.20000000298023224D);
         *
         * New code:
         * //remove y offset
         * int j = MathHelper.floor(this.posY);
         */
        if(insn.getOpcode() == LDC && ((LdcInsnNode)insn).cst.equals(0.20000000298023224D))
            removeFrom(instructions, insn, 1);
        /*
         * move: (changes are around line 890)
         * Old code:
         * if (iblockstate.getBlock().isAir(iblockstate, this.world, blockpos))
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
        else if(checkMethod(insn, "isAir")) {
            final InsnList list = new InsnList();
            list.add(new VarInsnNode(ALOAD, 7));
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(genMethodNode("git/jbredwards/subaquatic/mod/asm/plugin/vanilla/entity/PluginEntity$Hooks", "hasNoCollision", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)Z"));

            instructions.insert(insn, list);
            removeFrom(instructions, insn, -5);
        }
        /*
         * move: (changes are around line 896)
         * Old code:
         * if (block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate)
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
}
