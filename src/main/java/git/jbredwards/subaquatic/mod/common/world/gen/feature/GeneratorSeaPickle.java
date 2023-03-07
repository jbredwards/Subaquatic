package git.jbredwards.subaquatic.mod.common.world.gen.feature;

import git.jbredwards.subaquatic.mod.common.block.BlockSeaPickle;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import git.jbredwards.subaquatic.mod.common.world.biome.BiomeWarmOcean;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public enum GeneratorSeaPickle implements IWorldGenerator
{
    INSTANCE;

    @Override
    public void generate(@Nonnull Random random, int chunkX, int chunkZ, @Nonnull World world, @Nonnull IChunkGenerator chunkGenerator, @Nonnull IChunkProvider chunkProvider) {
        final int originX = chunkX << 4 | 8;
        final int originZ = chunkZ << 4 | 8;

        if(isValidBiome(world.getBiome(new BlockPos(originX, 0, originZ)))) {
            for(int i = 0; i < 20; i++) {
                if(random.nextFloat() < 0.0625) {
                    final BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(originX + random.nextInt(16), 0, originZ + random.nextInt(16)));
                    if(isValidBiome(world.getBiome(pos)) && SubaquaticBlocks.SEA_PICKLE.canPlaceBlockAt(world, pos) && world.getBlockState(pos).getMaterial() == Material.WATER) {
                        world.setBlockState(pos, SubaquaticBlocks.SEA_PICKLE.withAmount(SubaquaticBlocks.SEA_PICKLE.getDefaultState().withProperty(BlockSeaPickle.GLOWING, true), random.nextInt(4)));
                    }
                }
            }
        }
    }

    static boolean isValidBiome(@Nonnull Biome biome) { return biome instanceof BiomeWarmOcean; }
}
