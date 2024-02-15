/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.modded;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.fluidlogged_api.mod.asm.plugins.vanilla.block.PluginBlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.IPlantable;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * The Underground Biomes mod accounts for modded BlockBush instances
 * @author jbred
 *
 */
public final class PluginUndergroundBiomes implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals("canSustainPlant"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * canSustainPlant:
         * Old code:
         * return false;
         *
         * New code:
         * //account for modded BlockBush instances
         * return Hooks.canSustainBush(state, plantable);
         */
        if(insn.getOpcode() == ICONST_0 && insn.getNext().getOpcode() == IRETURN) {
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 1));
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 5));
            instructions.insertBefore(insn, genMethodNode("canSustainBush", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraftforge/common/IPlantable;)Z"));
            instructions.remove(insn);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static boolean canSustainBush(@Nonnull IBlockState soil, @Nonnull IPlantable plant) {
            return plant instanceof PluginBlockBush.Accessor && ((PluginBlockBush.Accessor)plant).canSustainBush_Public(soil);
        }
    }
}
