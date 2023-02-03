package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.block;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.api.block.IChorusPlantSoil;
import net.minecraft.block.BlockChorusPlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;

/**
 * Implement IChorusPlantSoil functionality
 * @author jbred
 *
 */
public final class PluginBlockChorusPlant implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * New code:
         * //implement IChorusPlantSoil functionality
         * public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
         * {
         *     return Hooks.getActualState(this, state, worldIn, pos);
         * }
         */
        overrideMethod(classNode, method -> method.name.equals(obfuscated ? "func_176221_a" : "getActualState"),
            "getActualState", "(Lnet/minecraft/block/BlockChorusPlant;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/state/IBlockState;", generator -> {
                generator.visitVarInsn(ALOAD, 0);
                generator.visitVarInsn(ALOAD, 1);
                generator.visitVarInsn(ALOAD, 2);
                generator.visitVarInsn(ALOAD, 3);
            }
        );
        /*
         * New code:
         * //implement IChorusPlantSoil functionality
         * public boolean canSurviveAt(World wordIn, BlockPos pos)
         * {
         *     return Hooks.canSurviveAt(this, worldIn, pos);
         * }
         */
        overrideMethod(classNode, method -> method.name.equals(obfuscated ? "func_185608_b" : "canSurviveAt"),
            "canSurviveAt", "(Lnet/minecraft/block/BlockChorusPlant;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Z", generator -> {
                generator.visitVarInsn(ALOAD, 0);
                generator.visitVarInsn(ALOAD, 1);
                generator.visitVarInsn(ALOAD, 2);
            }
        );
        /*
         * New code:
         * //implement IChorusPlantSoil functionality
         * @SideOnly(Side.CLIENT)
         * public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
         * {
         *     return Hooks.shouldSideBeRendered(this, worldIn, pos, side);
         * }
         */
        overrideMethod(classNode, method -> method.name.equals(obfuscated ? "func_185608_b" : "shouldSideBeRendered"),
            "shouldSideBeRendered", "(Lnet/minecraft/block/BlockChorusPlant;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z", generator -> {
                generator.visitVarInsn(ALOAD, 0);
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
        //helper
        public static boolean canConnectTo(@Nonnull BlockChorusPlant block, @Nonnull IBlockState state, boolean checkSoil) {
            return block == state.getBlock() || state.getBlock() == Blocks.CHORUS_FLOWER || checkSoil && IChorusPlantSoil.isStateValidSoil(state);
        }

        public static boolean canSurviveAt(@Nonnull BlockChorusPlant block, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
            final IBlockState down = world.getBlockState(pos.down());

            final boolean isDownAir = down.getBlock().isAir(down, world, pos.down());
            final boolean isUpAir = world.isAirBlock(pos.up());

            for(EnumFacing side : EnumFacing.HORIZONTALS) {
                final BlockPos offset = pos.offset(side);

                if(block == world.getBlockState(offset).getBlock()) {
                    if(!isDownAir && !isUpAir) return false;

                    final IBlockState downNeighbor = world.getBlockState(offset.down());
                    if(block == downNeighbor.getBlock() && IChorusPlantSoil.isStateValidSoil(downNeighbor))
                        return true;
                }
            }

            return block == down.getBlock() && IChorusPlantSoil.isStateValidSoil(down);
        }

        @Nonnull
        public static IBlockState getActualState(@Nonnull BlockChorusPlant block, @Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
            return state
                    .withProperty(BlockChorusPlant.DOWN, canConnectTo(block, world.getBlockState(pos.down()), true))
                    .withProperty(BlockChorusPlant.UP, canConnectTo(block, world.getBlockState(pos.up()), false))
                    .withProperty(BlockChorusPlant.NORTH, canConnectTo(block, world.getBlockState(pos.north()), false))
                    .withProperty(BlockChorusPlant.SOUTH, canConnectTo(block, world.getBlockState(pos.south()), false))
                    .withProperty(BlockChorusPlant.EAST, canConnectTo(block, world.getBlockState(pos.east()), false))
                    .withProperty(BlockChorusPlant.WEST, canConnectTo(block, world.getBlockState(pos.west()), false));
        }

        public static boolean shouldSideBeRendered(@Nonnull BlockChorusPlant block, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
            return !canConnectTo(block, world.getBlockState(pos.offset(side)), side == EnumFacing.DOWN);
        }
    }
}
