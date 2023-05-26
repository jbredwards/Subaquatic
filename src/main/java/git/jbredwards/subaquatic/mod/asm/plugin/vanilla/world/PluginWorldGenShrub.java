package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.world;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Fix bug where the block under the log of a shrub is not converted to dirt
 * @author jbred
 *
 */
public final class PluginWorldGenShrub implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_180709_b" : "generate"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * generate: (changes are around line 34)
         * Old code:
         * position = position.up();
         *
         * New code:
         * //fix bug where the block under the log of a shrub is not converted to dirt
         * position = position.up();
         * Hooks.convertSoil(worldIn, position, state);
         */
        if(checkMethod(insn.getPrevious(), obfuscated ? "func_177984_a" : "up")) {
            final InsnList list = new InsnList();
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new VarInsnNode(ALOAD, 3));
            list.add(new VarInsnNode(ALOAD, 4));
            list.add(genMethodNode("convertSoil", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)V"));

            instructions.insert(insn, list);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static void convertSoil(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
            state.getBlock().onPlantGrow(state, world, pos.down(), pos);
        }
    }
}
