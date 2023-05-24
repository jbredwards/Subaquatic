package git.jbredwards.subaquatic.mod.asm.plugin.modded;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Water inside petal apothecaries have their biome colors applied
 * @author jbred
 *
 */
public final class PluginBotania implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_192841_a" : "render"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * render:
         * Old code:
         * GlStateManager.color(1F, 1F, 1F, alpha);
         *
         * New code:
         * //water inside petal apothecaries have their biome colors applied
         * Hooks.applyColor(alpha, altar);
         */
        if(checkMethod(insn, obfuscated ? "func_179124_c" : "color") && insn.getPrevious().getOpcode() == FLOAD) {
            instructions.insert(insn, genMethodNode("applyColor", "(FLnet/minecraft/tileentity/TileEntity;)V"));
            instructions.insert(insn, new VarInsnNode(ALOAD, 1));
            removeFrom(instructions, getPrevious(insn, 2), -2);
            instructions.remove(insn);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @SideOnly(Side.CLIENT)
        public static void applyColor(float alpha, @Nonnull TileEntity tile) {
            final int color = alpha == 1 ? 0xFFFFFF : BiomeColorHelper.getWaterColorAtPos(tile.getWorld(), tile.getPos());
            GlStateManager.color(((color >> 16) & 255) / 255f, ((color >> 8) & 255) / 255f, (color & 255) / 255f, alpha);
        }
    }
}
