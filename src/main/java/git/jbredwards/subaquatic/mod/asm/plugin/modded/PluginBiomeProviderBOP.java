package git.jbredwards.subaquatic.mod.asm.plugin.modded;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.common.world.gen.layer.GenLayerOceanBiomes;
import net.minecraft.world.gen.layer.GenLayer;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Apply ocean biome generator
 * @author jbred
 *
 */
public final class PluginBiomeProviderBOP implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals("setupBOPGenLayers"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * setupBOPGenLayers:
         * Old code:
         * GenLayer riverMixFinal = new GenLayerRiverMixBOP(100L, mainBranch, riversBranch);
         *
         * New code:
         * //apply ocean biome generator
         * GenLayer riverMixFinal = Hooks.applyOceanBiomes(new GenLayerRiverMixBOP(100L, mainBranch, riversBranch));
         */
        if(insn.getOpcode() == INVOKESPECIAL && ((MethodInsnNode)insn).owner.equals("biomesoplenty/common/world/layer/GenLayerRiverMixBOP")) {
            instructions.insert(insn, genMethodNode("applyOceanBiomes", "(Lnet/minecraft/world/gen/layer/GenLayer;)Lnet/minecraft/world/gen/layer/GenLayer;"));
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @Nonnull
        public static GenLayer applyOceanBiomes(@Nonnull GenLayer riverLayer) {
            return new GenLayerOceanBiomes(2, riverLayer);
        }
    }
}
