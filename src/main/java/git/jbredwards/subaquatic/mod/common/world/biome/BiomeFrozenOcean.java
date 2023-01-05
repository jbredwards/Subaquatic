package git.jbredwards.subaquatic.mod.common.world.biome;

import git.jbredwards.subaquatic.api.biome.BiomeSubaquaticOcean;
import git.jbredwards.subaquatic.mod.common.init.ModBlocks;
import git.jbredwards.subaquatic.mod.common.world.gen.feature.WorldGenBlueIce;
import git.jbredwards.subaquatic.mod.common.world.gen.feature.WorldGenIceberg;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorPerlin;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public class BiomeFrozenOcean extends BiomeSubaquaticOcean
{
    @Nonnull
    protected static final NoiseGeneratorPerlin NOISE = new NoiseGeneratorPerlin(new Random(3456), 3);
    protected NoiseGeneratorPerlin perlin1, perlin2;

    public BiomeFrozenOcean(@Nonnull BiomeProperties propertiesIn) {
        super(propertiesIn);
        spawnableCreatureList.add(new SpawnListEntry(EntityPolarBear.class, 1, 1, 2));
    }

    @Nonnull
    @Override
    public BiomeDecorator createBiomeDecorator() {
        return getModdedBiomeDecorator(new BiomeDecorator() {
            @Override
            protected void genDecorations(@Nonnull Biome biomeIn, @Nonnull World worldIn, @Nonnull Random rand) {
                //normal icebergs
                if(rand.nextFloat() < 0.0625) {
                    final int offsetX = rand.nextInt(8) + 12;
                    final int offsetZ = rand.nextInt(8) + 12;
                    final BlockPos pos = worldIn.getHeight(chunkPos.add(offsetX, 0, offsetZ));
                    new WorldGenIceberg(Blocks.PACKED_ICE.getDefaultState()).generate(worldIn, rand, pos);
                }
                //rare icebergs
                if(rand.nextFloat() < 0.005) {
                    final int offsetX = rand.nextInt(8) + 12;
                    final int offsetZ = rand.nextInt(8) + 12;
                    final BlockPos pos = worldIn.getHeight(chunkPos.add(offsetX, 0, offsetZ));
                    new WorldGenIceberg(ModBlocks.BLUE_ICE.getDefaultState()).generate(worldIn, rand, pos);
                }
                //blue ice ore gen
                genStandardOre1(worldIn, rand, 20, new WorldGenBlueIce(), 30, 64);
                super.genDecorations(biomeIn, worldIn, rand);
            }
        });
    }

    @Override
    public void genTerrainBlocks(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull ChunkPrimer chunkPrimerIn, int posX, int posZ, double noiseVal) {
        final NoiseGeneratorPerlin perlin1 = this.perlin1 != null ? this.perlin1 : (this.perlin1 = new NoiseGeneratorPerlin(rand, 4));

        double d0 = 0;
        double d1 = 0;

        final int seaLevel = worldIn.getSeaLevel();
        double d2 = Math.min(Math.abs(noiseVal), perlin1.getValue((double)posX * 0.1, (double)posZ * 0.1));

        if(d2 > 1.8) {
            final NoiseGeneratorPerlin perlin2 = this.perlin2 != null ? this.perlin2 : (this.perlin2 = new NoiseGeneratorPerlin(rand, 1));
            final float temperature = getTemperature(new BlockPos(posX, 63, posZ));
            final double noiseFactor = 0.09765625;
            double d4 = Math.abs(perlin2.getValue((double)posX * noiseFactor, (double)posZ * noiseFactor));
            d0 = d2 * d2 * 1.2;
            double d5 = Math.ceil(d4 * 40) + 14;
            if (d0 > d5) {
                d0 = d5;
            }

            if (temperature > 0.1) {
                d0 -= 2;
            }

            if (d0 > 2) {
                d1 = (double)seaLevel - d0 - 7;
                d0 = d0 + (double)seaLevel;
            } else {
                d0 = 0;
            }
        }

        int x = posX & 15;
        int z = posZ & 15;
        IBlockState filler = fillerBlock;
        IBlockState top = topBlock;
        int l1 = (int)(noiseVal / 3 + 3 + rand.nextDouble() * 0.25);
        int j = -1;
        int k = 0;
        int l = 2 + rand.nextInt(4);
        int i1 = seaLevel + 18 + rand.nextInt(10);

        for(int y = 255; y >= 0; --y) {
            if(y <= rand.nextInt(5)) {
                chunkPrimerIn.setBlockState(x, y, z, BEDROCK);
            }
            else {
                if(chunkPrimerIn.getBlockState(x, y, z).getMaterial() == Material.AIR && y < (int)d0 && rand.nextDouble() > 0.01) {
                    chunkPrimerIn.setBlockState(x, y, z, Blocks.PACKED_ICE.getDefaultState());
                }
                else if(chunkPrimerIn.getBlockState(x, y, z).getMaterial() == Material.WATER && y > (int)d1 && y < seaLevel && d1 != 0 && rand.nextDouble() > 0.15) {
                    chunkPrimerIn.setBlockState(x, y, z, Blocks.PACKED_ICE.getDefaultState());
                }

                IBlockState iblockstate1 = chunkPrimerIn.getBlockState(x, y, z);
                if(iblockstate1.getMaterial() == Material.AIR) {
                    j = -1;
                }
                else if (iblockstate1.getBlock() != Blocks.STONE) {
                    if(iblockstate1.getBlock() == Blocks.PACKED_ICE && k <= l && y > i1) {
                        chunkPrimerIn.setBlockState(x, y, z, Blocks.SNOW.getDefaultState());
                        ++k;
                    }
                }
                else if (j == -1) {
                    if (l1 <= 0) {
                        top = AIR;
                        filler = STONE;
                    } else if (y >= seaLevel - 4 && y <= seaLevel + 1) {
                        top = topBlock;
                        filler = fillerBlock;
                    }

                    if (y < seaLevel && (top == null || top.getMaterial() == Material.AIR)) {
                        if (getTemperature(new BlockPos(posX, y, posZ)) < 0.15) {
                            top = ICE;
                        } else {
                            top = WATER;
                        }
                    }

                    j = l1;
                    if (y >= seaLevel - 1) {
                        assert top != null;
                        chunkPrimerIn.setBlockState(x, y, z, top);
                    } else if (y < seaLevel - 7 - l1) {
                        top = AIR;
                        filler = STONE;
                        chunkPrimerIn.setBlockState(x, y, z, getOceanSurface());
                    } else {
                        chunkPrimerIn.setBlockState(x, y, z, filler);
                    }
                }
                else if (j > 0) {
                    --j;
                    chunkPrimerIn.setBlockState(x, y, z, filler);
                    if (j == 0 && filler.getBlock() == Blocks.SAND && l1 > 1) {
                        j = rand.nextInt(4) + Math.max(0, y - 63);
                        filler = iblockstate1.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? RED_SANDSTONE : SANDSTONE;
                    }
                }
            }
        }
    }

    @Override
    public float getTemperature(@Nonnull BlockPos pos) {
        float defaultTemp = getDefaultTemperature();
        final double noiseTemp = NOISE.getValue(pos.getX() * 0.05, pos.getZ() * 0.05);
        final double otherTemp = GRASS_COLOR_NOISE.getValue(pos.getX() * 0.2, pos.getZ() * 0.2);

        if(noiseTemp + otherTemp < 0.3 && GRASS_COLOR_NOISE.getValue(pos.getX() * 0.09, pos.getZ() * 0.09) < 0.8) {
            defaultTemp = 0.2f;
        }

        if(pos.getY() > 64) {
            final float temp = (float)(TEMPERATURE_NOISE.getValue(pos.getX() / 8.0, pos.getZ() / 8.0) * 4);
            return defaultTemp - (temp + pos.getY() - 64) * 0.05f / 30;
        }

        return defaultTemp;
    }
}
