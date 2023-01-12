package git.jbredwards.subaquatic.mod.asm.plugin.vanilla;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public final class PluginEntityRenderer implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_78474_d" : "renderRainSnow"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * renderRainSnow: (changes are around lines 1708 - 1711)
         * Old code:
         * bufferbuilder.pos((double)l1 - d3 + 0.5D, (double)l2, (double)k1 - d4 + 0.5D).tex(0.0D, (double)k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
         * bufferbuilder.pos((double)l1 + d3 + 0.5D, (double)l2, (double)k1 + d4 + 0.5D).tex(1.0D, (double)k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
         * bufferbuilder.pos((double)l1 + d3 + 0.5D, (double)k2, (double)k1 + d4 + 0.5D).tex(1.0D, (double)l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
         * bufferbuilder.pos((double)l1 - d3 + 0.5D, (double)k2, (double)k1 - d4 + 0.5D).tex(0.0D, (double)l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
         *
         * New code:
         *
         */
        if(checkMethod(insn, obfuscated ? "func_181666_a" : "color")) {
            return true;
        }

        return false;
    }
}
