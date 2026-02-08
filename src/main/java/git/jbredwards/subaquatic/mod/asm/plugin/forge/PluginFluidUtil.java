/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.forge;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.api.entity.bucketable.BucketableEntityRegistry;
import git.jbredwards.subaquatic.mod.asm.plugin.vanilla.item.PluginItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidActionResult;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Place fish contained within bucket
 * @author jbred
 *
 */
public final class PluginFluidUtil implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) {
        return checkMethod(method, "tryPlaceFluid", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;Lnet/minecraftforge/fluids/FluidStack;)Lnet/minecraftforge/fluids/FluidActionResult;");
    }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * tryPlaceFluid: (changes are around line 597)
         * Old code:
         * return new FluidActionResult(containerFluidHandler.getContainer());
         *
         * New code:
         * //place fish contained within bucket
         * return Hooks.placeFish(new FluidActionResult(containerFluidHandler.getContainer()), world, pos, container);
         */
        if(insn.getOpcode() == ARETURN) {
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 1));
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 2));
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 3));
            instructions.insertBefore(insn, genMethodNode("placeFish", "(Lnet/minecraftforge/fluids/FluidActionResult;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)Lnet/minecraftforge/fluids/FluidActionResult;"));
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @Nonnull
        public static FluidActionResult placeFish(@Nonnull final FluidActionResult result, @Nonnull final World world, @Nonnull final BlockPos pos, @Nonnull final ItemStack bucket) {
            if(result.isSuccess()) {
                @Nullable final EntityPlayer player = PluginItemStack.Hooks.USING_PLAYER.get();
                if(BucketableEntityRegistry.tryPlaceBucketedEntity(player, world, pos, bucket) && !world.isRemote && (player == null || !player.isCreative())) {
                    // Ensure fish data is wiped from fluid handlers that modify (but keep) the original container object.
                    @Nonnull final ItemStack resultStack = result.getResult();
                    resultStack.removeSubCompound(BucketableEntityRegistry.NBT_ROOT);
                    if(resultStack.hasTagCompound() && resultStack.getTagCompound().isEmpty()) resultStack.setTagCompound(null);
                }
            }

            return result;
        }
    }
}
