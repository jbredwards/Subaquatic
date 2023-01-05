package git.jbredwards.subaquatic.mod.asm.plugin.vanilla;

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
public final class PluginGenLayer implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_180781_a" : "initializeAllBiomeGenerators"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * initializeAllBiomeGenerators: (changes are around line 86)
         * Old code:
         * GenLayer genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
         *
         * New code:
         * //apply ocean biome generator
         * GenLayer genlayerrivermix = Hooks.applyOceanBiomes(new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth));
         */
        if(insn.getOpcode() == INVOKESPECIAL && ((MethodInsnNode)insn).owner.equals("net/minecraft/world/gen/layer/GenLayerRiverMix")) {
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