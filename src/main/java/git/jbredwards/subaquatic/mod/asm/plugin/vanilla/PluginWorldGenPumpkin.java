package git.jbredwards.subaquatic.mod.asm.plugin.vanilla;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Generate non-carved pumpkins instead of carved ones
 * @author jbred
 *
 */
public final class PluginWorldGenPumpkin implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_180709_b" : "generate"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * generate: (changes are around line 18)
         * Old code:
         * worldIn.setBlockState(blockpos, Blocks.PUMPKIN.getDefaultState().withProperty(BlockPumpkin.FACING, EnumFacing.Plane.HORIZONTAL.random(rand)), 2);
         *
         * New code:
         * //generate non-carved pumpkins instead of carved ones
         * worldIn.setBlockState(blockpos, SubaquaticBlocks.PUMPKIN.getDefaultState(), 2);
         */
        if(checkField(insn, obfuscated ? "field_150423_aK" : "PUMPKIN")) {
            removeFrom(instructions, getNext(insn, 2), 4);
            instructions.insertBefore(insn, new FieldInsnNode(GETSTATIC, "git/jbredwards/subaquatic/mod/common/init/SubaquaticBlocks", "PUMPKIN", "Lgit/jbredwards/subaquatic/mod/common/block/BlockCarvablePumpkin;"));
            instructions.remove(insn);
            return true;
        }

        return false;
    }
}
