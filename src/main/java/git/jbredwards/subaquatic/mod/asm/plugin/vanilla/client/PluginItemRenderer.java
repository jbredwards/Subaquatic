package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.client;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticWaterColorConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Apply biome colors to underwater overlay
 * @author jbred
 *
 */
public final class PluginItemRenderer implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_78448_c" : "renderWaterOverlayTexture"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * renderWaterOverlayTexture: (changes are around line 537)
         * Old code:
         * this.mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
         *
         * New code:
         * //change texture
         * this.mc.getTextureManager().bindTexture(Hooks.UNDERWATER_OVERLAY);
         */
        if(insn.getOpcode() == GETSTATIC) {
            instructions.insert(insn, new FieldInsnNode(GETSTATIC, getHookClass(), "UNDERWATER_OVERLAY", "Lnet/minecraft/util/ResourceLocation;"));
            instructions.remove(insn);
        }
        /*
         * renderWaterOverlayTexture: (changes are around line 540)
         * Old code:
         * GlStateManager.color(f, f, f, 0.5F);
         *
         * New code:
         * //use biome color (takes brightness into account)
         * Hooks.applyColor(f, partialTicks);
         */
        else if(checkMethod(insn, obfuscated ? "func_179131_c" : "color")) {
            instructions.insert(insn, genMethodNode("applyColor", "(FF)V"));
            instructions.insert(insn, new VarInsnNode(FLOAD, 1));
            removeFrom(instructions, insn, -3);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @Nonnull
        public static final ResourceLocation UNDERWATER_OVERLAY = new ResourceLocation(Subaquatic.MODID, "textures/misc/underwater.png");
        public static void applyColor(float brightness, float partialTicks) {
            final float[] fogComp = SubaquaticWaterColorConfig.getFogColorAt(Minecraft.getMinecraft().world,
                    new BlockPos(Minecraft.getMinecraft().player.getPositionEyes(partialTicks)));

            brightness *= 0.2f;
            GlStateManager.color(fogComp[0] * 0.8f + brightness, fogComp[1] * 0.8f + brightness, fogComp[2] * 0.8f + brightness, 0.5f);
        }
    }
}
