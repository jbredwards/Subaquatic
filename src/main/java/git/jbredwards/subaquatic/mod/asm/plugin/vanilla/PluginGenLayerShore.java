package git.jbredwards.subaquatic.mod.asm.plugin.vanilla;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.api.biome.IOceanBiome;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.BiomeDictionary;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.IntPredicate;

/**
 * Account for all ocean biomes when generating shores
 * @author jbred
 *
 */
public final class PluginGenLayerShore implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * New code:
         * //Generate correct deep ocean biomes
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
        //helpers
        static final int MUSHROOM_ISLAND = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND);
        static final int MUSHROOM_ISLAND_SHORE = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND_SHORE);

        public static int[] getInts(@Nonnull GenLayer layer, int areaX, int areaZ, int areaWidth, int areaHeight, @Nonnull GenLayer parent) {
            final int[] biomeInts = parent.getInts(areaX - 1, areaZ - 1, areaWidth + 2, areaHeight + 2);
            final int[] out = IntCache.getIntCache(areaWidth * areaHeight);
            for(int x = 0; x < areaWidth; x++) {
                for(int z = 0; z < areaHeight; z++) {
                    final int biomeId = biomeInts[x + 1 + (z + 1) * areaHeight];
                    out[x + z * areaHeight] = biomeId;

                    final @Nullable Biome biome = Biome.getBiomeForId(biomeId);
                    if(biome != null) {
                        final Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(biome);
                        //create mushroom shores
                        if(types.contains(BiomeDictionary.Type.MUSHROOM)) {
                            if(isSurroundedBy(biomeInts, x, z, areaHeight, IOceanBiome::isShallowOcean))
                                out[x + z * areaHeight] = MUSHROOM_ISLAND_SHORE;
                        }
                        //create jungle shores
                        else if(types.contains(BiomeDictionary.Type.JUNGLE)) {
                            
                        }
                    }
                }
            }

            return out;
        }

        //helper
        static boolean isSurroundedBy(int[] biomeInts, int x, int z, int areaHeight, @Nonnull IntPredicate predicate) {
            return predicate.test(biomeInts[x + 1 + (z + 1 - 1) * (areaHeight + 2)])
                    || predicate.test(biomeInts[x + 1 + 1 + (z + 1) * (areaHeight + 2)])
                    || predicate.test(biomeInts[x + 1 - 1 + (z + 1) * (areaHeight + 2)])
                    || predicate.test(biomeInts[x + 1 + (z + 1 + 1) * (areaHeight + 2)]);
        }
    }
}
