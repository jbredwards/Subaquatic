package git.jbredwards.subaquatic.mod.common.world.gen.handler;

import git.jbredwards.subaquatic.mod.Subaquatic;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraft.world.gen.NoiseGeneratorSimplex;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.Mod;
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
public final class EndLakesHandler
{
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    static void genLakes(@Nonnull PopulateChunkEvent.Pre event) {
        if(event.getGenerator() instanceof ChunkGeneratorEnd) {
            final int chunkX = event.getChunkX();
            final int chunkZ = event.getChunkZ();

            //don't generate lakes on the main end island
            if(chunkX * chunkX + chunkZ * chunkZ > 2048) {
                final ChunkGeneratorEnd generator = (ChunkGeneratorEnd)event.getGenerator();
                final Random rand = event.getRand();
                final int originX = chunkX << 4 | 8;
                final int originZ = chunkZ << 4 | 8;

                //small lake gen
                if(rand.nextFloat() < 0.2) {
                    final BlockPos pos = new BlockPos(originX + rand.nextInt(16), 70, originZ + rand.nextInt(16));
                    new WorldGenLakes(Blocks.WATER).generate(event.getWorld(), rand, pos);
                }

                //large lake gen
                final double[][] endIslands = getIslandValues(generator.islandNoise, chunkX, chunkZ);
                for(int offsetX = 0; offsetX < 16; offsetX++) {
                    for(int offsetZ = 0; offsetZ < 16; offsetZ++) {
                        final int x = originX + offsetX;
                        final int z = originZ + offsetZ;

                        final double noiseVal = Biome.GRASS_COLOR_NOISE.getValue(x / 80.0, z / 80.0) * 20;
                        event.getWorld().setBlockState(new BlockPos(x, (int)noiseVal, z), Blocks.WOOL.getDefaultState(), 2);

                        final double islandVal = endIslands[offsetX][offsetZ] * 5;
                        event.getWorld().setBlockState(new BlockPos(x, (int)islandVal, z), Blocks.STAINED_GLASS.getDefaultState(), 2);

                        //final double islandValTest = generator.getIslandHeightValue(chunkX, chunkZ, 1, 1) * 5;
                        //event.getWorld().setBlockState(new BlockPos(x, (int)islandValTest, z), Blocks.NETHERRACK.getDefaultState(), 2);
                    }
                }
            }
        }
    }

    static double[][] getIslandValues(@Nonnull NoiseGeneratorSimplex islandNoise, int chunkX, int chunkZ) {
        final double[][] ret = new double[16][16];

        
        final int blendSize = 16;


    }

    //calculates island height values
    //static double getIslandVal(@Nonnull NoiseGeneratorSimplex islandNoise, int posX, int posZ) {

        /*final double chunkX = posX / 16.0;
        final double chunkZ = posZ / 16.0;

        double chunkOffsetX = chunkX * 2 + 1;
        double chunkOffsetZ = chunkZ * 2 + 1;
        double ret = 100 - Math.sqrt(chunkOffsetX * chunkOffsetX + chunkOffsetZ * chunkOffsetZ) * 8;

        if(ret > 80) ret = 80;
        else if(ret < -100) ret = -100;

        for(int x = -12; x <= 12; ++x) {
            for(int z = -12; z <= 12; ++z) {
                final double k = chunkX + x;
                final double l = chunkZ + z;

                if(islandNoise.getValue(k, l) < -0.9) {
                    double f3 = (Math.abs(k) * 3439 + Math.abs(l) * 147) % 13.0 + 9;

                    chunkOffsetX = 1 - x * 2;
                    chunkOffsetZ = 1 - z * 2;
                    double newRet = 100 - Math.sqrt(chunkOffsetX * chunkOffsetX + chunkOffsetZ * chunkOffsetZ) * f3;

                    if(newRet > 80) newRet = 80;
                    else if(newRet < -100) newRet = -100;

                    if(newRet > ret) ret = newRet;
                }
            }
        }

        return ret;*/
    //}
}
