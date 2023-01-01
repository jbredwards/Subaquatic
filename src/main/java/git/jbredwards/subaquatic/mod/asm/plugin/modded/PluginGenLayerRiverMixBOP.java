package git.jbredwards.subaquatic.mod.asm.plugin.modded;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.asm.plugin.vanilla.PluginGenLayerRiverMix;
import net.minecraft.world.gen.layer.GenLayer;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

/**
 * Account for all ocean biomes when generating rivers
 * @author jbred
 *
 */
public final class PluginGenLayerRiverMixBOP implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * Interface handling
         */
        classNode.interfaces.add("git/jbredwards/subaquatic/mod/asm/plugin/vanilla/PluginGenLayerRiverMix$IBOPGenerator");
        classNode.methods.forEach(method -> { if(method.name.equals("biomeSupportsRivers")) method.access = ACC_PUBLIC; });
        /*
         * New code:
         * //account for all ocean biomes when generating rivers
         * public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
         * {
         *     return Hooks.getInts(this, areaX, areaY, areaWidth, areaHeight, biomesBranch, this.riversBranch);
         * }
         */
        overrideMethod(classNode, method -> method.name.equals(obfuscated ? "func_75904_a" : "getInts"), "getInts", "(Lnet/minecraft/world/gen/layer/GenLayer;IIIILnet/minecraft/world/gen/layer/GenLayer;Lnet/minecraft/world/gen/layer/GenLayer;)[I", generator -> {
            generator.visitVarInsn(ALOAD, 0);
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
        public static int[] getInts(@Nonnull GenLayer layer, int areaX, int areaY, int areaWidth, int areaHeight, @Nonnull GenLayer biomePatternGeneratorChain, @Nonnull GenLayer riverPatternGeneratorChain) {
            return PluginGenLayerRiverMix.Hooks.getInts(layer, areaX, areaY, areaWidth, areaHeight, biomePatternGeneratorChain, riverPatternGeneratorChain);
        }
    }
}
