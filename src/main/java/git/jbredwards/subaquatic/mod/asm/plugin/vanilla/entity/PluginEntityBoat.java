package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.entity;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.common.capability.IBubbleColumn;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Bubble columns rock boats
 * @author jbred
 *
 */
public final class PluginEntityBoat implements IASMPlugin
{
    @Override
    public int getMethodIndex(@Nonnull MethodNode method, boolean obfuscated) {
        if(method.name.equals("<clinit>")) return 1;
        else if(method.name.equals(obfuscated ? "func_70088_a" : "entityInit")) return 2;
        else return method.name.equals(obfuscated ? "func_70071_h_" : "onUpdate") ? 3 : 0;
    }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        //called during EntityBoat.<clinit> to ensure ROCKING_TICKS is initialized at the correct time
        if(index == 1 && insn.getPrevious() == instructions.getFirst()) {
            instructions.insertBefore(insn, genMethodNode("clinit", "()V"));
            return true;
        }
        /*
         * entityInit: (changes are around line 103)
         * Old code:
         * {
         *     ...
         * }
         *
         * New code:
         * //register new data parameter
         * {
         *     Hooks.registerRockingTicks(this);
         *     ...
         * }
         */
        else if(index == 2 && insn.getPrevious() == instructions.getFirst()) {
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 0));
            instructions.insertBefore(insn, genMethodNode("registerRockingTicks", "(Lnet/minecraft/entity/item/EntityBoat;)V"));
            return true;
        }
        /*
         * onUpdate: (changes are around line 363)
         * Old code:
         * this.doBlockCollisions();
         *
         * New code:
         * //bubble columns rock boats
         * this.doBlockCollisions();
         * Hooks.updateRocking(this);
         */
        else if(index == 3 && checkMethod(insn, obfuscated ? "func_145775_I" : "doBlockCollisions")) {
            instructions.insert(insn, genMethodNode("updateRocking", "(Lnet/minecraft/entity/item/EntityBoat;)V"));
            instructions.insert(insn, new VarInsnNode(ALOAD, 0));
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @Nonnull
        public static final DataParameter<Integer> ROCKING_TICKS = EntityDataManager.createKey(EntityBoat.class, DataSerializers.VARINT);
        public static void clinit() {} //called during EntityBoat.<clinit> to ensure ROCKING_TICKS is initialized at the correct time

        public static void registerRockingTicks(@Nonnull EntityBoat boat) { boat.getDataManager().register(ROCKING_TICKS, 0); }
        public static void updateRocking(@Nonnull EntityBoat boat) {
            final @Nullable IBubbleColumn cap = IBubbleColumn.get(boat);
            if(cap instanceof IBubbleColumn.Boat) ((IBubbleColumn.Boat)cap).updateRocking(boat);
        }
    }
}
