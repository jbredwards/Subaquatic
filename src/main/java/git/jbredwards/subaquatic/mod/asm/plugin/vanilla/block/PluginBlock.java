package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.block;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticWaterColorConfig;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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

    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * New code:
         * //create rooted dirt instead of normal dirt
         * public void onPlantGrow(IBlockState state, World world, BlockPos pos, BlockPos source)
         * {
         *     Hooks.onPlantGrow(this, world, pos);
         * }
         */
        overrideMethod(classNode, method -> method.name.equals("onPlantGrow"),
            "onPlantGrow", "(Lnet/minecraft/block/Block;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V", generator -> {
                generator.visitVarInsn(ALOAD, 0);
                generator.visitVarInsn(ALOAD, 1);
                generator.visitVarInsn(ALOAD, 2);
                generator.visitVarInsn(ALOAD, 3);
            }
        );

        return true;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @Nonnull
        public static Vec3d betterWaterFogColor(@Nonnull World world, @Nonnull BlockPos origin, float modifier) {
            final float[] components = SubaquaticWaterColorConfig.getFogColorAt(world, origin);
            return new Vec3d(Math.min(1, components[0] + modifier), Math.min(1, components[1] + modifier), Math.min(1, components[2] + modifier));
        }

        public static void onPlantGrow(@Nonnull Block block, @Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos) {
            if(state.getMaterial() == Material.GROUND || state.getMaterial() == Material.GRASS || state.getBlock().isAir(state, world, pos)) {
                if(SubaquaticConfigHandler.Server.World.General.generateRootedDirt) {
                    world.setBlockState(pos, SubaquaticBlocks.ROOTED_DIRT.getDefaultState(), 2);
                    final IBlockState down = world.getBlockState(pos.down());

                    //convert grass below to rooted dirt too if possible
                    if(down != state && down.getMaterial() == Material.GRASS) down.getBlock().onPlantGrow(down, world, pos.down(), pos);

                    //add roots below if possible
                    else if(down.getMaterial() == Material.WATER || down.getBlock().isAir(down, world, pos.down()))
                        world.setBlockState(pos.down(), SubaquaticBlocks.HANGING_ROOTS.getDefaultState(), 2);
                }

                //vanilla behavior
                else if(state != Blocks.DIRT.getDefaultState()) world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 2);
            }
        }
    }
}
