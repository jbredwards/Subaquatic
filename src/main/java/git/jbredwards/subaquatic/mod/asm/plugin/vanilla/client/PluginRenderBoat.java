package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.client;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.common.capability.IBubbleColumn;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Render bubble column boat rocking
 * @author jbred
 *
 */
public final class PluginRenderBoat implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_188311_a" : "setupRotation"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * setupRotation: (changes are around line 70)
         * Old code:
         * GlStateManager.scale(-1.0F, -1.0F, 1.0F);
         *
         * New code:
         * //render bubble column boat rocking
         * Hooks.setupRockingRotation(entityIn, partialTicks);
         * GlStateManager.scale(-1.0F, -1.0F, 1.0F);
         */
        if(checkMethod(insn, obfuscated ? "func_179152_a" : "scale")) {
            final InsnList list = new InsnList();
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new VarInsnNode(FLOAD, 3));
            list.add(genMethodNode("setupRockingRotation", "(Lnet/minecraft/entity/item/EntityBoat;F)V"));

            instructions.insertBefore(getPrevious(insn, 3), list);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @SideOnly(Side.CLIENT)
        public static void setupRockingRotation(@Nonnull EntityBoat boat, float partialTicks) {
            final @Nullable IBubbleColumn cap = IBubbleColumn.get(boat);
            if(cap instanceof IBubbleColumn.Boat) {
                final float rockingAngle = ((IBubbleColumn.Boat)cap).getRenderRockingAngle(partialTicks);
                if(!MathHelper.epsilonEquals(rockingAngle, 0)) GlStateManager.rotate(rockingAngle, 1, 0, 1);
            }
        }
    }
}
