package git.jbredwards.subaquatic.mod.common.world.gen.handler;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.ChunkGeneratorEnd;
import net.minecraftforge.event.terraingen.ChunkGeneratorEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@Mod.EventBusSubscriber(modid = Subaquatic.MODID)
public final class EndLakesHandler
{
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    static void genLargeLakes(@Nonnull ChunkGeneratorEvent.ReplaceBiomeBlocks event) {
        if(event.getGenerator() instanceof ChunkGeneratorEnd) {
            final int chunkX = event.getX();
            final int chunkZ = event.getZ();

            //don't generate lakes on the main end island
            if(chunkX * chunkX + chunkZ * chunkZ > 2048) {
                final ChunkGeneratorEnd generator = (ChunkGeneratorEnd)event.getGenerator();
                final ChunkPrimer primer = event.getPrimer();

                final double[][] endIslands = getIslandValues(generator, chunkX, chunkZ);
                final int originX = chunkX << 4;
                final int originZ = chunkZ << 4;

                for(int offsetX = 0; offsetX < 16; offsetX++) {
                    for(int offsetZ = 0; offsetZ < 16; offsetZ++) {
                        final double lakeNoise = Biome.GRASS_COLOR_NOISE.getValue((originX + offsetX) * 0.0125, (originZ + offsetZ) * 0.0125);
                        if(lakeNoise > 0) {
                            final double islandVal = endIslands[offsetX][offsetZ];
                            if(islandVal > 0) {
                                final int originY = 67;

                                //beach
                                final int offsetY = originY - (int)(Math.sqrt(lakeNoise) * islandVal * 0.25);
                                final int sandHeight = 56 + generator.rand.nextInt(4);
                                for(int y = sandHeight; y > offsetY; y--) {
                                    if(primer.getBlockState(offsetX, y, offsetZ).getMaterial() != Material.AIR)
                                        primer.setBlockState(offsetX, y, offsetZ, SubaquaticBlocks.END_SAND.getDefaultState());
                                }

                                //lake
                                for(int y = originY; y > offsetY + 1; y--) {
                                    if(y > 56) primer.setBlockState(offsetX, y, offsetZ, Blocks.AIR.getDefaultState());
                                    else primer.setBlockState(offsetX, y, offsetZ, Blocks.WATER.getDefaultState());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //any value outside an end island is below 0, the further from the edge on an island the larger the value
    static double[][] getIslandValues(@Nonnull ChunkGeneratorEnd generator, int chunkX, int chunkZ) {
        final double[][] islandChunkValues = new double[3][3];
        for(int x = -1; x <= 1; x++) {
            for(int z = -1; z <= 1; z++) {
                islandChunkValues[x + 1][z + 1] = generator.getIslandHeightValue(chunkX + x, chunkZ + z, 1, 1);
            }
        }

        //blend chunk values to make smooth transitions between chunks
        final double[][] ret = new double[16][16];
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                double sum = 0;
                for(int i = 0; i < 16; i++) {
                    for(int j = 0; j < 16; j++) {
                        sum += islandChunkValues[x + i >> 4][z + j >> 4];
                    }
                }

                ret[x][z] = sum / 256;
            }
        }

        return ret;
    }
}
