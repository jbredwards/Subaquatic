package git.jbredwards.subaquatic.mod.asm.plugin.modded;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.common.capability.IFishBucket;
import git.jbredwards.subaquatic.mod.common.entity.util.IBucketableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.objectweb.asm.tree.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Place any fish contained within the bucket when fluidlogging, and yes I'm asm-ing my own mod XD
 * @author jbred
 *
 */
public final class PluginFluidloggedAPI implements IASMPlugin
{
    @Override
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals("tryBucketDrain"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * tryBucketDrain:
         * Old code:
         * else if(handler.drain(new FluidStack(stack, fluidState.getFluidBlock().place(world, pos, stack.copy(), true)), true) != null)
         * {
         *     ...
         * }
         *
         * //New code:
         * place any fish contained within the bucket when fluidlogging
         * else if(Hooks.drainAndPlaceFish(handler, new FluidStack(stack, fluidState.getFluidBlock().place(world, pos, stack.copy(), true)), world, pos) != null)
         * {
         *     ...
         * }
         */
        if(checkMethod(insn, "drain")) {
            instructions.insert(insn, genMethodNode("drainAndPlaceFish", "(Lnet/minecraftforge/fluids/capability/IFluidHandlerItem;Lnet/minecraftforge/fluids/FluidStack;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraftforge/fluids/FluidStack;"));
            instructions.insert(insn, new VarInsnNode(ALOAD, 1));
            instructions.insert(insn, new VarInsnNode(ALOAD, 0));
            removeFrom(instructions, insn, -1);
            return true;
        }

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @Nullable
        public static FluidStack drainAndPlaceFish(@Nonnull IFluidHandlerItem handler, @Nonnull FluidStack stack, @Nonnull World world, @Nonnull BlockPos pos) {
            final ItemStack bucket = handler.getContainer();
            final FluidStack result = handler.drain(stack, true);

            if(result != null) {
                final IFishBucket cap = IFishBucket.get(bucket);
                if(cap != null) IBucketableEntity.placeCapturedEntity(world, pos, bucket, cap.getData());
            }

            return result;
        }
    }
}
