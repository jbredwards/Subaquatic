/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.network;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * Fix ItemStack capabilities being lost when sending an ItemStack to the client
 * @author jbred
 *
 */
public final class PluginPacketBuffer implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        overrideMethod(classNode, method -> method.name.equals(obfuscated ? "func_150791_c" : "readItemStack"),
            "readItemStack", "(Lnet/minecraft/network/PacketBuffer;)Lnet/minecraft/item/ItemStack;", generator -> generator.visitVarInsn(ALOAD, 0));

        overrideMethod(classNode, method -> method.name.equals(obfuscated ? "func_150788_a" : "writeItemStack"),
            "writeItemStack", "(Lnet/minecraft/network/PacketBuffer;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/network/PacketBuffer;", generator -> {
                generator.visitVarInsn(ALOAD, 0);
                generator.visitVarInsn(ALOAD, 1);
            }
        );

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @Nonnull
        public static ItemStack readItemStack(@Nonnull PacketBuffer buf) throws IOException {
            final NBTTagCompound nbt = buf.readCompoundTag();
            if(nbt == null) return ItemStack.EMPTY;

            final ItemStack readStack = new ItemStack(nbt);
            readStack.getItem().readNBTShareTag(readStack, buf.readCompoundTag());
            return readStack;
        }

        @Nonnull
        public static PacketBuffer writeItemStack(@Nonnull PacketBuffer buf, @Nonnull ItemStack stack) {
            if(stack.isEmpty()) return buf.writeCompoundTag(null);

            buf.writeCompoundTag(stack.serializeNBT());
            buf.writeCompoundTag(stack.getItem().isDamageable() || stack.getItem().getShareTag() ? stack.getItem().getNBTShareTag(stack) : null);

            return buf;
        }
    }
}
