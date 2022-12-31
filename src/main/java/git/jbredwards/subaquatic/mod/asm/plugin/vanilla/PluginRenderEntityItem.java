package git.jbredwards.subaquatic.mod.asm.plugin.vanilla;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Don't render item bobbing while in water
 * @author jbred
 *
 */
public final class PluginRenderEntityItem implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_177077_a" : "transformModelCount"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         *transformModelCount: (changes are around line 47)
         * Old code:
         * float f1 = shouldBob() ? MathHelper.sin(((float)itemIn.getAge() + p_177077_8_) / 10.0F + itemIn.hoverStart) * 0.1F + 0.1F : 0;
         *
         * New code:
         * //don't render item bobbing while in water
         * float f1 = Hooks.shouldBob(this, itemIn) ? MathHelper.sin(((float)itemIn.getAge() + p_177077_8_) / 10.0F + itemIn.hoverStart) * 0.1F + 0.1F : 0;
         */
        if(checkMethod(insn, "shouldBob")) {
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 1));
            instructions.insertBefore(insn, genMethodNode("shouldBob", "(Lnet/minecraft/client/renderer/entity/RenderEntityItem;Lnet/minecraft/entity/item/EntityItem;)Z"));
            instructions.remove(insn);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static boolean shouldBob(@Nonnull RenderEntityItem renderer, @Nonnull EntityItem entity) {
            return renderer.shouldBob() && FluidloggedUtils.getFluidState(entity.world, new BlockPos(entity.getPositionEyes(1))).isEmpty();
        }
    }
}
