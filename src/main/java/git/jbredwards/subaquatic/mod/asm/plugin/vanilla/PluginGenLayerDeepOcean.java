package git.jbredwards.subaquatic.mod.asm.plugin.vanilla;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.api.biome.IOceanBiome;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

/**
 * Take modded ocean biomes into account when determining whether to generate a deep ocean
 * @author jbred
 *
 */
public final class PluginGenLayerDeepOcean implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * New code:
         * //Take modded ocean biomes into account when determining whether to generate a deep ocean
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
        static final int DEEP_OCEAN = Biome.getIdForBiome(Biomes.DEEP_OCEAN); //helper
        public static int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight, @Nonnull GenLayer parent) {
            final int[] biomeInts = parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
            final int[] out = IntCache.getIntCache(areaWidth * areaHeight);
            for(int x = 0; x < areaWidth; x++) {
                for(int z = 0; z < areaHeight; z++) {
                    final int biomeId = biomeInts[x + 1 + (z + 1) * (areaWidth + 2)];
                    if(IOceanBiome.isShallowOcean(biomeId)) {
                        int oceanChecks = 0;

                        if(IOceanBiome.isShallowOcean(biomeInts[x + 1 + (z + 1 - 1) * (areaWidth + 2)])) ++oceanChecks;
                        if(IOceanBiome.isShallowOcean(biomeInts[x + 1 + 1 + (z + 1) * (areaWidth + 2)])) ++oceanChecks;
                        if(IOceanBiome.isShallowOcean(biomeInts[x + 1 - 1 + (z + 1) * (areaWidth + 2)])) ++oceanChecks;
                        if(IOceanBiome.isShallowOcean(biomeInts[x + 1 + (z + 1 + 1) * (areaWidth + 2)])) ++oceanChecks;

                        out[x + z * areaWidth] = oceanChecks == 4 ? DEEP_OCEAN : biomeId;
                    }

                    else out[x + z * areaWidth] = biomeId;
                }
            }

            return out;
        }
    }
}