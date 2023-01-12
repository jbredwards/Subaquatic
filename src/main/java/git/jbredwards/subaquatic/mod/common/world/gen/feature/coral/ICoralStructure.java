package git.jbredwards.subaquatic.mod.common.world.gen.feature.coral;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.mod.common.block.AbstractBlockCoral;
import git.jbredwards.subaquatic.mod.common.block.BlockCoral;
import git.jbredwards.subaquatic.mod.common.block.BlockCoralFan;
import git.jbredwards.subaquatic.mod.common.world.gen.feature.GeneratorCoral;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public interface ICoralStructure
{
    /**
     * Generates the main shape of the structure
     */
    void generate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockCoral coral);

    /**
     * Places a coral block and surrounds it with random coral fans/fins
     */
    default boolean placeCoralBlock(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull BlockCoral coral) {
        if(canPlaceCoralAt(world.getBlockState(pos), coral)) {
            final IBlockState up = world.getBlockState(pos.up());
            if(canPlaceCoralAt(up, coral)) { //don't allow coral to generate above water
                //place coral fin
                if(rand.nextFloat() < 0.2) {
                    final AbstractBlockCoral coralFin = rand.nextFloat() < 0.2
                            ? GeneratorCoral.CORAL_FANS.get(rand.nextInt(GeneratorCoral.CORAL_FANS.size()))
                            : GeneratorCoral.CORAL_FINS.get(rand.nextInt(GeneratorCoral.CORAL_FINS.size()));

                    if(canPlaceCoralAt(up, coralFin)) {
                        final IBlockState stateToPlace = coralFin instanceof BlockCoralFan
                                ? coralFin.getDefaultState().withProperty(BlockCoralFan.SIDE, EnumFacing.UP)
                                : coralFin.getDefaultState();

                        world.setBlockState(pos.up(), stateToPlace, 2);
                    }
                }

                //place coral fans
                for(EnumFacing facing : EnumFacing.HORIZONTALS) {
                    if(rand.nextFloat() < 0.2) {
                        final BlockCoralFan coralFan = GeneratorCoral.CORAL_FANS.get(rand.nextInt(GeneratorCoral.CORAL_FANS.size()));
                        final BlockPos offset = pos.offset(facing);

                        if(canPlaceCoralAt(world.getBlockState(offset), coralFan)) world.setBlockState(offset,
                                coralFan.getDefaultState().withProperty(BlockCoralFan.SIDE, facing), 2);
                    }
                }

                world.setBlockState(pos, coral.getDefaultState(), 2);
                return true;
            }
        }

        return false;
    }

    default boolean canPlaceCoralAt(@Nonnull IBlockState here, @Nonnull AbstractBlockCoral coral) {
        return FluidloggedUtils.isCompatibleFluid(coral.neededFluid, FluidloggedUtils.getFluidFromState(here));
    }
}
