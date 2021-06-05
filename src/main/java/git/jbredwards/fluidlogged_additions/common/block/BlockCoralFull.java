package git.jbredwards.fluidlogged_additions.common.block;

import git.jbredwards.fluidlogged.common.event.FluidloggedEvents;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
@SuppressWarnings("NullableProblems")
public class BlockCoralFull extends Block implements IGrowable
{
    //fluid that must be around this in order for this to stay alive
    //null if this block is dead
    @Nullable public final Fluid fluid;
    //the block that corresponds to this
    @Nonnull public BlockCoralFull corresponding = this;

    public BlockCoralFull(@Nullable Fluid fluid, Material blockMaterialIn, MapColor blockMapColorIn) {
        super(blockMaterialIn, blockMapColorIn);
        this.fluid = fluid;

        setTickRandomly(true);
    }

    public BlockCoralFull(@Nullable Fluid fluid, Material materialIn) {
        super(materialIn);
        this.fluid = fluid;

        setTickRandomly(true);
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        //makes this dead if no fluid is around
        if(!isDead(worldIn, pos, state) && !isUnderwater(worldIn, pos)) {
            worldIn.setBlockState(pos, corresponding.getDefaultState());
        }
    }

    //should be called for each block using this class
    public void setCorresponding(BlockCoralFull correspondingIn) {
        this.corresponding = correspondingIn;
        correspondingIn.corresponding = this;
    }

    public boolean isUnderwater(@Nonnull World world, @Nonnull BlockPos pos) {
        //checks sides
        for(EnumFacing facing : EnumFacing.values()) {
            IBlockState state = world.getBlockState(pos.offset(facing));
            if(FluidloggedEvents.getFluid(state) == fluid) return true;
        }

        //default
        return false;
    }

    public boolean isDead(World world, BlockPos pos, IBlockState state) {
        return fluid == null;
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return isDead(worldIn, pos, state) && isUnderwater(worldIn, pos);
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return canGrow(worldIn, pos, state, worldIn.isRemote);
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        worldIn.setBlockState(pos, corresponding.getDefaultState());
    }
}
