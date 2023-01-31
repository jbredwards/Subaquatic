package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.fluidlogged_api.mod.asm.plugins.vanilla.block.PluginBlockBush;
import git.jbredwards.subaquatic.api.block.IChorusPlantSoil;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class BlockEndSand extends BlockFalling implements IChorusPlantSoil
{
    @SideOnly(Side.CLIENT)
    @Override
    public int getDustColor(@Nonnull IBlockState state) { return 0xc3bd89; }

    @Override
    public boolean canSustainPlant(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull EnumFacing direction, @Nonnull IPlantable plantable) {
        if(plantable instanceof PluginBlockBush.Accessor && ((PluginBlockBush.Accessor)plantable).canSustainBush_Public(state)) return true;
        switch(plantable.getPlantType(world, pos.offset(direction))) {
            case Desert: return true;
            case Beach: return
                    FluidloggedUtils.getFluidOrReal(world, pos.east()).getMaterial() == Material.WATER ||
                    FluidloggedUtils.getFluidOrReal(world, pos.west()).getMaterial() == Material.WATER ||
                    FluidloggedUtils.getFluidOrReal(world, pos.north()).getMaterial() == Material.WATER ||
                    FluidloggedUtils.getFluidOrReal(world, pos.south()).getMaterial() == Material.WATER;

            default: return false;
        }
    }
}
