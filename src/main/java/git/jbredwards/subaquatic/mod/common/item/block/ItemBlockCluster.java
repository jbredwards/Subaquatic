/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.item.block;

import git.jbredwards.subaquatic.mod.common.item.util.IBlockCluster;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class ItemBlockCluster extends ItemBlock
{
    @Nonnull
    protected final IBlockCluster clusterHandler;
    public <T extends Block & IBlockCluster> ItemBlockCluster(@Nonnull T blockIn) {
        super(blockIn);
        clusterHandler = blockIn;
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        final ItemStack held = player.getHeldItem(hand);

        BlockPos placedPos = pos;
        IBlockState state = worldIn.getBlockState(placedPos);
        boolean canClusterWith = false;

        //check if this can be clustered here
        if(canPlaceOnSide(worldIn, placedPos, state, facing, held, player, false)) {
            canClusterWith = true;
        }
        else if(Block.isEqualTo(state.getBlock(), block) || !state.getBlock().isReplaceable(worldIn, placedPos)) {
            placedPos = placedPos.offset(facing);
            state = worldIn.getBlockState(placedPos);
        }

        //check if this can be clustered adjacent to this
        if(!canClusterWith && !placedPos.equals(pos) && canPlaceOnSide(worldIn, placedPos, state, facing, held, player, false)) {
            canClusterWith = true;
        }

        //cluster if possible, otherwise try placing it like normal
        if(canClusterWith) { if(!clusterHandler.clusterWith(this, held, player, worldIn, placedPos, facing, hitX, hitY, hitZ, state)) return EnumActionResult.FAIL; }
        else if(state.getBlock() == block || !canPlaceOnSide(worldIn, placedPos, state, facing, held, player, true)
                || !clusterHandler.createInitialCluster(this, held, player, worldIn, placedPos, facing, hitX, hitY, hitZ, state)) return EnumActionResult.FAIL;

        final IBlockState placedState = worldIn.getBlockState(placedPos);
        final SoundType sound = placedState.getBlock().getSoundType(placedState, worldIn, placedPos, player);
        worldIn.playSound(player, placedPos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1) / 2, sound.getPitch() * 0.8f);

        if(!player.isCreative()) held.shrink(1);
        return EnumActionResult.SUCCESS;
    }

    public boolean canPlaceOnSide(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EnumFacing side, @Nonnull ItemStack held, @Nonnull EntityPlayer player, boolean skipCluster) {
        return (skipCluster || Block.isEqualTo(state.getBlock(), block) && clusterHandler.canClusterWith(held, player, world, pos, side, state))
                && block.canPlaceBlockOnSide(world, pos, side) && player.canPlayerEdit(pos.offset(EnumFacing.UP), EnumFacing.UP, held)
                ;//&& !ForgeEventFactory.onBlockPlace(player, new BlockSnapshot(world, pos, block.getDefaultState()), side).isCanceled();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public final boolean canPlaceBlockOnSide(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumFacing side, @Nonnull EntityPlayer player, @Nonnull ItemStack stack) {
        return true;
    }
}
