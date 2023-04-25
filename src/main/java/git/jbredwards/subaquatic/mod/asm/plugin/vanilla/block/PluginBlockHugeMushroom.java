package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.block;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Allow the huge mushroom item blocks to be more accessible & useful outside just commands
 * @author jbred
 *
 */
public final class PluginBlockHugeMushroom implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * getItem:
         * New code:
         * //allow creative players to "pick-block" huge mushrooms
         * public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
         * {
         *     return Hooks.getSilkTouchDrop(state);
         * }
         */
        overrideMethod(classNode, method -> method.name.equals(obfuscated ? "func_185473_a" : "getItem"),
            "getSilkTouchDrop", "(Lnet/minecraft/block/state/IBlockState;)Lnet/minecraft/item/ItemStack;",
                generator -> generator.visitVarInsn(ALOAD, 3));
        /*
         * canSilkHarvest:
         * New code:
         * //allow players to silk touch huge mushrooms
         * @ASMGenerated
         * public boolean canSilkHarvest()
         * {
         *     return SubaquaticConfigHandler.Server.Block.mushroomBlockSilkTouch;
         * }
         */
        addMethod(classNode, obfuscated ? "func_149700_E" : "canSilkHarvest", "()Z", null, null,
            generator -> generator.visitFieldInsn(GETSTATIC, "git/jbredwards/subaquatic/mod/common/config/SubaquaticConfigHandler$Server$Block", "mushroomBlockSilkTouch", "Z"));
        /*
         * getSilkTouchDrop:
         * New code:
         * //allow players to silk touch huge mushrooms & stems
         * @ASMGenerated
         * public ItemStack getSilkTouchDrop(IBlockState state)
         * {
         *     return Hooks.getSilkTouchDrop(state);
         * }
         */
        addMethod(classNode, obfuscated ? "func_180643_i" : "getSilkTouchDrop", "(Lnet/minecraft/block/state/IBlockState;)Lnet/minecraft/item/ItemStack;",
            "getSilkTouchDrop", "(Lnet/minecraft/block/state/IBlockState;)Lnet/minecraft/item/ItemStack;",
                generator -> generator.visitVarInsn(ALOAD, 1));
        /*
         * getStateForPlacement:
         * New code:
         * //better mushroom states when placed
         * public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
         * {
         *     return Hooks.getStateForPlacement(this, worldIn, pos, meta);
         * }
         */
        overrideMethod(classNode, method -> method.name.equals(obfuscated ? "func_180642_a" : "getStateForPlacement"),
            "getStateForPlacement", "(Lnet/minecraft/block/BlockHugeMushroom;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;I)Lnet/minecraft/block/state/IBlockState;", generator -> {
                generator.visitVarInsn(ALOAD, 0);
                generator.visitVarInsn(ALOAD, 1);
                generator.visitVarInsn(ALOAD, 2);
                generator.visitVarInsn(ILOAD, 7);
            }
        );
        /*
         * getSilkTouchDrop:
         * New code:
         * //configurable fortune & stem harvesting
         * @ASMGenerated
         * public int quantityDropped(IBlockState state, int fortune, Random random)
         * {
         *     return Hooks.quantityDropped(state, fortune, random);
         * }
         */
        addMethod(classNode, "quantityDropped", "(Lnet/minecraft/block/state/IBlockState;ILjava/util/Random;)I",
            "quantityDropped", "(Lnet/minecraft/block/state/IBlockState;ILjava/util/Random;)I", generator -> {
                generator.visitVarInsn(ALOAD, 1);
                generator.visitVarInsn(ILOAD, 2);
                generator.visitVarInsn(ALOAD, 3);
            }
        );

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        @Nonnull
        public static ItemStack getSilkTouchDrop(@Nonnull IBlockState state) {
            if(state.getBlock() == Blocks.RED_MUSHROOM_BLOCK || state.getBlock() == Blocks.BROWN_MUSHROOM_BLOCK) {
                switch(state.getValue(BlockHugeMushroom.VARIANT)) {
                    case STEM: return new ItemStack(state.getBlock(), 1, 1);
                    case ALL_STEM: return new ItemStack(state.getBlock(), 1, 2);
                }
            }

            return new ItemStack(state.getBlock(), 1, 0);
        }

        @Nonnull
        public static IBlockState getStateForPlacement(@Nonnull BlockHugeMushroom block, @Nonnull World world, @Nonnull BlockPos pos, int meta) {
            switch(meta) {
                case 1: return block.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.STEM);
                case 2: return block.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.ALL_STEM);
                default: return block.getDefaultState();
            }
        }

        public static int quantityDropped(@Nonnull IBlockState state, int fortune, @Nonnull Random rand) {
            if(!SubaquaticConfigHandler.Server.Block.mushroomStemsDropMushrooms) {
                switch(state.getValue(BlockHugeMushroom.VARIANT)) {
                    case STEM:
                    case ALL_STEM: return 0;
                }
            }

            final int oldQuantityDropped = state.getBlock().quantityDropped(rand);
            return SubaquaticConfigHandler.Server.Block.mushroomBlockFortune ? Math.min(4, oldQuantityDropped + rand.nextInt(fortune + 1)) : oldQuantityDropped;
        }
    }
}
