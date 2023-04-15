package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.potion;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Use water fluid color instead of hardcoded one
 * @author jbred
 *
 */
public final class PluginPotionUtils implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_185181_a" : "getPotionColorFromEffectList"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * getPotionColorFromEffectList: (changes are around line 108)
         * Old code:
         * return 3694022;
         *
         * New code:
         * //use water fluid color
         * return FluidRegistry.WATER.getColor();
         */
        if(insn.getOpcode() == LDC && insn.getNext().getOpcode() == IRETURN) {
            instructions.insertBefore(insn, new FieldInsnNode(GETSTATIC, "net/minecraftforge/fluids/FluidRegistry", "WATER", "Lnet/minecraftforge/fluids/Fluid;"));
            instructions.insertBefore(insn, new MethodInsnNode(INVOKEVIRTUAL, "net/minecraftforge/fluids/Fluid", "getColor", "()I", false));
            instructions.remove(insn);
            return true;
        }

        return false;
    }
}
