package git.jbredwards.subaquatic.mod.asm.plugin.forge;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.common.capability.IEntityBucket;
import git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket.IBucketableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;

/**
 * Place fish contained within bucket
 * @author jbred
 *
 */
public final class PluginFluidUtil implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) {
        return checkMethod(method, "tryPlaceFluid", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraftforge/fluids/capability/IFluidHandler;Lnet/minecraftforge/fluids/FluidStack;)Z");
    }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * tryPlaceFluid: (changes are around line 645)
         * Old code:
         * if (world.provider.doesWaterVaporize() && fluid.doesVaporize(resource))
         * {
         *     ...
         * }
         *
         * New code:
         * //cache the old bucket item
         * if (Hooks.doesWaterVaporize(world.provider, fluidSource) && fluid.doesVaporize(resource))
         * {
         *     ...
         * }
         */
        if(checkMethod(insn, obfuscated ? "func_177500_n" : "doesWaterVaporize")) {
            instructions.insertBefore(insn, new VarInsnNode(ALOAD, 3));
            instructions.insertBefore(insn, genMethodNode("doesWaterVaporize", "(Lnet/minecraft/world/WorldProvider;Lnet/minecraftforge/fluids/capability/IFluidHandler;)Z"));
            instructions.remove(insn);
        }
        /*
         * tryPlaceFluid: (changes are around line 650)
         * Old code:
         * result.getFluid().vaporize(player, world, pos, result);
         *
         * New code:
         * //place fish contained within bucket
         * result.getFluid().vaporize(player, world, pos, result);
         * Hooks.placeFish(world, pos);
         */
        else if(checkMethod(insn, "vaporize")) {
            final InsnList list = new InsnList();
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new VarInsnNode(ALOAD, 2));
            list.add(genMethodNode("placeFish", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"));
            instructions.insert(insn, list);
        }
        /*
         * tryPlaceFluid: (changes are around line 661)
         * Old code:
         * SoundEvent soundevent = resource.getFluid().getEmptySound(resource);
         *
         * New code:
         * //place fish contained within bucket
         * SoundEvent soundevent = resource.getFluid().getEmptySound(resource);
         * Hooks.placeFish(world, pos);
         */
        else if(checkMethod(insn.getPrevious(), "getEmptySound")) {
            final InsnList list = new InsnList();
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new VarInsnNode(ALOAD, 2));
            list.add(genMethodNode("placeFish", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"));
            instructions.insert(insn, list);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @Nonnull
        private static ItemStack filledBucket = ItemStack.EMPTY;
        public static boolean doesWaterVaporize(@Nonnull WorldProvider provider, @Nonnull IFluidHandler handler) {
            if(handler instanceof IFluidHandlerItem) filledBucket = ((IFluidHandlerItem)handler).getContainer();
            return provider.doesWaterVaporize();
        }

        public static void placeFish(@Nonnull World world, @Nonnull BlockPos pos) {
            final IEntityBucket cap = IEntityBucket.get(filledBucket);
            if(cap != null) IBucketableEntity.placeCapturedEntity(world, pos, filledBucket, cap.getHandler());
        }
    }
}
