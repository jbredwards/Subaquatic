package git.jbredwards.subaquatic.mod.asm;

import git.jbredwards.fluidlogged_api.api.asm.AbstractClassTransformer;
import git.jbredwards.fluidlogged_api.api.asm.BasicLoadingPlugin;
import git.jbredwards.subaquatic.mod.asm.plugin.vanilla.world.PluginGenLayerDeepOcean;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@BasicLoadingPlugin.Name("Subaquatic Plugin")
@BasicLoadingPlugin.MCVersion("1.12.2")
@BasicLoadingPlugin.SortingIndex(1001)
public final class ASMHandler implements BasicLoadingPlugin
{
    @SuppressWarnings("unused")
    public static final class Transformer extends AbstractClassTransformer
    {
        public Transformer() {
            //vanilla
            plugins.put("net.minecraft.world.gen.layer.GenLayerDeepOcean", new PluginGenLayerDeepOcean());
        }

        @Nonnull
        @Override
        public String getPluginName() { return "Subaquatic Plugin"; }
    }
}
