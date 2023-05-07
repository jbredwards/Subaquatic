package git.jbredwards.subaquatic.mod.asm.plugin.modded;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import net.minecraft.entity.item.EntityItem;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

/**
 * Certain modded item classes need this due to certain fluid interactions, examples:
 * <p>-applied energistics 2 crystal seeds
 * <p>-astral sorcery rock crystals and crystal tools
 * @author jbred
 *
 */
public final class PluginNoItemBobbing implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        classNode.interfaces.add("git/jbredwards/subaquatic/api/item/IFloatingBehavior");
        /*
         * New code:
         * //certain items don't bob at the surface
         * @ASMGenerated
         * public void doEntityFloat(EntityItem entityItem)
         * {
         *     Hooks.doEntityFloat(entityItem);
         * }
         */
        addMethod(classNode, "doEntityFloat", "(Lnet/minecraft/entity/item/EntityItem;)V",
            "doEntityFloat", "(Lnet/minecraft/entity/item/EntityItem;)V",
                generator -> generator.visitVarInsn(ALOAD, 1)
        );

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static void doEntityFloat(@Nonnull EntityItem entityItem) {
            entityItem.motionX *= 0.99;
            entityItem.motionY *= 0.98;
            entityItem.motionZ *= 0.99;
        }
    }
}
