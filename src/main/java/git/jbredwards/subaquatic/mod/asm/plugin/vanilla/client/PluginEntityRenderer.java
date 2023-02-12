package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.client;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.BiomeColorHelper;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Colors rain according to biome color
 * @author jbred
 *
 */
public final class PluginEntityRenderer implements IASMPlugin
{
    boolean isOptifine = false;
    int count = 0; //count exists to prevent also coloring the snow

    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_78474_d" : "renderRainSnow"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        if(!isOptifine && checkField(insn, "ForgeWorldProvider_getWeatherRenderer", "Lnet/optifine/reflect/ReflectorMethod;")) isOptifine = true;
        /*
         * renderRainSnow: (changes are around lines 1708 - 1711)
         * Old code:
         * bufferbuilder.pos((double)l1 - d3 + 0.5D, (double)l2, (double)k1 - d4 + 0.5D).tex(0.0D, (double)k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
         * bufferbuilder.pos((double)l1 + d3 + 0.5D, (double)l2, (double)k1 + d4 + 0.5D).tex(1.0D, (double)k2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
         * bufferbuilder.pos((double)l1 + d3 + 0.5D, (double)k2, (double)k1 + d4 + 0.5D).tex(1.0D, (double)l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
         * bufferbuilder.pos((double)l1 - d3 + 0.5D, (double)k2, (double)k1 - d4 + 0.5D).tex(0.0D, (double)l2 * 0.25D + d5).color(1.0F, 1.0F, 1.0F, f4).lightmap(k3, l3).endVertex();
         *
         * New code:
         * //colors rain according to biome color
         * int color = Hooks.getColor(blockpos$mutableblockpos);
         * float red = Hooks.getRed(color);
         * float green = Hooks.getGreen(color);
         * float blue = Hooks.getBlue(color);
         * bufferbuilder.pos((double)l1 - d3 + 0.5D, (double)l2, (double)k1 - d4 + 0.5D).tex(0.0D, (double)k2 * 0.25D + d5).color(red, green, blue, f4).lightmap(k3, l3).endVertex();
         * bufferbuilder.pos((double)l1 + d3 + 0.5D, (double)l2, (double)k1 + d4 + 0.5D).tex(1.0D, (double)k2 * 0.25D + d5).color(red, green, blue, f4).lightmap(k3, l3).endVertex();
         * bufferbuilder.pos((double)l1 + d3 + 0.5D, (double)k2, (double)k1 + d4 + 0.5D).tex(1.0D, (double)l2 * 0.25D + d5).color(red, green, blue, f4).lightmap(k3, l3).endVertex();
         * bufferbuilder.pos((double)l1 - d3 + 0.5D, (double)k2, (double)k1 - d4 + 0.5D).tex(0.0D, (double)l2 * 0.25D + d5).color(red, green, blue, f4).lightmap(k3, l3).endVertex();
         */
        if(checkMethod(insn, obfuscated ? "func_181666_a" : "color", "(FFFF)Lnet/minecraft/client/renderer/BufferBuilder;")) {
            //creates the colors used later
            if(count == 0) {
                final InsnList list = new InsnList();
                list.add(new LabelNode());
                //blockpos variable
                list.add(new VarInsnNode(ALOAD, isOptifine ? 20 : 21));
                //stores the color
                list.add(genMethodNode("getColor", "(Lnet/minecraft/util/math/BlockPos;)I"));
                list.add(new VarInsnNode(ISTORE, 50));
                //stores the red
                list.add(new VarInsnNode(ILOAD, 50));
                list.add(genMethodNode("getRed", "(I)F"));
                list.add(new VarInsnNode(FSTORE, 51));
                //stores the green
                list.add(new VarInsnNode(ILOAD, 50));
                list.add(genMethodNode("getGreen", "(I)F"));
                list.add(new VarInsnNode(FSTORE, 102));
                //stores the blue
                list.add(new VarInsnNode(ILOAD, 50));
                list.add(genMethodNode("getBlue", "(I)F"));
                list.add(new VarInsnNode(FSTORE, 53));
                //adds the new variables to the insn list
                instructions.insert(getPrevious(insn, 30), list);
            }

            //replaces the red value
            final AbstractInsnNode redNode = getPrevious(insn, 4);
            instructions.insert(redNode, new VarInsnNode(FLOAD, 51));
            instructions.remove(redNode);
            //replaces the green value
            final AbstractInsnNode greenNode = getPrevious(insn, 3);
            instructions.insert(greenNode, new VarInsnNode(FLOAD, 52));
            instructions.remove(greenNode);
            //replaces the blue value
            final AbstractInsnNode blueNode = getPrevious(insn, 2);
            instructions.insert(blueNode, new VarInsnNode(FLOAD, 53));
            instructions.remove(blueNode);

            //returns true once applied to all four
            return ++count == 4;
        }

        return false;
    }

    @Override
    public boolean addLocalVariables(@Nonnull MethodNode method, @Nonnull LabelNode start, @Nonnull LabelNode end, int index) {
        method.localVariables.add(new LocalVariableNode("color", "I", null, start, end, 50));
        method.localVariables.add(new LocalVariableNode("red", "F", null, start, end, 51));
        method.localVariables.add(new LocalVariableNode("green", "F", null, start, end, 52));
        method.localVariables.add(new LocalVariableNode("blue", "F", null, start, end, 53));
        return true;
    }

    @Override
    public boolean recalcFrames(boolean obfuscated) { return true; }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static int getColor(@Nonnull BlockPos pos) {
            return SubaquaticConfigHandler.coloredRain
                    ? BiomeColorHelper.getWaterColorAtPos(Minecraft.getMinecraft().world, pos)
                    : 0xFFFFFF;
        }

        public static float getRed(int color) { return (color >> 16 & 255) / 255.0f; }
        public static float getGreen(int color) { return (color >> 8 & 255) / 255.0f; }
        public static float getBlue(int color) { return (color & 255) / 255.0f; }
    }
}
