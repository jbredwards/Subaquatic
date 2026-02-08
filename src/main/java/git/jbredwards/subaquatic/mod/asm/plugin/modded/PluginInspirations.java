/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.modded;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Place fish contained within bucket
 * @author jbred
 *
 */
public final class PluginInspirations implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals("interact"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * interact:
         * Old code:
         * SoundEvent sound = recipe.getSound(stack, boiling, level, state);
         *
         * New code:
         * //place fish contained within bucket
         * SoundEvent sound = recipe.getSound(stack, boiling, level, state);
         * PluginBlockCauldron.Hooks.placeCapturedEntity(player, world, pos, stack);
         */
        if(checkMethod(insn.getPrevious(), "getSound")) {
            final InsnList list = new InsnList();
            list.add(new VarInsnNode(ALOAD, 3));
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new VarInsnNode(ALOAD, 5));
            list.add(genMethodNode("git/jbredwards/subaquatic/mod/asm/plugin/vanilla/block/PluginBlockCauldron$Hooks", "placeCapturedEntity", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V"));

            instructions.insert(insn, list);
            return true;
        }

        return false;
    }
}
