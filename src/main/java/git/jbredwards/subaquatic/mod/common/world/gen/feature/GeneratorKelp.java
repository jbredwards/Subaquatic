package git.jbredwards.subaquatic.mod.common.world.gen.feature;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.mod.common.block.BlockKelp;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler.Server.World.Kelp;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import git.jbredwards.subaquatic.mod.common.world.gen.IConfigurableWorldGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public enum GeneratorKelp implements IConfigurableWorldGenerator
{
    INSTANCE;

    @Override
    public void generate(@Nonnull Random random, int chunkX, int chunkZ, @Nonnull World world, @Nonnull IChunkGenerator chunkGenerator, @Nonnull IChunkProvider chunkProvider) {
        if(Kelp.enabled && isDimensionValid(world, Kelp.dimensions)) {
            final int originX = chunkX << 4 | 8;
            final int originZ = chunkZ << 4 | 8;

            final double noiseVal = Biome.GRASS_COLOR_NOISE.getValue(originX / 80.0, originZ / 80.0);
            final int count = (int)Math.ceil(noiseVal * getMaxForBiome(world, originX, originZ, Kelp.PER_BIOME_RARITY, Kelp.defaultAmount));

            for(int i = 0; i < count; i++) {
                BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(originX + random.nextInt(16), 0, originZ + random.nextInt(16)));
                if(world.getBlockState(pos.down()).isTopSolid() && SubaquaticBlocks.KELP.canPlaceBlockAt(world, pos)) {
                    final int height = MathHelper.getInt(random, Kelp.minHeight, Kelp.maxHeight);
                    for(int y = 0; y < height; y++) {
                        final Fluid fluidHere = FluidloggedUtils.getFluidFromState(world.getBlockState(pos));
                        if(fluidHere != null && SubaquaticBlocks.KELP.isFluidValid(SubaquaticBlocks.KELP.getDefaultState(), world, pos, fluidHere)) {
                            world.setBlockState(pos, SubaquaticBlocks.KELP.getDefaultState().withProperty(BlockKelp.AGE, random.nextInt(16)), 2);
                            pos = pos.up();
                        }

                        else break;
                    }
                }
            }
        }
    }
}
