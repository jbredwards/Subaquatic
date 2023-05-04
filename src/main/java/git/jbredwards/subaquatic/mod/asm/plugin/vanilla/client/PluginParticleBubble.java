package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.client;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.client.particle.ParticleBubblePop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleBubble;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

/**
 * Add the unused bubble pop particle from 1.13+
 * @author jbred
 *
 */
public final class PluginParticleBubble implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * New code:
         * add the unused bubble pop particle from 1.13+
         * @ASMGenerated
         * public void setExpired()
         * {
         *     super.setExpired();
         *     Hooks.setExpired(this);
         * }
         */
        addMethod(classNode, obfuscated ? "func_187112_i" : "setExpired", "()V",
            "setExpired", "(Lnet/minecraft/client/particle/ParticleBubble;)V", generator -> {
                generator.visitVarInsn(ALOAD, 0);
                generator.visitMethodInsn(INVOKESPECIAL, "net/minecraft/client/particle/Particle", obfuscated ? "func_187112_i" : "setExpired", "()V", false);
                generator.visitVarInsn(ALOAD, 0);
            }
        );

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static void setExpired(@Nonnull ParticleBubble particle) {
            Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleBubblePop(particle));
        }
    }
}
