package git.jbredwards.subaquatic.mod.asm.plugin.modded;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.asm.plugin.vanilla.world.PluginGenLayerShore;
import net.minecraft.world.gen.layer.GenLayer;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

/**
 * Account for all ocean biomes when generating shores
 * @author jbred
 *
 */
public final class PluginGenLayerShoreBOP implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * New code:
         * //account for all ocean biomes when generating shores
         * public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
         * {
         *     return Hooks.getInts(areaX, areaY, areaWidth, areaHeight, this.parent);
         * }
         */
        overrideMethod(classNode, method -> method.name.equals(obfuscated ? "func_75904_a" : "getInts"), "getInts", "(IIIILnet/minecraft/world/gen/layer/GenLayer;)[I", generator -> {
            generator.visitVarInsn(ILOAD, 1);
            generator.visitVarInsn(ILOAD, 2);
            generator.visitVarInsn(ILOAD, 3);
            generator.visitVarInsn(ILOAD, 4);
            generator.visitVarInsn(ALOAD, 0);
            generator.visitFieldInsn(GETFIELD, "net/minecraft/world/gen/layer/GenLayer", obfuscated ? "field_75909_a" : "parent", "Lnet/minecraft/world/gen/layer/GenLayer;");
        });

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static int[] getInts(int areaX, int areaZ, int areaWidth, int areaHeight, @Nonnull GenLayer parent) {
            return PluginGenLayerShore.Hooks.getInts(areaX, areaZ, areaWidth, areaHeight, parent);
        }
    }
}
