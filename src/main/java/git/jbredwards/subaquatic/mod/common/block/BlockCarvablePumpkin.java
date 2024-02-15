/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class BlockCarvablePumpkin extends Block
{
    public BlockCarvablePumpkin(@Nonnull Material materialIn) { this(materialIn, materialIn.getMaterialMapColor()); }
    public BlockCarvablePumpkin(@Nonnull Material materialIn, @Nonnull MapColor mapColorIn) {
        super(materialIn, mapColorIn);
    }

    @Override
    public boolean onBlockActivated(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        final ItemStack held = playerIn.getHeldItem(hand);
        if(held.getItem() instanceof ItemShears) {
            if(!worldIn.isRemote) {
                final EnumFacing pumpkinFacing = facing.getAxis().isVertical() ? playerIn.getHorizontalFacing().getOpposite() : facing;
                worldIn.setBlockState(pos, Blocks.PUMPKIN.getDefaultState().withProperty(BlockPumpkin.FACING, pumpkinFacing));
                worldIn.playSound(null, pos, SubaquaticSounds.PUMPKIN_CARVE, SoundCategory.BLOCKS, 1, 1);

                final EntityItem item = new EntityItem(worldIn,
                        pos.getX() + pumpkinFacing.getXOffset() * 0.65 + 0.5,
                        pos.getY() + 0.1,
                        pos.getZ() + pumpkinFacing.getZOffset() * 0.65 + 0.5,
                        new ItemStack(Items.PUMPKIN_SEEDS, 4));

                item.motionX = 0.05 * pumpkinFacing.getXOffset() + worldIn.rand.nextDouble() * 0.02;
                item.motionY = 0.05;
                item.motionZ = 0.05 * pumpkinFacing.getZOffset() + worldIn.rand.nextDouble() * 0.02;
                worldIn.spawnEntity(item);

                held.damageItem(1, playerIn);
            }

            return true;
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

    //exists so walls, fences, and glass panes don't connect to this
    @Nonnull
    @Override
    public BlockFaceShape getBlockFaceShape(@Nonnull IBlockAccess worldIn, @Nonnull IBlockState state, @Nonnull BlockPos pos, @Nonnull EnumFacing face) {
        if(face.getAxis().isVertical()) return BlockFaceShape.SOLID;
        final IBlockState neighbor = worldIn.getBlockState(pos.offset(face));
        if(neighbor.getBlock() instanceof BlockCarvablePumpkin) return BlockFaceShape.SOLID;

        switch(neighbor.getBlockFaceShape(worldIn, pos, face.getOpposite())) {
            case MIDDLE_POLE:
            case MIDDLE_POLE_THIN:
            case MIDDLE_POLE_THICK: return BlockFaceShape.UNDEFINED;
            default: return BlockFaceShape.SOLID;
        }
    }
}
