package git.jbredwards.subaquatic.mod.asm.plugin.vanilla;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Account for modded shallow oceans
 * @author jbred
 *
 */
public final class PluginGenLayerEdge implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_151625_e" : "getIntsSpecial"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * getIntsSpecial: (changes are around layer 119)
         * Old code:
         * if (k != 0 && this.nextInt(13) == 0)
         * {
         *     ...
         * }
         *
         * New code:
         * //account for modded shallow oceans
         * if (!IOceanBiome.isShallowOcean(k) && this.nextInt(13) == 0)
         * {
         *     ...
         * }
         */
        if(insn.getOpcode() == ILOAD && ((VarInsnNode)insn).var == 9) {
            ((JumpInsnNode)insn.getNext()).setOpcode(IFNE);
            instructions.insert(insn, genMethodNode("git/jbredwards/subaquatic/api/biome/IOceanBiome", "isShallowOcean", "(I)Z"));
            return true;
        }

        return false;
    }
}
