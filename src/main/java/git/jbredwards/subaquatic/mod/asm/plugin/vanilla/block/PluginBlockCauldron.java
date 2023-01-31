package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.block;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.objectweb.asm.tree.ClassNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Allows cauldrons to both have translucent water & to have water collision
 * @author jbred
 *
 */
public final class PluginBlockCauldron implements IASMPlugin
{
    @Override
    public boolean transformClass(@Nonnull ClassNode classNode, boolean obfuscated) {
        /*
         * New code:
         * //allow cauldrons to have translucent water
         * @ASMGenerated
         * public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
         * {
         *     return Hooks.canCauldronRenderInLayer(this, layer);
         * }
         */
        addMethod(classNode, "canRenderInLayer", "(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/util/BlockRenderLayer;)Z",
            "canCauldronRenderInLayer", "(Lnet/minecraft/block/Block;Lnet/minecraft/util/BlockRenderLayer;)Z", generator -> {
                generator.visitVarInsn(ALOAD, 0);
                generator.visitVarInsn(ALOAD, 2);
            }
        );
        /*
         * New code:
         * //allow cauldrons to have water collision
         * @ASMGenerated
         * public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos blockpos, IBlockState iblockstate, Entity entity, double yToTest, Material materialIn, boolean testingHead)
         * {
         *     return Hooks.isEntityInsideCauldronMaterial(world, blockpos, iblockstate, entity, yToTest, materialIn, testingHead);
         * }
         */
        addMethod(classNode, "isEntityInsideMaterial", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/Entity;DLnet/minecraft/block/material/Material;Z)Ljava/lang/Boolean;",
            "isEntityInsideCauldronMaterial", "(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/entity/Entity;DLnet/minecraft/block/material/Material;Z)Ljava/lang/Boolean;", (generator) -> {
                generator.visitVarInsn(ALOAD, 1);
                generator.visitVarInsn(ALOAD, 2);
                generator.visitVarInsn(ALOAD, 3);
                generator.visitVarInsn(ALOAD, 4);
                generator.visitVarInsn(DLOAD, 5);
                generator.visitVarInsn(ALOAD, 7);
                generator.visitVarInsn(ILOAD, 8);
            }
        );
        /*
         * New code:
         * //allow cauldrons to have water collision
         * @ASMGenerated
         * public Boolean isAABBInsideMaterial(World world, BlockPos pos, AxisAlignedBB boundingBox, Material materialIn)
         * {
         *     return Hooks.isAABBInsideCauldronMaterial(this, world, pos, boundingBox, materialIn);
         * }
         */
        addMethod(classNode, "isAABBInsideMaterial", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;Lnet/minecraft/block/material/Material;)Ljava/lang/Boolean;",
            "isAABBInsideCauldronMaterial", "(Lnet/minecraft/block/Block;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;Lnet/minecraft/block/material/Material;)Ljava/lang/Boolean;", (generator) -> {
                generator.visitVarInsn(ALOAD, 0);
                generator.visitVarInsn(ALOAD, 1);
                generator.visitVarInsn(ALOAD, 2);
                generator.visitVarInsn(ALOAD, 3);
                generator.visitVarInsn(ALOAD, 4);
            }
        );
        /*
         * New code:
         * //allow cauldrons to have water collision
         * @ASMGenerated
         * public Boolean isAABBInsideLiquid(World world, BlockPos pos, AxisAlignedBB boundingBox)
         * {
         *     return Hooks.isAABBInsideCauldronLiquid(world, pos, boundingBox);
         * }
         */
        addMethod(classNode, "isAABBInsideLiquid", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/lang/Boolean;",
            "isAABBInsideCauldronLiquid", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/AxisAlignedBB;)Ljava/lang/Boolean;", (generator) -> {
                generator.visitVarInsn(ALOAD, 1);
                generator.visitVarInsn(ALOAD, 2);
                generator.visitVarInsn(ALOAD, 3);
            }
        );

        return false;
    }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static boolean canCauldronRenderInLayer(@Nonnull Block block, @Nonnull BlockRenderLayer layer) {
            //only apply fix to vanilla cauldrons, as not to potentially ruin any modded ones
            return block == Blocks.CAULDRON && layer == BlockRenderLayer.TRANSLUCENT || block.getRenderLayer() == layer;
        }

        @Nullable
        public static Boolean isEntityInsideCauldronMaterial(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Entity entity, double yToTest, @Nonnull Material materialIn, boolean testingHead) {
            if(state.getBlock() != Blocks.CAULDRON || materialIn != Material.WATER) return null;

            final int level = state.getValue(BlockCauldron.LEVEL);
            if(!testingHead) yToTest = entity.posY;

            return level > 0 && yToTest < pos.getY() + 0.375 + level * 0.1875;
        }

        @Nullable
        public static Boolean isAABBInsideCauldronMaterial(@Nonnull Block block, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB boundingBox, @Nonnull Material materialIn) {
            return block != Blocks.CAULDRON || materialIn != Material.WATER ? null : block.isAABBInsideLiquid(world, pos, boundingBox);
        }

        @Nonnull
        public static Boolean isAABBInsideCauldronLiquid(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB boundingBox) {
            final int level = world.getBlockState(pos).getValue(BlockCauldron.LEVEL);
            return level > 0 && boundingBox.minY < pos.getY() + 0.375 + level * 0.1875;
        }
    }
}
