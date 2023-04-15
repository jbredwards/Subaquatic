package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.world;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Fix bug where the block under tall trees is not converted to dirt
 * @author jbred
 *
 */
public final class PluginWorldGenBigTree implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_180709_b" : "generate"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * generate: (changes are around line 335):
         * Old code:
         * this.generateLeafNodeList();
         *
         * New code:
         * //fix bug where the block under tall trees is not converted to dirt
         * Hooks.convertSoil(worldIn, position);
         * this.generateLeafNodeList();
         */
        if(checkMethod(insn.getNext(), obfuscated ? "func_76489_a" : "generateLeafNodeList")) {
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 1));
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 3));
            instructions.insertBefore(insn, genMethodNode("convertSoil", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"));
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static void convertSoil(@Nonnull World world, @Nonnull BlockPos pos) {
            final IBlockState state = world.getBlockState(pos.down());
            state.getBlock().onPlantGrow(state, world, pos.down(), pos);
        }
    }
}
