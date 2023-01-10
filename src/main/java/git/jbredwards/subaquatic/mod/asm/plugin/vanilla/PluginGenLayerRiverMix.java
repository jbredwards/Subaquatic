package git.jbredwards.subaquatic.mod.asm.plugin.vanilla;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.api.biome.IOceanBiome;
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
         *     return Hooks.getInts(this, areaX, areaY, areaWidth, areaHeight, this.biomePatternGeneratorChain, this.riverPatternGeneratorChain);
         * }
         */
        overrideMethod(classNode, method -> method.name.equals(obfuscated ? "func_75904_a" : "getInts"), "getInts", "(Lnet/minecraft/world/gen/layer/GenLayer;IIIILnet/minecraft/world/gen/layer/GenLayer;Lnet/minecraft/world/gen/layer/GenLayer;)[I", generator -> {
            generator.visitVarInsn(ALOAD, 0);
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

        public static int[] getInts(@Nonnull GenLayer layer, int areaX, int areaY, int areaWidth, int areaHeight, @Nonnull GenLayer biomePatternGeneratorChain, @Nonnull GenLayer riverPatternGeneratorChain) {
            final int[] biomeInts = biomePatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
            final int[] riverInts = riverPatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
            final int[] out = IntCache.getIntCache(areaWidth * areaHeight);
            for(int i = 0; i < areaWidth * areaHeight; ++i) {
                //skip if there shouldn't be a river
                if(riverInts[i] != RIVER) {
                    out[i] = biomeInts[i];
                    continue;
                }

                //skip if this is an ocean
                if(IOceanBiome.isOcean(biomeInts[i])) {
                    out[i] = biomeInts[i];
                    continue;
                }

                //check for BOP special case
                if(layer instanceof IBOPGenerator && !((IBOPGenerator)layer).biomeSupportsRivers(biomeInts[i])) {
                    out[i] = biomeInts[i];
                    continue;
                }

                //skip if the biome is null
                final @Nullable Biome biome = Biome.getBiomeForId(biomeInts[i]);
                if(biome == null) {
                    out[i] = biomeInts[i];
                    continue;
                }

                //check for biome type special cases
                final Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(biome);
                if(types.contains(BiomeDictionary.Type.SNOWY)) out[i] = FROZEN_RIVER;
                else if(types.contains(BiomeDictionary.Type.MUSHROOM)) out[i] = MUSHROOM_RIVER;

                //default
                else out[i] = riverInts[i] & 255;
            }

            return out;
        }
    }

    //needed for Biomes O' Plenty support
    public interface IBOPGenerator
    {
        boolean biomeSupportsRivers(int biomeId);
    }
}
