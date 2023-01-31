package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.block;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Implement IChorusPlantSoil functionality
 * @author jbred
 *
 */
public final class PluginBlockChorusFlower implements IASMPlugin
{
    @Override
    public int getMethodIndex(@Nonnull MethodNode method, boolean obfuscated) {
        if(method.name.equals(obfuscated ? "func_180650_b" : "updateTick")) return 1;
        else return method.name.equals(obfuscated ? "func_185606_b" : "canSurvive") ? 2 : 0;
    }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        if(checkField(insn, obfuscated ? "field_150377_bs" : "END_STONE")) {
            /*
             * updateTick: (changes are around line 68)
             * Old code:
             * if (block == Blocks.END_STONE)
             * {
             *     ...
             * }
             *
             * New code:
             * //implement IChorusPlantSoil functionality
             * if (IChorusPlantSoil.isSoil(iblockstate))
             * {
             *     ...
             * }
             */
            if(index == 1) {
                ((JumpInsnNode)insn.getNext()).setOpcode(IFEQ);
                instructions.insert(insn, genMethodNode("git/jbredwards/subaquatic/api/block/IChorusPlantSoil", "isSoil", "(Lnet/minecraft/block/state/IBlockState;)Z"));
                instructions.insert(insn, new VarInsnNode(ALOAD, 9));
                removeFrom(instructions, insn, -1);
            }

            /*
             * canSurvive: (changes are around line 224)
             * Old code:
             * if (block != Blocks.CHORUS_PLANT && block != Blocks.END_STONE)
             * {
             *     ...
             * }
             *
             * New code:
             * //implement IChorusPlantSoil functionality
             * if (block != Blocks.CHORUS_PLANT && !IChorusPlantSoil.isSoil(iblockstate))
             * {
             *     ...
             * }
             */
            else if(index == 2) {
                ((JumpInsnNode)insn.getNext()).setOpcode(IFNE);
                instructions.insert(insn, genMethodNode("git/jbredwards/subaquatic/api/block/IChorusPlantSoil", "isSoil", "(Lnet/minecraft/block/state/IBlockState;)Z"));
                instructions.insert(insn, new VarInsnNode(ALOAD, 3));
                removeFrom(instructions, insn, -1);
            }

            return true;
        }

        return false;
    }
}
