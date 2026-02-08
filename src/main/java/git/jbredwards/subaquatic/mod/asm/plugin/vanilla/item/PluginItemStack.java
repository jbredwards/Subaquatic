/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.item;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import net.minecraft.entity.player.EntityPlayer;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Temporarily cache active player
 * @author jbred
 *
 */
public final class PluginItemStack implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull final MethodNode method, final boolean obfuscated) { return method.name.equals(obfuscated ? "func_77957_a" : "useItemRightClick"); }

    @Override
    public boolean transform(@Nonnull final InsnList instructions, @Nonnull final MethodNode method, @Nonnull final AbstractInsnNode insn, final boolean obfuscated, final int index) {
        /*
         * Old code: (changes are around line 254)
         * return this.getItem().onItemRightClick(worldIn, playerIn, hand);
         *
         * New code:
         * // Temporarily cache active player.
         * Hooks.USING_PLAYER.set(playerIn);
         * ActionResult<ItemStack> active = this.getItem().onItemRightClick(worldIn, playerIn, hand);
         * Hooks.USING_PLAYER.set(null);
         * return active;
         */
        if(insn == instructions.getFirst().getNext()) {
            instructions.insertBefore(insn, new FieldInsnNode(GETSTATIC, getHookClass(), "USING_PLAYER", "Ljava/lang/ThreadLocal;"));
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 2));
            instructions.insertBefore(insn, new MethodInsnNode(INVOKEVIRTUAL, "java/lang/ThreadLocal", "set", "(Ljava/lang/Object;)V", false));
        }
        else if(insn.getOpcode() == ARETURN) {
            instructions.insertBefore(insn, new VarInsnNode(ASTORE, 10));
            instructions.insertBefore(insn, new FieldInsnNode(GETSTATIC, getHookClass(), "USING_PLAYER", "Ljava/lang/ThreadLocal;"));
            instructions.insertBefore(insn, new InsnNode(ACONST_NULL));
            instructions.insertBefore(insn, new MethodInsnNode(INVOKEVIRTUAL, "java/lang/ThreadLocal", "set", "(Ljava/lang/Object;)V", false));
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 10));
        }

        return false;
    }

    @Override
    public boolean addLocalVariables(@Nonnull MethodNode method, @Nonnull LabelNode start, @Nonnull LabelNode end, int index) {
        method.localVariables.add(new LocalVariableNode("action", "Lnet/minecraft/util/ActionResult;", "Lnet/minecraft/util/ActionResult<Lnet/minecraft/item/ItemStack;>;", start, end, 10));
        return true;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @Nonnull
        public static final ThreadLocal<EntityPlayer> USING_PLAYER = new ThreadLocal<>();
    }
}
