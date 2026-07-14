/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.world;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Use new biome class for frozen oceans
 * @author jbred
 *
 */
public final class PluginBiome implements IASMPlugin
{
    @Override
    public int getMethodIndex(@Nonnull MethodNode method, boolean obfuscated) {
        return method.name.equals(obfuscated ? "func_185358_q" : "registerBiomes") ? 2 : 0;
    }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * registerBiomes: (changes are around line 562)
         * Old code:
         * registerBiome(10, "frozen_ocean", new BiomeOcean((new Biome.BiomeProperties("FrozenOcean")).setBaseHeight(-1.0F).setHeightVariation(0.1F).setTemperature(0.0F).setRainfall(0.5F).setSnowEnabled()));
         *
         * New code:
         * //replace old biome class with new one
         * registerBiome(10, "frozen_ocean", new BiomeFrozenOcean((new Biome.BiomeProperties("FrozenOcean")).setBaseHeight(-1.0F).setHeightVariation(0.1F).setTemperature(0.0F).setRainfall(0.5F).setSnowEnabled()));
         */
        if(index == 2 && insn.getOpcode() == LDC && ((LdcInsnNode)insn).cst.equals("frozen_ocean")) {
            ((TypeInsnNode)insn.getNext()).desc = "git/jbredwards/subaquatic/mod/common/world/biome/BiomeFrozenOcean";
            ((MethodInsnNode)getNext(insn, 16)).owner = "git/jbredwards/subaquatic/mod/common/world/biome/BiomeFrozenOcean";
            return true;
        }

        return false;
    }
}
