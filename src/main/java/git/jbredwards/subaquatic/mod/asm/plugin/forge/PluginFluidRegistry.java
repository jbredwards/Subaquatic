/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.forge;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Changes the water textures to allow for better coloring
 * @author jbred
 *
 */
public final class PluginFluidRegistry implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return checkMethod(method, "<clinit>", "()V"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * (changes are around line 73)
         * Old code:
         * ... new Fluid("water", new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow"), new ResourceLocation("blocks/water_overlay")...
         *
         * New code:
         * //change texture locations to greyscale
         * ... new Fluid("water", new ResourceLocation("subaquatic:blocks/water_still"), new ResourceLocation("subaquatic:blocks/water_flow"), new ResourceLocation("subaquatic:blocks/water_overlay")...
         */
        if(insn.getOpcode() == LDC && insn instanceof LdcInsnNode) {
            final LdcInsnNode ldc = (LdcInsnNode)insn;

            if(ldc.cst.equals("blocks/water_still")) ldc.cst = "subaquatic:blocks/water_still";
            if(ldc.cst.equals("blocks/water_flow"))  ldc.cst = "subaquatic:blocks/water_flow";
            if(ldc.cst.equals("blocks/water_overlay")) {
                ldc.cst = "subaquatic:blocks/water_overlay";
                return true;
            }
        }

        return false;
    }
}
