package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.block;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Fix grass & mycelium growing underwater, also fixes MC-130137
 * @author jbred
 *
 */
public final class PluginBlockGrass implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_180650_b" : "updateTick"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * updateTick: (changes are around line 46)
         * Old code:
         * if (worldIn.getLightFromNeighbors(pos.up()) < 4 && worldIn.getBlockState(pos.up()).getLightOpacity(worldIn, pos.up()) > 2)
         * {
         *     ...
         * }
         *
         * New code:
         * if (worldIn.getBlockState(pos.up()).getLightOpacity(worldIn, pos.up()) > 0)
         * {
         *     ...
         * }
         */
        if(checkMethod(getPrevious(insn, 2), obfuscated ? "func_175671_l" : "getLightFromNeighbors") && insn.getPrevious().getOpcode() == ICONST_4) removeFrom(instructions, insn, -5);
        else if(insn.getOpcode() == ICONST_2) {
            final boolean isFinished = insn.getNext().getOpcode() == IF_ICMPGT;
            instructions.insert(insn, new InsnNode(ICONST_0));
            instructions.remove(insn);
            return isFinished;
        }

        return false;
    }
}
