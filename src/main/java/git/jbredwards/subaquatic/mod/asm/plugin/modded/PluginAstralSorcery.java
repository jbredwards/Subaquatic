/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.modded;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import net.minecraft.entity.item.EntityItem;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

/**
 * Stardust don't bob at the surface
 * @author jbred
 *
 */
public final class PluginAstralSorcery implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        classNode.interfaces.add("git/jbredwards/subaquatic/api/item/IFloatingBehavior");
        /*
         * New code:
         * //stardust don't bob at the surface
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
            if(entityItem.getItem().getItemDamage() == 2) entityItem.motionY *= 0.98;
            else if(entityItem.motionY < 0.06) entityItem.motionY += 5.0E-4;

            entityItem.motionX *= 0.99;
            entityItem.motionZ *= 0.99;
        }
    }
}
