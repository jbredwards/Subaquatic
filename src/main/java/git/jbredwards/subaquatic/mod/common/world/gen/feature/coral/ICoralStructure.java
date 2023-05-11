package git.jbredwards.subaquatic.mod.common.world.gen.feature.coral;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
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
    void generate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull ICoralBlockSupplier coralBlock);

    /**
     * Places a coral block and surrounds it with random coral fans/fins
     */
    default boolean placeCoralBlock(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull ICoralBlockSupplier coralBlock) {
        if(canPlaceCoralAt(world.getBlockState(pos), coralBlock)) {
            final IBlockState up = world.getBlockState(pos.up());
            if(canPlaceCoralAt(up, coralBlock)) { //don't allow coral to generate above water
                //place coral fin
                if(rand.nextFloat() < 0.2) {
                    final ICoralBlockSupplier coralFin = rand.nextFloat() < 0.2
                            ? GeneratorCoral.CORAL_FANS.get(rand.nextInt(GeneratorCoral.CORAL_FANS.size()))
                            : GeneratorCoral.CORAL_FINS.get(rand.nextInt(GeneratorCoral.CORAL_FINS.size()));

                    if(canPlaceCoralAt(up, coralFin)) world.setBlockState(pos.up(), coralFin.withDirection(EnumFacing.UP), 2);
                }

                //place coral fans
                for(EnumFacing facing : EnumFacing.HORIZONTALS) {
                    if(rand.nextFloat() < 0.2) {
                        final ICoralBlockSupplier coralFan = GeneratorCoral.CORAL_FANS.get(rand.nextInt(GeneratorCoral.CORAL_FANS.size()));
                        final BlockPos offset = pos.offset(facing);

                        if(canPlaceCoralAt(world.getBlockState(offset), coralFan)) world.setBlockState(offset, coralFan.withDirection(facing), 2);
                    }
                }

                world.setBlockState(pos, coralBlock.getBlock().getDefaultState(), 2);
                return true;
            }
        }

        return false;
    }

    default boolean canPlaceCoralAt(@Nonnull IBlockState here, @Nonnull ICoralBlockSupplier coral) {
        return FluidloggedUtils.isCompatibleFluid(coral.getNeededFluid(), FluidloggedUtils.getFluidFromState(here));
    }
}
