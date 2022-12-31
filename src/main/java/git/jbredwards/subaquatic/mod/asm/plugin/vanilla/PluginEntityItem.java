package git.jbredwards.subaquatic.mod.asm.plugin.vanilla;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Items and XP orbs float while in water
 * @author jbred
 *
 */
public final class PluginEntityItem implements IASMPlugin
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
         * //items float in water
         * if(!Hooks.doesFloat(this))
         * {
         *     ...
         * }
         */
        if(checkMethod(insn, obfuscated ? "func_189652_ae" : "hasNoGravity")) {
            instructions.insert(insn, genMethodNode("doesFloat", "(Lnet/minecraft/entity/Entity;)Z"));
            instructions.remove(insn);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static boolean doesFloat(@Nonnull Entity entity) {
            if(entity.isInsideOfMaterial(Material.WATER)) {
                if(entity.motionY < 0.06) entity.motionY += 5.0E-4;
                entity.motionX *= 0.99;
                entity.motionZ *= 0.99;

                return true;
            }

            return entity.hasNoGravity();
        }
    }
}
