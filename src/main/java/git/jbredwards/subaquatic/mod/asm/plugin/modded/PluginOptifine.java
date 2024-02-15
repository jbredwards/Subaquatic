/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.modded;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Fix possible Optifine NPE with bubble particles
 * @author jbred
 *
 */
public final class PluginOptifine implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals("updateWaterFX"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * updateWaterFX:
         * Old code:
         * {
         *     ...
         * }
         *
         * New code:
         * {
         *     if(fx == null) return;
         *     ...
         * }
         */
        final InsnList list = new InsnList();
        final LabelNode label = new LabelNode();
        list.add(new VarInsnNode(ALOAD, 0));
        list.add(new JumpInsnNode(IFNONNULL, label));
        list.add(new InsnNode(RETURN));
        list.add(label);
        list.add(new FrameNode(F_SAME, 0, null, 0, null));
        instructions.insert(insn, list);
        return true;
    }
}
