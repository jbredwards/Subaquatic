package git.jbredwards.subaquatic.mod.asm.plugin.modded;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

/**
 * Preserve betweenlands biome colors
 * @author jbred
 *
 */
public final class PluginBetweenlands implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        classNode.interfaces.add("git/jbredwards/subaquatic/api/biome/IWaterColorProvider");
        return false;
    }
}
