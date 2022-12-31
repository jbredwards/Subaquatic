package git.jbredwards.subaquatic.mod.asm.plugin.vanilla;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Water droplet particles keep the color set by this mod
 * @author jbred
 *
 */
public final class PluginParticleDrip implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return checkMethod(method, obfuscated ? "func_189213_a" : "onUpdate", "()V"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * onUpdate: (changes are around lines 65-67)
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
        if(insn.getOpcode() == IF_ACMPNE) {
            //gets each node that sets the rgb for the particle
            final AbstractInsnNode redNode = getNext(insn, 5);
            final AbstractInsnNode greenNode = getNext(redNode, 5);
            final AbstractInsnNode blueNode = getNext(greenNode, 5);
            //removes the red color change
            instructions.remove(redNode.getPrevious().getPrevious());
            instructions.remove(redNode.getPrevious());
            instructions.remove(redNode);
            //removes the green color change
            instructions.remove(greenNode.getPrevious().getPrevious());
            instructions.remove(greenNode.getPrevious());
            instructions.remove(greenNode);
            //removes the blue color change
            instructions.remove(blueNode.getPrevious().getPrevious());
            instructions.remove(blueNode.getPrevious());
            instructions.remove(blueNode);

            return true;
        }

        return false;
    }
}
