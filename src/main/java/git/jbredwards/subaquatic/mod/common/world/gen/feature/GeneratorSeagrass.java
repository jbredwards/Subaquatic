package git.jbredwards.subaquatic.mod.common.world.gen.feature;

import git.jbredwards.subaquatic.api.biome.IOceanBiome;
import git.jbredwards.subaquatic.mod.common.block.BlockSeagrass;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBiomes;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import git.jbredwards.subaquatic.mod.common.world.biome.BiomeFrozenOcean;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeOcean;
import net.minecraft.world.biome.BiomeRiver;
import net.minecraft.world.biome.BiomeSwamp;
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
public enum GeneratorSeagrass implements IWorldGenerator
{
    INSTANCE;

    @Override
    public void generate(@Nonnull Random random, int chunkX, int chunkZ, @Nonnull World world, @Nonnull IChunkGenerator chunkGenerator, @Nonnull IChunkProvider chunkProvider) {
        final int originX = chunkX << 4 | 8;
        final int originZ = chunkZ << 4 | 8;
        final int amountForGen = getAmountForGen(world, originX, originZ);

        for(int i = 0; i < amountForGen; i++) {
            final int offsetX = random.nextInt(16);
            final int offsetZ = random.nextInt(16);

            final BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(originX + offsetX, 0, originZ + offsetZ));
            final Biome biome = world.getBiome(pos);

            if(!biome.isSnowyBiome() && world.getBlockState(pos.down()).isTopSolid() && SubaquaticBlocks.SEAGRASS.canPlaceBlockAt(world, pos)) {
                world.setBlockState(pos, SubaquaticBlocks.SEAGRASS.getDefaultState(), 2);
                //try growing a double tall plant
                if(random.nextFloat() < 0.3 && SubaquaticBlocks.SEAGRASS.canPlaceBlockAt(world, pos.up()))
                    world.setBlockState(pos.up(), SubaquaticBlocks.SEAGRASS.getDefaultState().withProperty(BlockSeagrass.TYPE, BlockSeagrass.Type.TOP), 2);
            }
        }
    }

    static int getAmountForGen(@Nonnull World world, int originX, int originZ) {
        final Biome biome = world.getBiome(new BlockPos(originX, world.getSeaLevel(), originZ));
        if(biome instanceof BiomeRiver || biome.getBiomeClass() == BiomeOcean.class) return 48;
        else if(biome instanceof BiomeSwamp) return 64;
        else if(biome instanceof BiomeFrozenOcean) return 8;
        else if(biome == SubaquaticBiomes.COLD_OCEAN) return 32;
        else if(biome == SubaquaticBiomes.DEEP_COLD_OCEAN) return 40;
        return biome instanceof IOceanBiome ? 80 : 32;
    }
}
