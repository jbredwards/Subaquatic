/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.item;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Add new config option to toggle the potion enchantment glint
 * @author jbred
 *
 */
public final class PluginItemPotion implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_77636_d" : "hasEffect"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * hasEffect: (changes are around line 161)
         * Old code:
         * return super.hasEffect(stack) || !PotionUtils.getEffectsFromStack(stack).isEmpty();
         *
         * New code:
         * //add new config option to toggle the potion enchantment glint
         * return super.hasEffect(stack) || !Hooks.doesPotionHaveNoGlint(stack);
         */
        if(checkMethod(insn, "isEmpty")) {
            instructions.insert(insn, genMethodNode("doesPotionHaveNoGlint", "(Lnet/minecraft/item/ItemStack;)Z"));
            removeFrom(instructions, insn, -1);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static boolean doesPotionHaveNoGlint(@Nonnull ItemStack stack) {
            return !SubaquaticConfigHandler.Client.Item.potionEnchantGlint || PotionUtils.getEffectsFromStack(stack).isEmpty();
        }
    }
}
