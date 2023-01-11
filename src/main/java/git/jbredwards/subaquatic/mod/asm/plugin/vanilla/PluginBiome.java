package git.jbredwards.subaquatic.mod.asm.plugin.vanilla;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.api.biome.IOceanSurfaceProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.Biome;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Allow modded ocean biomes to have custom surface blocks
 * @author jbred
 *
 */
public final class PluginBiome implements IASMPlugin
{
    @Override
    public int getMethodIndex(@Nonnull MethodNode method, boolean obfuscated) {
        if(method.name.equals(obfuscated ? "func_180628_b" : "generateBiomeTerrain")) return 1;
        else return method.name.equals(obfuscated ? "func_185358_q" : "registerBiomes") ? 2 : 0;
    }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * generateBiomeTerrain: (changes are around line 358)
         * Old code:
         * chunkPrimerIn.setBlockState(i1, j1, l, GRAVEL);
         *
         * New code:
         * //allow modded ocean biomes to have custom surface blocks
         * chunkPrimerIn.setBlockState(i1, j1, l, Hooks.getOceanTopBlock(this, GRAVEL));
         */
        if(index == 1 && checkField(insn, obfuscated ? "field_185368_d" : "GRAVEL")) {
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 0));
            instructions.insert(insn, genMethodNode("getOceanTopBlock", "(Lnet/minecraft/world/biome/Biome;Lnet/minecraft/block/state/IBlockState;)Lnet/minecraft/block/state/IBlockState;"));
            return true;
        }
        /*
         * registerBiomes: (changes are around line 562)
         * Old code:
         * registerBiome(10, "frozen_ocean", new BiomeOcean((new Biome.BiomeProperties("FrozenOcean")).setBaseHeight(-1.0F).setHeightVariation(0.1F).setTemperature(0.0F).setRainfall(0.5F).setSnowEnabled()));
         *
         * New code:
         * //replace old biome class with new one
         * registerBiome(10, "frozen_ocean", new BiomeFrozenOcean((new Biome.BiomeProperties("FrozenOcean")).setBaseHeight(-1.0F).setHeightVariation(0.1F).setTemperature(0.0F).setRainfall(0.5F).setSnowEnabled()));
         */
        else if(index == 2 && insn.getOpcode() == LDC && ((LdcInsnNode)insn).cst.equals("frozen_ocean")) {
            ((TypeInsnNode)insn.getNext()).desc = "git/jbredwards/subaquatic/mod/common/world/biome/BiomeFrozenOcean";
            ((MethodInsnNode)getNext(insn, 16)).owner = "git/jbredwards/subaquatic/mod/common/world/biome/BiomeFrozenOcean";
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @Nonnull
        public static IBlockState getOceanTopBlock(@Nonnull Biome biome, @Nonnull IBlockState fallback) {
            return biome instanceof IOceanSurfaceProvider ? ((IOceanSurfaceProvider)biome).getOceanSurface() : fallback;
        }
    }
}
