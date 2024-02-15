/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.world;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.api.event.OnCreateChunkFromPrimerEvent;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.common.MinecraftForge;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Add call to {@link OnCreateChunkFromPrimerEvent}
 * @author jbred
 *
 */
public final class PluginChunk implements IASMPlugin
{
    //=======================
    //EXPERIMENTAL CODE BELOW
    //=======================

    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return checkMethod(method, "<init>", "(Lnet/minecraft/world/World;Lnet/minecraft/world/chunk/ChunkPrimer;II)V"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * Constructor: (changes are around line 120)
         * Old code:
         * this(worldIn, x, z);
         *
         * New code:
         * //add call to OnCreateChunkFromPrimerEvent
         * this(worldIn, x, z);
         * Hooks.onCreateChunkFromPrimer(this, primer);
         */
        if(insn.getOpcode() == INVOKESPECIAL) {
            instructions.insert(insn, genMethodNode("onCreateChunkFromPrimer", "(Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/world/chunk/ChunkPrimer;)V"));
            instructions.insert(insn, new VarInsnNode(ALOAD, 2));
            instructions.insert(insn, new VarInsnNode(ALOAD, 0));
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static void onCreateChunkFromPrimer(@Nonnull Chunk chunk, @Nonnull ChunkPrimer primer) {
            MinecraftForge.TERRAIN_GEN_BUS.post(new OnCreateChunkFromPrimerEvent(chunk, primer));
        }
    }
}
