package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class BlockCoral extends AbstractBlockCoral
{
    public BlockCoral(@Nonnull Fluid neededFluidIn, @Nonnull Material materialIn) { super(neededFluidIn, materialIn); }
    public BlockCoral(@Nonnull Fluid neededFluidIn, @Nonnull Material materialIn, @Nonnull MapColor mapColorIn) {
        super(neededFluidIn, materialIn, mapColorIn);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, ALIVE); }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) { return getDefaultState().withProperty(ALIVE, (meta & 1) == 0); }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) { return state.getValue(ALIVE) ? 0 : 1; }

    @Override
    public boolean hasNeededFluid(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        for(EnumFacing facing : EnumFacing.values()) {
            if(FluidloggedUtils.isCompatibleFluid(neededFluid, FluidloggedUtils.getFluidState(world, pos.offset(facing)).getFluid()))
                return true;
        }

        return false;
    }
}
