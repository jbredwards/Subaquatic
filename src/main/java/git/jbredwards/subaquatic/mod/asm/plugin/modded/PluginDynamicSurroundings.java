package git.jbredwards.subaquatic.mod.asm.plugin.modded;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import javax.annotation.Nonnull;

/**
 * Water droplet particles keep the color set by this mod, and account for FluidStates when creating steam
 * @author jbred
 *
 */
public final class PluginDynamicSurroundings implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return checkMethod(method, obfuscated ? "func_189213_a" : "onUpdate", "()V"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * onUpdate:
         * Old code:
         * {
         *     this.particleRed = 0.2F;
         *     this.particleGreen = 0.3F;
         *     this.particleBlue = 1.0F;
         * }
         *
         * New code:
         * //remove hardcoded colors
         * {
         *
         * }
         */
        if(insn.getPrevious() != null && insn.getPrevious().getOpcode() == IF_ACMPNE && checkField(getPrevious(insn, 2), obfuscated ? "field_151586_h" : "WATER")) {
            while(insn.getOpcode() != GOTO) {
                insn = insn.getNext();
                instructions.remove(insn.getPrevious());
            }
        }
        /*
         * onUpdate:
         * Old code:
         * IBlockState state = ClientChunkCache.instance().getBlockState(this.pos);
         *
         * New code:
         * //account for FluidStates
         * IBlockState state = FluidloggedUtils.getFluidOrReal(ClientChunkCache.instance(), this.pos);
         */
        else if(checkMethod(insn, obfuscated ? "func_180495_p" : "getBlockState")) {
            instructions.insert(insn, genMethodNode("git/jbredwards/fluidlogged_api/api/util/FluidloggedUtils", "getFluidOrReal", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;"));
            instructions.remove(insn);
            return true;
        }

        return false;
    }
}
