/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.client;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.client.audio.IPrioritySound;
import net.minecraft.client.audio.ISound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Allow for sounds with a high priority
 * @author jbred
 *
 */
public final class PluginSoundManager implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_148611_c" : "playSound"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * playSound: (changes are around lines 439 & 444)
         * Old code:
         * this.sndSystem.newStreamingSource(false, ...); //line 439
         * this.sndSystem.newSource(false, ...);          //line 444
         *
         * New code:
         * //allow for sounds with a high priority
         * this.sndSystem.newStreamingSource(Hooks.isPrioritySound(p_sound), ...); //line 439
         * this.sndSystem.newSource(Hooks.isPrioritySound(p_sound), ...);          //line 444
         */
        if(insn.getOpcode() == ICONST_0) {
            instructions.insert(insn, genMethodNode("isPrioritySound", "(Lnet/minecraft/client/audio/ISound;)Z"));
            instructions.insert(insn, new VarInsnNode(ALOAD, 1));
            instructions.remove(insn);
            return checkMethod(getNext(insn, 17), "newSource");
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @SideOnly(Side.CLIENT)
        public static boolean isPrioritySound(@Nonnull ISound sound) {
            return sound instanceof IPrioritySound && ((IPrioritySound)sound).isPriority();
        }
    }
}
