package git.jbredwards.subaquatic.mod.asm.plugin.vanilla;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.api.biome.IOceanBiome;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.integration.biomesoplenty.BiomesOPlentyHandler;
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
        //static biome id helpers
        static final int BEACH = Biome.getIdForBiome(Biomes.BEACH);
        static final int COLD_BEACH = Biome.getIdForBiome(Biomes.COLD_BEACH);
        static final int DESERT = Biome.getIdForBiome(Biomes.DESERT);
        static final int EXTREME_HILLS = Biome.getIdForBiome(Biomes.EXTREME_HILLS);
        static final int EXTREME_HILLS_EDGE = Biome.getIdForBiome(Biomes.EXTREME_HILLS_EDGE);
        static final int EXTREME_HILLS_WITH_TREES = Biome.getIdForBiome(Biomes.EXTREME_HILLS_WITH_TREES);
        static final int JUNGLE_EDGE = Biome.getIdForBiome(Biomes.JUNGLE_EDGE);
        static final int MUSHROOM_ISLAND_SHORE = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND_SHORE);
        static final int STONE_BEACH = Biome.getIdForBiome(Biomes.STONE_BEACH);

        public static int[] getInts(int areaX, int areaZ, int areaWidth, int areaHeight, @Nonnull GenLayer parent) {
            final int[] biomeInts = parent.getInts(areaX - 1, areaZ - 1, areaWidth + 2, areaHeight + 2);
            final int[] out = IntCache.getIntCache(areaWidth * areaHeight);
            for(int x = 0; x < areaWidth; x++) {
                for(int z = 0; z < areaHeight; z++) {
                    final int biomeId = biomeInts[x + 1 + (z + 1) * (areaWidth + 2)];
                    final int index = x + z * areaWidth;
                    out[index] = biomeId;

                    if(!IOceanBiome.isOcean(biomeId)) {
                        final @Nullable Biome biome = Biome.getBiomeForId(biomeId);
                        if(biome != null) {
                            final Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(biome);
                            //create mushroom shores
                            if(types.contains(BiomeDictionary.Type.MUSHROOM)) {
                                if(hasAnyAround(biomeInts, x, z, areaWidth, IOceanBiome::isShallowOcean))
                                    out[index] = MUSHROOM_ISLAND_SHORE;
                            }
                            //create jungle mix & shores
                            else if(types.contains(BiomeDictionary.Type.JUNGLE)) {
                                if(hasMissingAround(biomeInts, x, z, areaWidth, Hooks::isJungleCompatible))
                                    out[index] = JUNGLE_EDGE;

                                else if(hasAnyAround(biomeInts, x, z, areaWidth, IOceanBiome::isOcean))
                                    out[index] = BEACH;
                            }
                            //create extreme hills shores
                            else if(biomeId == EXTREME_HILLS || biomeId == EXTREME_HILLS_EDGE || biomeId == EXTREME_HILLS_WITH_TREES) {
                                if(hasAnyAround(biomeInts, x, z, areaWidth, IOceanBiome::isOcean))
                                    out[index] = STONE_BEACH;
                            }
                            //create cold shores
                            else if(biome.isSnowyBiome()) {
                                if(hasAnyAround(biomeInts, x, z, areaWidth, IOceanBiome::isOcean)) {
                                    out[index] = COLD_BEACH;
                                    if(Subaquatic.isBOPInstalled && BiomesOPlentyHandler.isExtendedBiome(biome)) {
                                        final Biome beachBiome = BiomesOPlentyHandler.getBOPBeachBiome(biome);
                                        if(beachBiome == null) out[index] = biomeId;
                                    }
                                }
                            }
                            //create mesa mix
                            else if(types.contains(BiomeDictionary.Type.MESA)) {
                                if(!hasAnyAround(biomeInts, x, z, areaWidth, IOceanBiome::isOcean) && hasMissingAround(biomeInts, x, z, areaWidth, Hooks::isMesaCompatible))
                                    out[index] = DESERT;
                            }
                            //create generic shores
                            else if(!types.contains(BiomeDictionary.Type.RIVER) && !types.contains(BiomeDictionary.Type.SWAMP)) {
                                if(hasAnyAround(biomeInts, x, z, areaWidth, IOceanBiome::isOcean)) {
                                    out[index] = BEACH;
                                    if(Subaquatic.isBOPInstalled && BiomesOPlentyHandler.isExtendedBiome(biome)) {
                                        final Biome beachBiome = BiomesOPlentyHandler.getBOPBeachBiome(biome);
                                        out[index] = beachBiome == null ? biomeId : Biome.getIdForBiome(beachBiome);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return out;
        }

        //helper
        static boolean hasAnyAround(int[] biomeInts, int x, int z, int areaWidth, @Nonnull IntPredicate predicate) {
            return predicate.test(biomeInts[x + 1 + (z + 1 - 1) * (areaWidth + 2)])
                    || predicate.test(biomeInts[x + 1 + 1 + (z + 1) * (areaWidth + 2)])
                    || predicate.test(biomeInts[x + 1 - 1 + (z + 1) * (areaWidth + 2)])
                    || predicate.test(biomeInts[x + 1 + (z + 1 + 1) * (areaWidth + 2)]);
        }

        //helper
        static boolean hasMissingAround(int[] biomeInts, int x, int z, int areaWidth, @Nonnull IntPredicate predicate) {
            return !predicate.test(biomeInts[x + 1 + (z + 1 - 1) * (areaWidth + 2)])
                    || !predicate.test(biomeInts[x + 1 + 1 + (z + 1) * (areaWidth + 2)])
                    || !predicate.test(biomeInts[x + 1 - 1 + (z + 1) * (areaWidth + 2)])
                    || !predicate.test(biomeInts[x + 1 + (z + 1 + 1) * (areaWidth + 2)]);
        }

        //helper
        static boolean isJungleCompatible(int biomeId) {
            if(IOceanBiome.isOcean(biomeId)) return true;

            final Biome biome = Biome.getBiomeForId(biomeId);
            if(biome == null) return false;

            final Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(biome);
            return types.contains(BiomeDictionary.Type.JUNGLE) || types.contains(BiomeDictionary.Type.FOREST);
        }

        //helper
        static boolean isMesaCompatible(int biomeId) {
            final Biome biome = Biome.getBiomeForId(biomeId);
            return biome != null && BiomeDictionary.getTypes(biome).contains(BiomeDictionary.Type.MESA);
        }
    }
}
