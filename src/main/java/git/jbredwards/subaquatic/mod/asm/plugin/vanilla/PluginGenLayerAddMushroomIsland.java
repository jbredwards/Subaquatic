package git.jbredwards.subaquatic.mod.asm.plugin.vanilla;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Allow for modded shallow biomes
 * @author jbred
 *
 */
public final class PluginGenLayerAddMushroomIsland implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_75904_a" : "getInts"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * getInts: (changes are around line 38)
         * Old code:
         * if (k2 == 0 && k1 == 0 && l1 == 0 && i2 == 0 && j2 == 0 && this.nextInt(100) == 0)
         * {
         *     ...
         * }
         *
         * New code:
         * //allow for modded shallow biomes
         * if (IOceanBiome.isShallowOcean(k2) && IOceanBiome.isShallowOcean(k1) && IOceanBiome.isShallowOcean(l1) && IOceanBiome.isShallowOcean(i2) && IOceanBiome.isShallowOcean(j2) && this.nextInt(100) == 0)
         * {
         *     ...
         * }
         */
        if(insn.getOpcode() == IFNE) {
            if(insn.getPrevious().getOpcode() == INVOKEVIRTUAL) return true;
            ((JumpInsnNode)insn).setOpcode(IFEQ);
            instructions.insertBefore(insn, genMethodNode("git/jbredwards/subaquatic/api/biome/IOceanBiome", "isShallowOcean", "(I)Z"));
        }

        return false;
    }
}
