package git.jbredwards.subaquatic.mod.asm.plugin.vanilla;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Update pumpkin reference to the correct block
 * @author jbred
 *
 */
public final class PluginBlockStem implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_176481_j" : "getSeedItem"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * getSeedItem: (changes are around line 156)
         * Old code:
         * if (this.crop == Blocks.PUMPKIN)
         * {
         *     ...
         * }
         *
         * New code:
         * //update pumpkin reference to the correct block
         * if (this.crop == SubaquaticBlocks.PUMPKIN)
         * {
         *     ...
         * }
         */
        if(checkField(insn, obfuscated ? "field_150423_aK" : "PUMPKIN")) {
            instructions.insert(insn, new FieldInsnNode(GETSTATIC, "git/jbredwards/subaquatic/mod/common/init/SubaquaticBlocks", "PUMPKIN", "Lgit/jbredwards/subaquatic/mod/common/block/BlockCarvablePumpkin;"));
            instructions.remove(insn);
            return true;
        }

        return false;
    }
}
