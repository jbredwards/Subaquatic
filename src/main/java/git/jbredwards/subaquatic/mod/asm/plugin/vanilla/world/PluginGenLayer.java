/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.world;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Optimize GenLayer::isBiomeOceanic
 * @author jbred
 *
 */
public final class PluginGenLayer implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * isBiomeOceanic:
         * New code:
         * //heavily boost performance
         * protected static boolean isBiomeOceanic(int p_151618_0_)
         * {
         *     return IOceanBiome.isOcean(p_151618_0_);
         * }
         */
        overrideMethod(classNode, method -> method.name.equals(obfuscated ? "func_151618_b" : "isBiomeOceanic"), null, null, generator -> {
            generator.visitVarInsn(ILOAD, 0);
            generator.visitMethodInsn(INVOKESTATIC, "git/jbredwards/subaquatic/api/biome/IOceanBiome", "isOcean", "(I)Z", false);
        });

        return false;
    }
}
