package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.block;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

/**
 * Allow pumpkins to be placed anywhere
 * @author jbred
 *
 */
public final class PluginBlockPumpkin implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        classNode.methods.removeIf(method -> method.name.equals(obfuscated ? "func_176196_c" : "canPlaceBlockAt"));
        return false;
    }
}
