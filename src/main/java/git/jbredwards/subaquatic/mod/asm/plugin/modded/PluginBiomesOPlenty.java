package git.jbredwards.subaquatic.mod.asm.plugin.modded;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Don't change the underwater fog color while this mod is installed
 * @author jbred
 *
 */
public final class PluginBiomesOPlenty implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals("onGetFogColor"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * onGetFogColor:
         * Old code:
         * mixedColor = getFogBlendColorWater(world, player, x, y, z, event.getRenderPartialTicks());
         *
         * New code:
         * //don't change the underwater fog color while this mod is installed
         * mixedColor = new Vec3d((double)event.getRed(), (double)event.getGreen(), (double)event.getBlue());
         */
        if(checkMethod(insn, "getFogBlendColorWater")) {
            final InsnList list = new InsnList();
            list.add(new TypeInsnNode(NEW, "net/minecraft/util/math/Vec3d"));
            list.add(new InsnNode(DUP));
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraftforge/client/event/EntityViewRenderEvent$FogColors", "getRed", "()F", false));
            list.add(new InsnNode(F2D));
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraftforge/client/event/EntityViewRenderEvent$FogColors", "getGreen", "()F", false));
            list.add(new InsnNode(F2D));
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraftforge/client/event/EntityViewRenderEvent$FogColors", "getBlue", "()F", false));
            list.add(new InsnNode(F2D));
            list.add(new MethodInsnNode(INVOKESPECIAL, "net/minecraft/util/math/Vec3d", "<init>", "(DDD)V", false));

            instructions.insert(insn, list);
            removeFrom(instructions, insn, -7);
            return true;
        }

        return false;
    }
}
