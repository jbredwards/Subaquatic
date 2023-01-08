package git.jbredwards.subaquatic.mod.common.world.biome;

import git.jbredwards.subaquatic.api.biome.BiomeSubaquaticOcean;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBiomes;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
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
import javax.annotation.Nullable;
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
    protected long prevSeed;

    public BiomeFrozenOcean(@Nonnull BiomeProperties propertiesIn) { this(null, propertiesIn); }
    public BiomeFrozenOcean(@Nullable Biome deepOceanBiomeIn, @Nonnull BiomeProperties propertiesIn) {
        super(deepOceanBiomeIn, propertiesIn);
        spawnableCreatureList.add(new SpawnListEntry(EntityPolarBear.class, 1, 1, 2));
    }

    @Nonnull
    @Override
    public Biome getMixOceanBiome() { return SubaquaticBiomes.COLD_OCEAN; }

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
                    new WorldGenIceberg(SubaquaticBlocks.BLUE_ICE.getDefaultState()).generate(worldIn, rand, pos);
                }
                //blue ice ore gen
                genStandardOre1(worldIn, rand, 20, new WorldGenBlueIce(), 30, 64);
                super.genDecorations(biomeIn, worldIn, rand);
            }
        });
    }

    @Override
    public void genTerrainBlocks(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull ChunkPrimer chunkPrimerIn, int posX, int posZ, double noiseVal) {
        setSeed(worldIn.getSeed());
        buildSurface(worldIn, rand, chunkPrimerIn, posX, posZ, noiseVal);
    }

    //seed to test -955387458787438796
    protected void buildSurface(@Nonnull World world, @Nonnull Random rand, @Nonnull ChunkPrimer primer, int posX, int posZ, double noiseVal) {
        final int seaLevel = world.getSeaLevel();
        double maxHeight = 0;
        double minHeight = 0; //usually underwater

        final int xNoiseIn = (posX & -16) + (posZ & 15);
        final int zNoiseIn = (posZ & -16) + (posX & 15);

        double d2 = Math.min(Math.abs(noiseVal), perlin1.getValue(xNoiseIn * 0.1, zNoiseIn * 0.1));
        if(d2 > 1.8) {
            final double noiseScale = 0.09765625;
            double d4 = Math.abs(perlin2.getValue(xNoiseIn * noiseScale, zNoiseIn * noiseScale));

            maxHeight = d2 * d2 * 1.2;
            double d5 = Math.ceil(d4 * 40) + 14;
            if(maxHeight > d5) {
                maxHeight = d5;
            }

            if(getTemperature(new BlockPos(posX, 63, posZ)) > 0.1) maxHeight -= 2;
            if(maxHeight > 2) {
                minHeight = seaLevel - maxHeight - 7;
                maxHeight = maxHeight + seaLevel;
            }

            else maxHeight = 0;
        }

        //I hate that these are mixed, but vanilla's are too, soo...
        final int x = posZ & 15;
        final int z = posX & 15;
        
        IBlockState filler = fillerBlock;
        IBlockState top = topBlock;
        
        final int oceanSurfaceY = (int)(noiseVal / 3 + 3 + rand.nextDouble() * 0.25);
        int j = -1;
        int k = 0;
        int l = 2 + rand.nextInt(4);
        final int snowMinY = seaLevel + 8 + rand.nextInt(10);

        for(int posY = 255; posY >= 0; --posY) {
            //bedrock gen
            if(posY <= rand.nextInt(5)) primer.setBlockState(x, posY, z, BEDROCK);
            else {
                //generates most of the (above sea level) packed ice
                if(primer.getBlockState(x, posY, z).getBlock() == Blocks.AIR && posY < (int)maxHeight && rand.nextDouble() > 0.01)
                    primer.setBlockState(x, posY, z, Blocks.PACKED_ICE.getDefaultState());

                //generates most of the (under sea level) packed ice
                else if(primer.getBlockState(x, posY, z).getMaterial() == Material.WATER && posY > (int)minHeight && posY < seaLevel && minHeight != 0 && rand.nextDouble() > 0.15)
                    primer.setBlockState(x, posY, z, Blocks.PACKED_ICE.getDefaultState());


                final IBlockState here = primer.getBlockState(x, posY, z);
                if(here.getBlock() == Blocks.AIR) j = -1;
                else if(here.getBlock() != STONE.getBlock()) {
                    if(here.getBlock() == Blocks.PACKED_ICE && k <= l && posY > snowMinY) {
                        primer.setBlockState(x, posY, z, Blocks.SNOW.getDefaultState());
                        ++k;
                    }
                }
                else if(j == -1) {
                    if(oceanSurfaceY <= 0) {
                        top = AIR;
                        filler = STONE;
                    }
                    else if(posY >= seaLevel - 4 && posY <= seaLevel + 1) {
                        top = topBlock;
                        filler = fillerBlock;
                    }

                    //random ice gen
                    if(posY < seaLevel && (top.getBlock() == Blocks.AIR))
                        top = getTemperature(new BlockPos(posX, posY, posZ)) < 0.15F ? ICE : WATER;

                    j = oceanSurfaceY;

                    if(posY >= seaLevel - 1) primer.setBlockState(x, posY, z, top);
                    else if(posY < seaLevel - 7 - oceanSurfaceY) {
                        top = AIR;
                        filler = STONE;
                        primer.setBlockState(x, posY, z, GRAVEL);
                    }
                    else primer.setBlockState(x, posY, z, filler);
                }
                else if(j > 0) {
                    --j;
                    primer.setBlockState(x, posY, z, filler);
                }
            }
        }
    }

    protected void setSeed(long seed) {
        if(seed != prevSeed || perlin1 == null || perlin2 == null) {
            final Random rand = new Random(seed);
            perlin1 = new NoiseGeneratorPerlin(rand, 4);
            perlin2 = new NoiseGeneratorPerlin(rand, 1);
            prevSeed = seed;
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
