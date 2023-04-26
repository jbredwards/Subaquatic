package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.world;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Generate non-carved pumpkins instead of carved ones
 * @author jbred
 *
 */
public final class PluginWorldGenPumpkin implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_180709_b" : "generate"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * generate: (changes are around line 18)
         * Old code:
         * worldIn.setBlockState(blockpos, Blocks.PUMPKIN.getDefaultState().withProperty(BlockPumpkin.FACING, EnumFacing.Plane.HORIZONTAL.random(rand)), 2);
         *
         * New code:
         * //generate non-carved pumpkins instead of carved ones
         * worldIn.setBlockState(blockpos, Hooks.getPumpkinForGen(rand), 2);
         */
        if(checkMethod(insn.getNext(), obfuscated ? "func_176223_P" : "getDefaultState")) {
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 2));
            instructions.insertBefore(insn, genMethodNode("getPumpkinForGen", "(Ljava/util/Random;)Lnet/minecraft/block/state/IBlockState;"));
            removeFrom(instructions, insn, 6);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @Nonnull
        public static IBlockState getPumpkinForGen(@Nonnull Random rand) {
            final float chance = SubaquaticConfigHandler.Server.World.generateFacelessPumpkinsChance;
            if(chance == 1) return SubaquaticBlocks.PUMPKIN.getDefaultState();
            if(chance == 0) return Blocks.PUMPKIN.getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.Plane.HORIZONTAL.random(rand));

            return rand.nextFloat() < chance
                        ? SubaquaticBlocks.PUMPKIN.getDefaultState()
                        : Blocks.PUMPKIN.getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.Plane.HORIZONTAL.random(rand));
        }
    }
}
