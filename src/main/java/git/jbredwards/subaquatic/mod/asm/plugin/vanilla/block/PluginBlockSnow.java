package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.block;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Prevent snow layers from being placeable on blue ice
 * @author jbred
 *
 */
public final class PluginBlockSnow implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_176196_c" : "canPlaceBlockAt"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * canPlaceBlockAt: (changes are around line 121)
         * Old code:
         * if (block != Blocks.ICE && block != Blocks.PACKED_ICE && block != Blocks.BARRIER)
         * {
         *     ...
         * }
         *
         * New code:
         * //prevent snow layers from being placeable on blue ice
         * if (block != Blocks.ICE && block != Blocks.PACKED_ICE && block != SubaquaticBlocks.BLUE_ICE && block != Blocks.BARRIER)
         * {
         *     ...
         * }
         */
        if(checkField(insn.getPrevious(), obfuscated ? "field_150403_cj" : "PACKED_ICE")) {
            final InsnList list = new InsnList();
            list.add(new VarInsnNode(ALOAD, 4));
            list.add(new FieldInsnNode(GETSTATIC, "git/jbredwards/subaquatic/mod/common/init/SubaquaticBlocks", "BLUE_ICE", "Lnet/minecraft/block/BlockPackedIce;"));
            list.add(new JumpInsnNode(IF_ACMPEQ, ((JumpInsnNode)insn).label));

            instructions.insert(insn, list);
            return true;
        }

        return false;
    }
}
