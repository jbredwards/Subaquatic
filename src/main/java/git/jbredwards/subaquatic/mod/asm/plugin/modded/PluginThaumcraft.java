package git.jbredwards.subaquatic.mod.asm.plugin.modded;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Use old water texture
 * @author jbred
 *
 */
public final class PluginThaumcraft implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals("renderFluid"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * renderFluid:
         * Old code:
         * TextureAtlasSprite icon = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.WATER.getDefaultState());
         *
         * New code:
         * //use old water texture
         * TextureAtlasSprite icon = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/water_still");
         */
        if(checkMethod(insn, obfuscated ? "func_175602_ab" : "getBlockRendererDispatcher")) {
            final InsnList list = new InsnList();
            list.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/Minecraft", obfuscated ? "func_147117_R" : "getTextureMapBlocks", "()Lnet/minecraft/client/renderer/texture/TextureMap;", false));
            list.add(new LdcInsnNode("minecraft:blocks/water_still"));
            list.add(new MethodInsnNode(INVOKEVIRTUAL, "net/minecraft/client/renderer/texture/TextureMap", obfuscated ? "func_110572_b" : "getAtlasSprite", "(Ljava/lang/String;)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;", false));

            instructions.insertBefore(insn, list);
            removeFrom(instructions, insn, 4);
            return true;
        }

        return false;
    }
}
