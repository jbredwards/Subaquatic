package git.jbredwards.subaquatic.mod.asm.plugin.modded;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.asm.plugin.vanilla.world.PluginGenLayerRiverMix;
import net.minecraft.world.gen.layer.GenLayer;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

/**
 * Account for all ocean biomes when generating rivers
 * @author jbred
 *
 */
public final class PluginBOPGenLayerRiverMix implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * New code:
         * //account for all ocean biomes when generating rivers
         * public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
         * {
         *     return Hooks.getInts(areaX, areaY, areaWidth, areaHeight, biomesBranch, this.riversBranch);
         * }
         */
        overrideMethod(classNode, method -> method.name.equals(obfuscated ? "func_75904_a" : "getInts"), "getInts", "(IIIILnet/minecraft/world/gen/layer/GenLayer;Lnet/minecraft/world/gen/layer/GenLayer;)[I", generator -> {
            generator.visitVarInsn(ILOAD, 1);
            generator.visitVarInsn(ILOAD, 2);
            generator.visitVarInsn(ILOAD, 3);
            generator.visitVarInsn(ILOAD, 4);
            generator.visitVarInsn(ALOAD, 0);
            generator.visitFieldInsn(GETFIELD, "biomesoplenty/common/world/layer/GenLayerRiverMixBOP", "biomesBranch", "Lnet/minecraft/world/gen/layer/GenLayer;");
            generator.visitVarInsn(ALOAD, 0);
            generator.visitFieldInsn(GETFIELD, "biomesoplenty/common/world/layer/GenLayerRiverMixBOP", "riversBranch", "Lnet/minecraft/world/gen/layer/GenLayer;");
        });

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight, @Nonnull GenLayer biomePatternGeneratorChain, @Nonnull GenLayer riverPatternGeneratorChain) {
            return PluginGenLayerRiverMix.Hooks.getInts(areaX, areaY, areaWidth, areaHeight, biomePatternGeneratorChain, riverPatternGeneratorChain);
        }
    }
}
