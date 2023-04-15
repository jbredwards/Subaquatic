package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.block;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import net.minecraft.block.BlockLever;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Add lever redstone particles
 * @author jbred
 *
 */
public final class PluginBlockLever implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * randomDisplayTick:
         * New code:
         * //PluginWorldClient removes the hardcoded barrier particle spawning system, this reimplements it
         * @ASMGenerated
         * @SideOnly(Side.CLIENT)
         * public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
         * {
         *     Hooks.addLeverParticles(state, world, pos, rand);
         * }
         */
        addMethod(classNode, obfuscated ? "func_180655_c" : "randomDisplayTick", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)V",
            "addLeverParticles", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)V", generator -> {
                //adds the client-side only annotation
                generator.visitAnnotation("Lnet/minecraftforge/fml/relauncher/SideOnly;", true)
                        .visitEnum("value", "Lnet/minecraftforge/fml/relauncher/Side;", "CLIENT");
                generator.visitVarInsn(ALOAD, 1);
                generator.visitVarInsn(ALOAD, 2);
                generator.visitVarInsn(ALOAD, 3);
                generator.visitVarInsn(ALOAD, 4);
            }
        );

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static void addLeverParticles(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Random rand) {
            if(state.getValue(BlockLever.POWERED) && rand.nextFloat() < 0.25) {
                final EnumFacing facing = state.getValue(BlockLever.FACING).getFacing().getOpposite();
                world.spawnParticle(EnumParticleTypes.REDSTONE,
                        pos.getX() + 0.5 + 0.3 * facing.getXOffset(),
                        pos.getY() + 0.5 + 0.3 * facing.getYOffset(),
                        pos.getZ() + 0.5 + 0.3 * facing.getZOffset(),
                        0, 0, 0);
            }
        }
    }
}
