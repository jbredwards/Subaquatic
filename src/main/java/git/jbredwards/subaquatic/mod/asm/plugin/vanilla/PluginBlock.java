package git.jbredwards.subaquatic.mod.asm.plugin.vanilla;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticWaterColorConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Remove hardcoded values for biome fog color
 * @author jbred
 *
 */
public final class PluginBlock implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals("getFogColor"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * getFogColor: (changes are around line 2587)
         * Old code:
         * return new Vec3d(0.02F + f12, 0.02F + f12, 0.2F + f12);
         *
         * New code:
         * //remove hardcoded values for biome fog color
         * return Hooks.betterWaterFogColor(world, pos, f12);
         */
        if(insn.getOpcode() == NEW) {
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 1));
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 2));
            instructions.insertBefore(insn, new VarInsnNode(FLOAD, 7));
            instructions.insertBefore(insn, genMethodNode("betterWaterFogColor", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;F)Lnet/minecraft/util/math/Vec3d;"));
            removeFrom(instructions, insn, 14);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @Nonnull
        public static Vec3d betterWaterFogColor(@Nonnull World world, @Nonnull BlockPos origin, float modifier) {
            final float[] components = SubaquaticWaterColorConfig.getFogColorAt(world, origin);
            return new Vec3d(Math.min(1, components[0] + modifier), Math.min(1, components[1] + modifier), Math.min(1, components[2] + modifier));
        }
    }
}
