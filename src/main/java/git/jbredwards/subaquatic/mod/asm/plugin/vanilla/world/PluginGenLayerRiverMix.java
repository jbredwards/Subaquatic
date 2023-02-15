package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.world;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.api.biome.IOceanBiome;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.compat.biomesoplenty.BiomesOPlentyHandler;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.BiomeDictionary;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * Account for all ocean biomes when generating rivers
 * @author jbred
 *
 */
public final class PluginGenLayerRiverMix implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * New code:
         * //account for all ocean biomes when generating rivers
         * public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
         * {
         *     return Hooks.getInts(areaX, areaY, areaWidth, areaHeight, this.biomePatternGeneratorChain, this.riverPatternGeneratorChain);
         * }
         */
        overrideMethod(classNode, method -> method.name.equals(obfuscated ? "func_75904_a" : "getInts"), "getInts", "(IIIILnet/minecraft/world/gen/layer/GenLayer;Lnet/minecraft/world/gen/layer/GenLayer;)[I", generator -> {
            generator.visitVarInsn(ILOAD, 1);
            generator.visitVarInsn(ILOAD, 2);
            generator.visitVarInsn(ILOAD, 3);
            generator.visitVarInsn(ILOAD, 4);
            generator.visitVarInsn(ALOAD, 0);
            generator.visitFieldInsn(GETFIELD, "net/minecraft/world/gen/layer/GenLayerRiverMix", obfuscated ? "field_75910_b" : "biomePatternGeneratorChain", "Lnet/minecraft/world/gen/layer/GenLayer;");
            generator.visitVarInsn(ALOAD, 0);
            generator.visitFieldInsn(GETFIELD, "net/minecraft/world/gen/layer/GenLayerRiverMix", obfuscated ? "field_75911_c" : "riverPatternGeneratorChain", "Lnet/minecraft/world/gen/layer/GenLayer;");
        });

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        static final int RIVER = Biome.getIdForBiome(Biomes.RIVER);
        static final int FROZEN_RIVER = Biome.getIdForBiome(Biomes.FROZEN_RIVER);
        static final int MUSHROOM_RIVER = Biome.getIdForBiome(Biomes.MUSHROOM_ISLAND_SHORE);

        public static int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight, @Nonnull GenLayer biomePatternGeneratorChain, @Nonnull GenLayer riverPatternGeneratorChain) {
            final int[] biomeInts = biomePatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
            final int[] riverInts = riverPatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
            final int[] out = IntCache.getIntCache(areaWidth * areaHeight);
            for(int i = 0; i < areaWidth * areaHeight; ++i) {
                out[i] = biomeInts[i];
                if(riverInts[i] == RIVER && !IOceanBiome.isOcean(biomeInts[i])) {
                    final @Nullable Biome biome = Biome.getBiomeForId(biomeInts[i]);
                    if(biome != null && (!Subaquatic.isBOPInstalled || BiomesOPlentyHandler.doesBiomeSupportRivers(biome))) { //check for BOP special case
                        final Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(biome);
                        if(!types.contains(BiomeDictionary.Type.BEACH)) { //don't generate on beach biomes
                            //check for snowy biomes
                            if(biome.isSnowyBiome()) out[i] = FROZEN_RIVER;
                            //check for mushroom biomes
                            else if(types.contains(BiomeDictionary.Type.MUSHROOM)) out[i] = MUSHROOM_RIVER;
                            //default
                            else out[i] = RIVER;
                        }
                    }
                }
            }

            return out;
        }
    }
}
