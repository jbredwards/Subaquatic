/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.item;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Add new config option to toggle the exp bottle enchantment glint
 * @author jbred
 *
 */
public final class PluginItemExpBottle implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * hasEffect:
         * New code:
         * //add new config option to toggle the exp bottle enchantment glint
         * public boolean hasEffect(ItemStack stack)
         * {
         *     return Hooks.hasEffect(stack);
         * }
         */
        overrideMethod(classNode, method -> method.name.equals(obfuscated ? "func_77636_d" : "hasEffect"),
            "hasEffect", "(Lnet/minecraft/item/ItemStack;)Z", generator -> generator.visitVarInsn(ALOAD, 1));

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static boolean hasEffect(@Nonnull ItemStack stack) {
            return SubaquaticConfigHandler.Client.Item.expBottleEnchantGlint || stack.isItemEnchanted();
        }
    }
}
