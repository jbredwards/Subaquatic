package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.block;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Backport the vanilla 1.13+ beacon sounds
 * @author jbred
 *
 */
public final class PluginTileEntityBeacon implements IASMPlugin
{
    @Override
    public int getMethodIndex(@Nonnull MethodNode method, boolean obfuscated) {
        if(method.name.equals(obfuscated ? "func_73660_a" : "update")) return 1;
        else return method.name.equals(obfuscated ? "func_174885_b" : "setField") ? 2 : 0;
    }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        if(index == 1) {
            /*
             * update: (changes are around line 67)
             * Old code:
             * {
             *     ...
             * }
             *
             * New code:
             * //play activate & deactivate sounds
             * {
             *     Hooks.playUpdateSound(this, this.isComplete, this.prevIsComplete);
             *     this.prevIsComplete = this.isComplete;
             *     ...
             * }
             */
            if(insn == instructions.getFirst()) {
                final InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new FieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityBeacon", obfuscated ? "field_146015_k" : "isComplete", "Z"));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new FieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityBeacon", "prevIsComplete", "Z"));
                list.add(genMethodNode("playUpdateSound", "(Lnet/minecraft/tileentity/TileEntity;ZZ)V"));

                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new FieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityBeacon", obfuscated ? "field_146015_k" : "isComplete", "Z"));
                list.add(new FieldInsnNode(PUTFIELD, "net/minecraft/tileentity/TileEntityBeacon", "prevIsComplete", "Z"));
                instructions.insert(insn, list);
            }
            /*
             * update: (changes are around line 69)
             * Old code:
             * this.updateBeacon();
             *
             * New code:
             * //play ambient sound
             * this.updateBeacon();
             * Hooks.playAmbientSound(this, this.isComplete);
             */
            else if(checkMethod(insn, obfuscated ? "func_174908_m" : "updateBeacon")) {
                final InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new FieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityBeacon", obfuscated ? "field_146015_k" : "isComplete", "Z"));
                list.add(genMethodNode("playAmbientSound", "(Lnet/minecraft/tileentity/TileEntity;Z)V"));
                instructions.insert(insn, list);
                return true;
            }
        }
        /*
         * setField: (changes are around line 69)
         * Old code:
         * {
         *     ...
         * }
         *
         * New code:
         * //play select sound
         * {
         *     Hooks.playPowerSelectSound(this, this.isComplete);
         *     ...
         * }
         */
        else if(index == 2) {
            final InsnList list = new InsnList();
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new VarInsnNode(ALOAD, 0));
            list.add(new FieldInsnNode(GETFIELD, "net/minecraft/tileentity/TileEntityBeacon", obfuscated ? "field_146015_k" : "isComplete", "Z"));
            list.add(genMethodNode("playPowerSelectSound", "(Lnet/minecraft/tileentity/TileEntity;Z)V"));
            instructions.insert(insn, list);
            return true;
        }

        return false;
    }

    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        classNode.fields.add(new FieldNode(ACC_PUBLIC, "prevIsComplete", "Z", null, null));
        return true;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static void playAmbientSound(@Nonnull TileEntity tile, boolean isComplete) {
            if(isComplete && !tile.getWorld().isRemote)
                tile.getWorld().playSound(null, tile.getPos(), SubaquaticSounds.BEACON_AMBIENT, SoundCategory.BLOCKS, 1, 1);
        }

        public static void playPowerSelectSound(@Nonnull TileEntity tile, boolean isComplete) {
            if(isComplete && !tile.getWorld().isRemote)
                tile.getWorld().playSound(null, tile.getPos(), SubaquaticSounds.BEACON_POWER_SELECT, SoundCategory.BLOCKS, 1, 1);
        }

        public static void playUpdateSound(@Nonnull TileEntity tile, boolean isComplete, boolean prevIsComplete) {
            if(isComplete != prevIsComplete && !tile.getWorld().isRemote)
                tile.getWorld().playSound(null, tile.getPos(), isComplete ? SubaquaticSounds.BEACON_ACTIVATE : SubaquaticSounds.BEACON_DEACTIVATE, SoundCategory.BLOCKS, 1, 1);
        }
    }
}
