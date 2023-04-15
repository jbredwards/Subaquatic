package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticItems;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
@Mod.EventBusSubscriber(modid = Subaquatic.MODID)
public class BlockRootedDirt extends Block implements IGrowable
{
    public BlockRootedDirt(@Nonnull Material materialIn) { this(materialIn, materialIn.getMaterialMapColor()); }
    public BlockRootedDirt(@Nonnull Material materialIn, @Nonnull MapColor mapColorIn) { super(materialIn, mapColorIn); }

    @Override
    public boolean canSustainPlant(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing direction, @Nonnull IPlantable plantable) {
        switch(plantable.getPlantType(world, pos.offset(direction))) {
            case Plains: return true;
            case Beach: return
                    FluidloggedUtils.getFluidOrReal(world, pos.east()).getMaterial() == Material.WATER ||
                    FluidloggedUtils.getFluidOrReal(world, pos.west()).getMaterial() == Material.WATER ||
                    FluidloggedUtils.getFluidOrReal(world, pos.north()).getMaterial() == Material.WATER ||
                    FluidloggedUtils.getFluidOrReal(world, pos.south()).getMaterial() == Material.WATER;

            default: return false;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    static void onTillRootedDirt(@Nonnull UseHoeEvent event) {
        if(event.getResult() == Event.Result.DEFAULT) {
            final World world = event.getWorld();
            final BlockPos pos = event.getPos();
            final IBlockState state = world.getBlockState(pos);

            if(state.getBlock() instanceof BlockRootedDirt && world.isAirBlock(pos.up())) {
                world.playSound(event.getEntityPlayer(), pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1, 1);
                if(!world.isRemote) {
                    spawnAsEntity(world, pos, new ItemStack(SubaquaticItems.HANGING_ROOTS));
                    world.setBlockState(pos, Blocks.FARMLAND.getDefaultState(), 11);
                }

                event.setResult(Event.Result.ALLOW);
            }
        }
    }

    @Override
    public boolean canGrow(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean isClient) {
        return worldIn.isAirBlock(pos.down());
    }

    @Override
    public boolean canUseBonemeal(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        return true;
    }

    @Override
    public void grow(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        worldIn.setBlockState(pos.down(), SubaquaticBlocks.HANGING_ROOTS.getDefaultState());
    }
}
