/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.client;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Water droplet particles keep the color set by this mod
 * @author jbred
 *
 */
public final class PluginParticleDrip implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return checkMethod(method, obfuscated ? "func_189213_a" : "onUpdate", "()V"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * onUpdate: (changes are around lines 65-67)
         * Old code:
         * {
         *     this.particleRed = 0.2F;
         *     this.particleGreen = 0.3F;
         *     this.particleBlue = 1.0F;
         * }
         *
         * New code:
         * //remove hardcoded colors
         * {
         *
         * }
         */
        if(insn.getPrevious() != null && insn.getPrevious().getOpcode() == IF_ACMPNE) {
            while(insn.getOpcode() != GOTO) {
                insn = insn.getNext();
                instructions.remove(insn.getPrevious());
            }

            return true;
        }

        return false;
    }
}
