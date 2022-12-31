package git.jbredwards.subaquatic.mod.asm.plugin.vanilla;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.api.biome.IOceanBiome;
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
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_180628_b" : "generateBiomeTerrain"); }

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
        if(checkField(insn, obfuscated ? "field_185368_d" : "GRAVEL")) {
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 0));
            instructions.insert(insn, genMethodNode("getOceanTopBlock", "(Lnet/minecraft/world/biome/Biome;Lnet/minecraft/block/state/IBlockState;)Lnet/minecraft/block/state/IBlockState;"));
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @Nonnull
        public static IBlockState getOceanTopBlock(@Nonnull Biome biome, @Nonnull IBlockState fallback) {
            return biome instanceof IOceanBiome ? biome.topBlock : fallback;
        }
    }
}
