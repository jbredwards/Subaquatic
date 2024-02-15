/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.entity;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import net.minecraft.entity.monster.EntityZombie;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Baby zombies burn in daylight if enabled
 * @author jbred
 *
 */
public final class PluginEntityZombie implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_70636_d" : "onLivingUpdate"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * onLivingUpdate: (changes are around line 214)
         * Old code:
         * if (this.world.isDaytime() && !this.world.isRemote && !this.isChild() && this.shouldBurnInDay())
         * {
         *     ...
         * }
         *
         * New code:
         * //baby zombies burn in daylight if enabled
         * if (this.world.isDaytime() && !this.world.isRemote && !Hooks.immuneToDaylight(this) && this.shouldBurnInDay())
         * {
         *     ...
         * }
         */
        if(checkMethod(insn, obfuscated ? "func_70631_g_" : "isChild")) {
            instructions.insert(insn, genMethodNode("immuneToDaylight", "(Lnet/minecraft/entity/monster/EntityZombie;)Z"));
            instructions.remove(insn);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static boolean immuneToDaylight(@Nonnull EntityZombie entity) {
            return !SubaquaticConfigHandler.Server.Entity.babyZombiesBurnInDaylight && entity.isChild();
        }
    }
}
