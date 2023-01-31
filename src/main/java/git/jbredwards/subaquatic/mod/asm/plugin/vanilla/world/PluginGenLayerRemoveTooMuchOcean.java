package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.world;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.api.biome.IOceanBiome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

/**
 * Account for modded shallow ocean biomes
 * @author jbred
 *
 */
public final class PluginGenLayerRemoveTooMuchOcean implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * New code:
         * //Account for modded shallow ocean biomes
         * public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
         * {
         *     return Hooks.getInts(this, areaX, areaY, areaWidth, areaHeight, this.parent);
         * }
         */
        overrideMethod(classNode, method -> method.name.equals(obfuscated ? "func_75904_a" : "getInts"), "getInts", "(Lnet/minecraft/world/gen/layer/GenLayer;IIIILnet/minecraft/world/gen/layer/GenLayer;)[I", generator -> {
            generator.visitVarInsn(ALOAD, 0);
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
        public static int[] getInts(@Nonnull GenLayer layer, int areaX, int areaZ, int areaWidth, int areaHeight, @Nonnull GenLayer parent) {
            final int[] biomeInts = parent.getInts(areaX - 1, areaZ - 1, areaWidth + 2, areaHeight + 2);
            final int[] out = IntCache.getIntCache(areaWidth * areaHeight);
            for(int x = 0; x < areaWidth; x++) {
                for(int z = 0; z < areaHeight; z++) {
                    final int biomeId = biomeInts[x + 1 + (z + 1) * (areaWidth + 2)];
                    out[x + z * areaWidth] = biomeId;

                    layer.initChunkSeed(x + areaX, z + areaZ);
                    if(IOceanBiome.isShallowOcean(biomeId)
                            && IOceanBiome.isShallowOcean(biomeInts[x + 1 + (z + 1 - 1) * (areaWidth + 2)])
                            && IOceanBiome.isShallowOcean(biomeInts[x + 1 + 1 + (z + 1) * (areaWidth + 2)])
                            && IOceanBiome.isShallowOcean(biomeInts[x + 1 - 1 + (z + 1) * (areaWidth + 2)])
                            && IOceanBiome.isShallowOcean(biomeInts[x + 1 + (z + 1 + 1) * (areaWidth + 2)])
                            && layer.nextInt(2) == 0) out[x + z * areaWidth] = 1;
                }
            }

            return out;
        }
    }
}
