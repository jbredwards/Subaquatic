/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.block;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

/**
 * Destroying an active beacon block plays the deactivation sound
 * @author jbred
 *
 */
public final class PluginBlockBeacon implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * New code:
         * @ASMGenerated
         * public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
         * {
         *     Hooks.playDeactivateSound(worldIn, pos);
         * }
         */
        addMethod(classNode, obfuscated ? "func_180663_b" : "breakBlock", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)V",
            "playDeactivateSound", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V", generator -> {
                generator.visitVarInsn(ALOAD, 1);
                generator.visitVarInsn(ALOAD, 2);
            }
        );

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static void playDeactivateSound(@Nonnull World world, @Nonnull BlockPos pos) {
            if(!world.isRemote) {
                final TileEntity tile = world.getTileEntity(pos);
                if(tile instanceof TileEntityBeacon && ((TileEntityBeacon)tile).isComplete)
                    world.playSound(null, pos, SubaquaticSounds.BEACON_DEACTIVATE, SoundCategory.BLOCKS, 1, 1);
            }

            world.removeTileEntity(pos);
        }
    }
}
