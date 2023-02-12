package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.entity;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * XP Orbs float while in water and have a proper eye height
 * @author jbred
 *
 */
public final class PluginEntityXPOrb implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_70071_h_" : "onUpdate"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * onUpdate: (changes are around line 118)
         * Old code:
         * if (!this.hasNoGravity())
         * {
         *     ...
         * }
         *
         * New code:
         * //xp orbs float in water
         * if(!Hooks.doesFloat(this))
         * {
         *     ...
         * }
         */
        if(checkMethod(insn, obfuscated ? "func_189652_ae" : "hasNoGravity")) {
            instructions.insert(insn, genMethodNode("git/jbredwards/subaquatic/mod/asm/plugin/vanilla/entity/PluginEntityItem$Hooks", "doesFloat", "(Lnet/minecraft/entity/Entity;)Z"));
            instructions.remove(insn);
            return true;
        }

        return false;
    }

    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * New code:
         * //xp orbs have a proper eye height
         * @ASMGenerated
         * public float getEyeHeight()
         * {
         *     return 0.15f;
         * }
         */
        addMethod(classNode, obfuscated ? "func_70047_e" : "getEyeHeight", "()F", null, null, generator -> generator.visitLdcInsn(0.15f));
        return true;
    }
}
