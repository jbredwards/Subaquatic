package git.jbredwards.subaquatic.mod.common.item;

import git.jbredwards.subaquatic.mod.common.item.util.ISeaGrowable;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class ItemAquaticBoneMeal extends Item
{
    public ItemAquaticBoneMeal() {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, new Bootstrap.BehaviorDispenseOptional() {
            @Nonnull
            @Override
            protected ItemStack dispenseStack(@Nonnull IBlockSource source, @Nonnull ItemStack stack) {
                final EnumFacing facing = source.getBlockState().getValue(BlockDispenser.FACING);
                final BlockPos pos = source.getBlockPos().offset(facing);
                final IBlockState state = source.getWorld().getBlockState(pos);
                successful = true;

                if(applyDryBonemeal(stack, source.getWorld(), pos, state, facing) || applySeaBonemeal(stack, source.getWorld(), pos, state, facing)) {
                    if(!source.getWorld().isRemote) source.getWorld().playEvent(Constants.WorldEvents.BONEMEAL_PARTICLES, pos, 0);
                }

                else successful = false;
                return stack;
            }
        });
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        final ItemStack held = player.getHeldItem(hand);
        if(!player.canPlayerEdit(pos.offset(facing), facing, held)) return EnumActionResult.FAIL;

        final IBlockState state = worldIn.getBlockState(pos);
        if(applyDryBonemeal(held, worldIn, pos, state, facing) || applySeaBonemeal(held, worldIn, pos, state, facing)) {
            if(!worldIn.isRemote) worldIn.playEvent(Constants.WorldEvents.BONEMEAL_PARTICLES, pos, 0);
            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }

    public boolean applyDryBonemeal(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EnumFacing facing) {
        if(state.getBlock() instanceof ISeaGrowable) {
            final ISeaGrowable growable = (ISeaGrowable)state.getBlock();
            if(growable.canApplySeaBoneMeal(world, pos, state, facing)) {
                if(!world.isRemote) {
                    growable.applySeaBoneMeal(world, pos, state, facing);
                    stack.shrink(1);
                }

                return true;
            }
        }

        return false;
    }

    public boolean applySeaBonemeal(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EnumFacing facing) {

    }

    @Override
    public boolean itemInteractionForEntity(@Nonnull ItemStack stack, @Nonnull EntityPlayer playerIn, @Nonnull EntityLivingBase target, @Nonnull EnumHand hand) {
        if(!target.world.isRemote && target.isCreatureType(EnumCreatureType.WATER_CREATURE, false)) {
            target.heal(Float.MAX_VALUE);
            stack.shrink(1);

            return true;
        }

        return false;
    }
}
