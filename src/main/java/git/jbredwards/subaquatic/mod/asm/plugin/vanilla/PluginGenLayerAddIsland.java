package git.jbredwards.subaquatic.mod.asm.plugin.vanilla;

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
public final class PluginGenLayerAddIsland implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * New code:
         * //account for modded shallow ocean biomes
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
                    layer.initChunkSeed(x + areaX, z + areaZ);

                    final int surroundingId1 = biomeInts[x + z * (areaWidth + 2)];
                    final int surroundingId2 = biomeInts[x + 2 + z * (areaWidth + 2)];
                    final int surroundingId3 = biomeInts[x + (z + 2) * (areaWidth + 2)];
                    final int surroundingId4 = biomeInts[x + 2 + (z + 2) * (areaWidth + 2)];

                    if(!IOceanBiome.isShallowOcean(biomeId) || IOceanBiome.isShallowOcean(surroundingId1) && IOceanBiome.isShallowOcean(surroundingId2) && IOceanBiome.isShallowOcean(surroundingId3) && IOceanBiome.isShallowOcean(surroundingId4)) {
                        out[x + z * areaWidth] = biomeId;
                        if(!IOceanBiome.isShallowOcean(biomeId) && layer.nextInt(5) == 0) {
                            if(IOceanBiome.isShallowOcean(surroundingId1))
                                out[x + z * areaWidth] = biomeId == 4 ? 4 : surroundingId1;
                            else if(IOceanBiome.isShallowOcean(surroundingId2))
                                out[x + z * areaWidth] = biomeId == 4 ? 4 : surroundingId2;
                            else if(IOceanBiome.isShallowOcean(surroundingId3))
                                out[x + z * areaWidth] = biomeId == 4 ? 4 : surroundingId3;
                            else if(IOceanBiome.isShallowOcean(surroundingId4))
                                out[x + z * areaWidth] = biomeId == 4 ? 4 : surroundingId4;
                        }
                    }

                    else {
                        int biomesChecked = 0;
                        int idToCreate = 1;

                        if(!IOceanBiome.isShallowOcean(surroundingId1) && layer.nextInt(++biomesChecked) == 0) {
                            idToCreate = surroundingId1;
                        }

                        if(!IOceanBiome.isShallowOcean(surroundingId2) && layer.nextInt(++biomesChecked) == 0) {
                            idToCreate = surroundingId2;
                        }

                        if(!IOceanBiome.isShallowOcean(surroundingId3) && layer.nextInt(++biomesChecked) == 0) {
                            idToCreate = surroundingId3;
                        }

                        if(!IOceanBiome.isShallowOcean(surroundingId4) && layer.nextInt(++biomesChecked) == 0) {
                            idToCreate = surroundingId4;
                        }

                        if(layer.nextInt(3) == 0) out[x + z * areaWidth] = idToCreate;
                        else out[x + z * areaWidth] = idToCreate == 4 ? 4 : biomeId;
                    }
                }
            }

            return out;
        }
    }
}
