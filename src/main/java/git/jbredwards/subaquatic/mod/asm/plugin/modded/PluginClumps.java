package git.jbredwards.subaquatic.mod.asm.plugin.modded;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Remove Clumps mod XP orb render override and allow Clumps mod XP orbs to float while in water
 * @author jbred
 *
 */
public final class PluginClumps implements IASMPlugin
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
         * //don't apply gravity to Clumps mod xp orbs while they're in water
         * if(!Hooks.hasNoGravityOrInWater(this))
         * {
         *     ...
         * }
         */
        if(checkMethod(insn, obfuscated ? "func_189652_ae" : "hasNoGravity")) {
            instructions.insert(insn, genMethodNode("hasNoGravityOrInWater", "(Lnet/minecraft/entity/Entity;)Z"));
            instructions.remove(insn);
            return true;
        }

        return false;
    }

    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        return !classNode.methods.removeIf(method -> method.name.equals("registerRenders"));
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static boolean hasNoGravityOrInWater(@Nonnull Entity entity) {
            return entity.hasNoGravity() || entity.isInsideOfMaterial(Material.WATER);
        }
    }
}
