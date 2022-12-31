package git.jbredwards.subaquatic.mod.asm.plugin.vanilla;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Get the biome colors from the radius specified in the config
 * @author jbred
 *
 */
public final class PluginBiomeColorHelper implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return checkMethod(method, obfuscated ? "func_180285_a" : "getColorAtPos", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/biome/BiomeColorHelper$ColorResolver;)I"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * getColorAtPos: (changes are around line 37)
         * Old code:
         * int k = 0;
         *
         * New code:
         * //initialize new local var
         * int k = 0;
         * int total = 0;
         */
        if(insn.getOpcode() == ISTORE && ((VarInsnNode)insn).var == 5 && insn.getPrevious().getOpcode() == ICONST_0) {
            instructions.insert(insn, new VarInsnNode(ISTORE, 11));
            instructions.insert(insn, new InsnNode(ICONST_0));
        }
        /*
         * getColorAtPos: (changes are around line 39)
         * Old code:
         * for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-1, 0, -1), pos.add(1, 0, 1)))
         * {
         *     ...
         * }
         *
         * New code:
         * //get the biome colors from the radius specified in the config
         * for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-SubaquaticConfigHandler.biomeColorBlendRadius, 0, -SubaquaticConfigHandler.biomeColorBlendRadius), pos.add(SubaquaticConfigHandler.biomeColorBlendRadius, 0, SubaquaticConfigHandler.biomeColorBlendRadius)))
         * {
         *     ...
         * }
         */
        else if(insn.getOpcode() == ICONST_M1) {
            instructions.insertBefore(insn, new FieldInsnNode(GETSTATIC, "git/jbredwards/subaquatic/mod/common/config/SubaquaticConfigHandler", "biomeColorBlendRadius", "I"));
            instructions.insert(insn, new InsnNode(IMUL));
        }
        else if(insn.getOpcode() == ICONST_1) {
            instructions.insert(insn, new FieldInsnNode(GETSTATIC, "git/jbredwards/subaquatic/mod/common/config/SubaquaticConfigHandler", "biomeColorBlendRadius", "I"));
            instructions.remove(insn);
        }
        /*
         * getColorAtPos: (changes are around line 44)
         * Old code:
         * k += l & 255;
         *
         * New code:
         * //increment total colors taking into account
         * k += l & 255;
         * total += 1;
         */
        else if(insn.getOpcode() == ISTORE && ((VarInsnNode)insn).var == 5) {
            final InsnList list = new InsnList();
            list.add(new VarInsnNode(ILOAD, 11));
            list.add(new InsnNode(ICONST_1));
            list.add(new InsnNode(IADD));
            list.add(new VarInsnNode(ISTORE, 11));
            instructions.insert(insn, list);
        }
        /*
         * getColorAtPos: (changes are around line 47)
         * Old code:
         * return (i / 9 & 255) << 16 | (j / 9 & 255) << 8 | k / 9 & 255;
         *
         * New code:
         * //get the biome colors from the radius specified in the config
         * return Hooks.doAccurateBiomeBlend(i, j, k, total);
         */
        else if(insn.getNext().getOpcode() == IRETURN) {
            final InsnList list = new InsnList();
            list.add(new VarInsnNode(ILOAD, 3));
            list.add(new VarInsnNode(ILOAD, 4));
            list.add(new VarInsnNode(ILOAD, 5));
            list.add(new VarInsnNode(ILOAD, 11));
            list.add(genMethodNode("doAccurateBiomeBlend", "(IIII)I"));
            instructions.insert(insn, list);
            removeFrom(instructions, insn, -20);
            return true;
        }

        return false;
    }

    @Override
    public boolean addLocalVariables(@Nonnull MethodNode method, @Nonnull LabelNode start, @Nonnull LabelNode end, int index) {
        method.localVariables.add(new LocalVariableNode("total", "I", null, start, end, 11));
        return true;
    }

    @Override
    public boolean recalcFrames(boolean obfuscated) { return true; }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static int doAccurateBiomeBlend(int r, int g, int b, int total) {
            return (r / total & 255) << 16 | (g / total & 255) << 8 | b / total & 255;
        }
    }
}
