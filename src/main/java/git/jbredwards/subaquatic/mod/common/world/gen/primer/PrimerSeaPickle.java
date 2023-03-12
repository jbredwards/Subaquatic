package git.jbredwards.subaquatic.mod.common.world.gen.primer;

import git.jbredwards.fluidlogged_api.api.util.FluidState;
import git.jbredwards.subaquatic.api.event.OnCreateChunkFromPrimerEvent;
import git.jbredwards.subaquatic.mod.common.block.BlockSeaPickle;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import git.jbredwards.subaquatic.mod.common.world.biome.BiomeWarmOcean;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public final class PrimerSeaPickle
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void generate(@Nonnull OnCreateChunkFromPrimerEvent event) {
        if(isValidBiome(event.getBiome(7, 7))) {
            for(int i = 0; i < 20; i++) {
                if(event.rand.nextFloat() < 0.0625) {
                    final int x = event.rand.nextInt(16);
                    final int z = event.rand.nextInt(16);
                    if(isValidBiome(event.getBiome(x, z))) {
                        final BlockPos pos = event.getPos(x, event.findTopSolidNonLiquidBlock(x, z), z);
                        final FluidState fluidState = event.getFluidState(pos);
                        if(!fluidState.isEmpty() && fluidState.getMaterial() == Material.WATER && SubaquaticBlocks.SEA_PICKLE.canSustainBush(event.getBlockState(pos.down()))) {
                            event.setBlockState(pos, SubaquaticBlocks.SEA_PICKLE.withAmount(SubaquaticBlocks.SEA_PICKLE.getDefaultState().withProperty(BlockSeaPickle.GLOWING, true), event.rand.nextInt(4)));
                            event.setFluidState(pos, FluidState.of(fluidState.getFluid()));
                        }
                    }
                }
            }
        }
    }

    static boolean isValidBiome(@Nonnull Biome biome) { return biome instanceof BiomeWarmOcean; }
}
