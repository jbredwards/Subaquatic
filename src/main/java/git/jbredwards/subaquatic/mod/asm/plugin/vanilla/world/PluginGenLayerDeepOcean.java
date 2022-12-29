package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.world;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.api.biome.IOceanBiome;
import git.jbredwards.subaquatic.mod.common.init.ModBiomes;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Generate correct deep ocean biomes
 * @author jbred
 *
 */
public final class PluginGenLayerDeepOcean implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * New code:
         * //Generate correct deep ocean biomes
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
        //helper
        static int frozenDeepOceanBiomeId;

        public static int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight, @Nonnull GenLayer parent) {
            final int[] parentInts = parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
            final int[] biomeNoise = IntCache.getIntCache(areaWidth * areaHeight);
            for(int x = 0; x < areaWidth; x++) {
                for(int z = 0; z < areaHeight; z++) {
                    final int biomeId = parentInts[x + 1 + (z + 1) * (areaWidth + 2)];
                    final @Nullable Biome biome = Biome.getBiomeForId(biomeId);
                    if(IOceanBiome.isShallowOcean(biome)) {
                        int deepOceanChecks = 0;

                        if(IOceanBiome.isShallowOcean(Biome.getBiomeForId(parentInts[x + 1 + (z + 1 - 1) * (areaWidth + 2)]))) ++deepOceanChecks;
                        if(IOceanBiome.isShallowOcean(Biome.getBiomeForId(parentInts[x + 1 + 1 + (z + 1) * (areaWidth + 2)]))) ++deepOceanChecks;
                        if(IOceanBiome.isShallowOcean(Biome.getBiomeForId(parentInts[x + 1 - 1 + (z + 1) * (areaWidth + 2)]))) ++deepOceanChecks;
                        if(IOceanBiome.isShallowOcean(Biome.getBiomeForId(parentInts[x + 1 + (z + 1 + 1) * (areaWidth + 2)]))) ++deepOceanChecks;

                        biomeNoise[x + z * areaWidth] = deepOceanChecks == 4 ? getDeepOceanBiomeId(biome) : biomeId;
                    }

                    else biomeNoise[x + z * areaWidth] = biomeId;
                }
            }

            return biomeNoise;
        }

        //helper
        public static int getDeepOceanBiomeId(@Nullable Biome biome) {
            //special case for vanilla's formally unused frozen ocean biome
            if(biome == Biomes.FROZEN_OCEAN) {
                if(frozenDeepOceanBiomeId != 0) return frozenDeepOceanBiomeId;
                else return frozenDeepOceanBiomeId = Biome.getIdForBiome(ModBiomes.DEEP_FROZEN_OCEAN);
            }

            //use modded deep ocean biomes if possible, otherwise default to vanilla deep ocean biome id
            else return biome instanceof IOceanBiome ? ((IOceanBiome)biome).getDeepOceanBiomeId() : 24;
        }
    }
}
