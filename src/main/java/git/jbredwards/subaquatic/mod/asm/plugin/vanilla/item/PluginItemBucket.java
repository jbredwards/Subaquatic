package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.item;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.common.capability.IFishBucket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Place fish contained within bucket
 * @author jbred
 *
 */
public final class PluginItemBucket implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_77659_a" : "onItemRightClick"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * onItemRightClick: (changes are around line 106)
         * Old code:
         * else if (this.tryPlaceContainedLiquid(playerIn, worldIn, blockpos1))
         * {
         *     ...
         * }
         *
         * New code:
         * //place fish contained within bucket
         * else if (Hooks.tryPlaceContainedLiquid(this, playerIn, worldIn, blockpos1, itemstack))
         * {
         *     ...
         * }
         */
        if(checkMethod(insn, obfuscated ? "func_180616_a" : "tryPlaceContainedLiquid")) {
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 5));
            instructions.insertBefore(insn, genMethodNode("tryPlaceContainedLiquid", "(Lnet/minecraft/item/ItemBucket;Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)Z"));
            instructions.remove(insn);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static boolean tryPlaceContainedLiquid(@Nonnull ItemBucket item, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull ItemStack stack) {
            if(item.tryPlaceContainedLiquid(player, world, pos)) {
                final IFishBucket cap = IFishBucket.get(stack);
                if(cap != null) cap.placeEntity(world, pos, stack);
                return true;
            }

            return false;
        }
    }
}
