/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.modded;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

/**
 * Remove duplicate mod functionality
 * @author jbred
 *
 */
public final class PluginBurnBabyBurn implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        classNode.methods.removeIf(method -> method.name.equals("init"));
        return false;
    }
}
