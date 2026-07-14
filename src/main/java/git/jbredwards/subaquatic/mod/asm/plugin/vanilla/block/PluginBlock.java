/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.block;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Allow trees to create rooted dirt
 * @author jbred
 *
 */
public final class PluginBlock implements IASMPlugin
{
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

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static void onPlantGrow(@Nonnull Block block, @Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos) {
            if(state.getMaterial() == Material.GROUND || state.getMaterial() == Material.GRASS || state.getBlock().isAir(state, world, pos)) {
                if(SubaquaticConfigHandler.Server.World.General.generateRootedDirt) {
                    world.setBlockState(pos, SubaquaticBlocks.ROOTED_DIRT.getDefaultState(), 2);
                    final IBlockState down = world.getBlockState(pos.down());

                    //convert grass below to rooted dirt too if possible
                    if(down != state && down.getMaterial() == Material.GRASS) down.getBlock().onPlantGrow(down, world, pos.down(), pos);

                    //add roots below if possible
                    else if(down.getBlock().isAir(down, world, pos.down()) || FluidloggedUtils.isFluid(down))
                        world.setBlockState(pos.down(), SubaquaticBlocks.HANGING_ROOTS.getDefaultState(), 2);
                }

                //vanilla behavior
                else if(state != Blocks.DIRT.getDefaultState()) world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 2);
            }

            //hanging roots while in water
            else if(FluidloggedUtils.isFluid(state)) world.setBlockState(pos.down(), SubaquaticBlocks.HANGING_ROOTS.getDefaultState(), 2);
        }
    }
}
