package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.item;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.common.capability.ICompactFishing;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Transfer modded fishing rod enchantments to the fishing hook entity
 * @author jbred
 *
 */
public final class PluginItemFishingRod implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_77659_a" : "onItemRightClick"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * onItemRightClick: (changes are around line 106)
         * Old code:
         * worldIn.spawnEntity(entityfishhook);
         *
         * New code:
         * //transfer modded fishing rod enchantments to the fishing hook entity
         * worldIn.spawnEntity(Hooks.transferModdedEnchData(entityfishhook, itemstack));
         */
        if(checkMethod(insn, obfuscated ? "func_72838_d" : "spawnEntity")) {
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 4));
            instructions.insertBefore(insn, genMethodNode("transferModdedEnchData", "(Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/Entity;"));
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @Nonnull
        public static Entity transferModdedEnchData(@Nonnull Entity entity, @Nonnull ItemStack stack) {
            final ICompactFishing cap = ICompactFishing.get(entity);
            if(cap != null) cap.setLevel(EnchantmentHelper.getEnchantmentLevel(SubaquaticEnchantments.COMPACT_FISHING, stack));

            return entity;
        }
    }
}
