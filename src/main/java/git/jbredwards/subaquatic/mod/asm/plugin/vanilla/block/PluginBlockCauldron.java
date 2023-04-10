package git.jbredwards.subaquatic.mod.asm.plugin.vanilla.block;

import git.jbredwards.fluidlogged_api.api.asm.IASMPlugin;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.capability.IEntityBucket;
import git.jbredwards.subaquatic.mod.common.compat.inspirations.InspirationsHandler;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket.IBucketableEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.objectweb.asm.tree.*;

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
    public boolean isMethodValid(@Nonnull MethodNode method, boolean obfuscated) { return method.name.equals(obfuscated ? "func_180639_a" : "onBlockActivated"); }

    @Override
    public boolean transform(@Nonnull InsnList instructions, @Nonnull MethodNode method, @Nonnull AbstractInsnNode insn, boolean obfuscated, int index) {
        /*
         * onBlockActivated: (changes are around line 126)
         * Old code:
         * this.setWaterLevel(worldIn, pos, state, 3);
         *
         * New code:
         * //place any fish within the water bucket
         * this.setWaterLevel(worldIn, pos, state, 3);
         * Hooks.placeCapturedEntity(worldIn, pos, itemstack);
         */
        if(checkMethod(insn, obfuscated ? "func_176590_a" : "setWaterLevel")) {
            final InsnList list = new InsnList();
            list.add(new VarInsnNode(ALOAD, 1));
            list.add(new VarInsnNode(ALOAD, 2));
            list.add(new VarInsnNode(ALOAD, 10));
            list.add(genMethodNode("placeCapturedEntity", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/item/ItemStack;)V"));

            instructions.insert(insn, list);
            return true;
        }

        return false;
    }

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

        return true;
    }

    @Override
    public boolean recalcFrames(boolean obfuscated) { return true; }

    @SuppressWarnings("unused")
    public static final class Hooks
    {
        public static boolean canCauldronRenderInLayer(@Nonnull Block block, @Nonnull BlockRenderLayer layer) {
            //only apply fix to vanilla cauldrons, as not to potentially ruin any modded ones
            return block == Blocks.CAULDRON && layer == BlockRenderLayer.TRANSLUCENT || block.getRenderLayer() == layer;
        }

        @Nullable
        public static Boolean isEntityInsideCauldronMaterial(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Entity entity, double yToTest, @Nonnull Material materialIn, boolean testingHead) {
            if(!SubaquaticConfigHandler.cauldronFluidPhysics || state.getBlock() != Blocks.CAULDRON || !doesCauldronHaveFluid(materialIn, world, pos)) return null;

            final int level = state.getValue(BlockCauldron.LEVEL);
            if(!testingHead) yToTest = entity.posY;

            return level > 0 && yToTest < pos.getY() + getFluidHeight(level);
        }

        @Nullable
        public static Boolean isAABBInsideCauldronMaterial(@Nonnull Block block, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB boundingBox, @Nonnull Material materialIn) {
            return !SubaquaticConfigHandler.cauldronFluidPhysics || block != Blocks.CAULDRON || !doesCauldronHaveFluid(materialIn, world, pos) ? null : block.isAABBInsideLiquid(world, pos, boundingBox);
        }

        @Nullable
        public static Boolean isAABBInsideCauldronLiquid(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull AxisAlignedBB boundingBox) {
            if(!SubaquaticConfigHandler.cauldronFluidPhysics) return null;
            final int level = world.getBlockState(pos).getValue(BlockCauldron.LEVEL);
            return level > 0 && boundingBox.minY < pos.getY() + getFluidHeight(level);
        }

        public static void placeCapturedEntity(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull ItemStack stack) {
            final IEntityBucket cap = IEntityBucket.get(stack);
            if(cap != null) IBucketableEntity.placeCapturedEntity(world, pos, stack, cap.getHandler());
        }

        //helper
        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        public static boolean doesCauldronHaveFluid(@Nonnull Material material, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
            return Subaquatic.isInspirationsInstalled ? InspirationsHandler.doesCauldronHaveMaterial(material, world, pos) : material == Material.WATER;
        }

        //helper
        public static double getFluidHeight(int level) { return level * 0.1875 + 0.375; }
    }
}
