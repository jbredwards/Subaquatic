package git.jbredwards.subaquatic.mod.common.item;

import git.jbredwards.subaquatic.mod.common.entity.util.PositionedEntitySupplier;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class ItemMinecartTypeless extends Item
{
    @Nonnull
    protected final PositionedEntitySupplier<EntityMinecart> minecartSupplier;
    public ItemMinecartTypeless(@Nonnull PositionedEntitySupplier<EntityMinecart> minecartSupplierIn) {
        minecartSupplier = minecartSupplierIn;

        setMaxStackSize(1);
        registerDispenserBehavior();
    }

    protected void registerDispenserBehavior() {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, new BehaviorDefaultDispenseItem() {
            @Nonnull
            @Override
            protected ItemStack dispenseStack(@Nonnull IBlockSource source, @Nonnull ItemStack stack) {
                final EnumFacing dispenserFacing = source.getBlockState().getValue(BlockDispenser.FACING);
                final World world = source.getWorld();
                final double x = source.getX() + dispenserFacing.getXOffset() * 1.125;
                final double y = Math.floor(source.getY()) + dispenserFacing.getYOffset();
                final double z = source.getZ() + dispenserFacing.getZOffset() * 1.125;

                final BlockPos pos = source.getBlockPos().offset(dispenserFacing);
                final IBlockState state = world.getBlockState(pos);
                double yOffset;

                if(BlockRailBase.isRailBlock(state))
                    yOffset = (state.getBlock() instanceof BlockRailBase ? ((BlockRailBase)state.getBlock()).getRailDirection(world, pos, state, null) : BlockRailBase.EnumRailDirection.NORTH_SOUTH).isAscending() ? 0.6 : 0.1;

                else {
                    final IBlockState downState = world.getBlockState(pos.down());
                    if(state.getMaterial() != Material.AIR || !BlockRailBase.isRailBlock(downState)) {
                        playDispenseSound(source);
                        spawnDispenseParticles(source, dispenserFacing);
                        return super.dispenseStack(source, stack);
                    }

                    yOffset = dispenserFacing != EnumFacing.DOWN && (downState.getBlock() instanceof BlockRailBase ? ((BlockRailBase)downState.getBlock()).getRailDirection(world, pos.down(), downState, null) : BlockRailBase.EnumRailDirection.NORTH_SOUTH).isAscending() ? -0.4 : -0.9;
                }

                final EntityMinecart minecart = minecartSupplier.generate(world, x, y + yOffset, z);
                if(stack.hasDisplayName()) minecart.setCustomNameTag(stack.getDisplayName());
                world.spawnEntity(minecart);

                stack.shrink(1);
                return stack;
            }
        });
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        final IBlockState state = worldIn.getBlockState(pos);
        if(!BlockRailBase.isRailBlock(state)) return EnumActionResult.FAIL;

        final ItemStack held = player.getHeldItem(hand);
        if(!worldIn.isRemote) {
            final double yOffset = (state.getBlock() instanceof BlockRailBase ? ((BlockRailBase)state.getBlock()).getRailDirection(worldIn, pos, state, null) : BlockRailBase.EnumRailDirection.NORTH_SOUTH).isAscending() ? 0.5 : 0;
            final EntityMinecart minecart = minecartSupplier.generate(worldIn, pos.getX() + 0.5, pos.getY() + yOffset + 0.0625, pos.getZ() + 0.5);

            if(held.hasDisplayName()) minecart.setCustomNameTag(held.getDisplayName());
            worldIn.spawnEntity(minecart);
        }

        held.shrink(1);
        return EnumActionResult.SUCCESS;
    }
}
