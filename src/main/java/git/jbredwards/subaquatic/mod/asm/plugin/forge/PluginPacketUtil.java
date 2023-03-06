package git.jbredwards.subaquatic.mod.asm.plugin.forge;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

/**
 * Fix ItemStack capabilities being lost when sending an ItemStack to the client
 * @author jbred
 *
 */
public final class PluginPacketUtil implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        overrideMethod(classNode, method -> method.name.equals("writeItemStackFromClientToServer"),
            "writeItemStackFromClientToServer", "(Lnet/minecraft/network/PacketBuffer;Lnet/minecraft/item/ItemStack;)V", generator -> {
                generator.visitVarInsn(ALOAD, 0);
                generator.visitVarInsn(ALOAD, 1);
            }
        );

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static void writeItemStackFromClientToServer(@Nonnull PacketBuffer buf, @Nonnull ItemStack stack) {
            if(stack.isEmpty()) {
                buf.writeCompoundTag(null);
                return;
            }

            buf.writeCompoundTag(stack.serializeNBT());
            buf.writeCompoundTag(stack.getItem().isDamageable() || stack.getItem().getShareTag() ? stack.getTagCompound() : null);
        }
    }
}
