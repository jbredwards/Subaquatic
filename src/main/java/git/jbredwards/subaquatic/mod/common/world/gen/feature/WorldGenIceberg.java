package git.jbredwards.subaquatic.mod.common.world.gen.feature;

import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Modified mojang iceberg code to work with WorldGenerator
 * @author jbred
 *
 */
public class WorldGenIceberg extends WorldGenerator
{
    @Nonnull
    protected final IBlockState state;
    public WorldGenIceberg(@Nonnull IBlockState stateIn) { state = stateIn; }
    
    @Override
    public boolean generate(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos position) {
        position = new BlockPos(position.getX(), worldIn.getSeaLevel(), position.getZ());
        boolean flag = rand.nextDouble() > 0.7D;
        IBlockState iblockstate = state;
        double d0 = rand.nextDouble() * 2.0D * Math.PI;
        int i = 11 - rand.nextInt(5);
        int j = 3 + rand.nextInt(3);
        boolean flag1 = rand.nextDouble() > 0.7D;
        int l = flag1 ? rand.nextInt(6) + 6 : rand.nextInt(15) + 3;
        if (!flag1 && rand.nextDouble() > 0.9D) {
            l += rand.nextInt(19) + 7;
        }

        int i1 = Math.min(l + rand.nextInt(11), 18);
        int j1 = Math.min(l + rand.nextInt(7) - rand.nextInt(5), 11);
        int k1 = flag1 ? i : 11;

        for(int l1 = -k1; l1 < k1; ++l1) {
            for(int i2 = -k1; i2 < k1; ++i2) {
                for(int j2 = 0; j2 < l; ++j2) {
                    int k2 = flag1 ? this.heightDependentRadiusEllipse(j2, l, j1) : this.heightDependentRadiusRound(rand, j2, l, j1);
                    if (flag1 || l1 < k2) {
                        this.generateIcebergBlock(worldIn, rand, position, l, l1, j2, i2, k2, k1, flag1, j, d0, flag, iblockstate);
                    }
                }
            }
        }

        this.smooth(worldIn, position, j1, l, flag1, i);

        for(int i3 = -k1; i3 < k1; ++i3) {
            for(int j3 = -k1; j3 < k1; ++j3) {
                for(int k3 = -1; k3 > -i1; --k3) {
                    int l3 = flag1 ? MathHelper.ceil((float)k1 * (1.0F - (float)Math.pow(k3, 2.0D) / ((float)i1 * 8.0F))) : k1;
                    int l2 = this.heightDependentRadiusSteep(rand, -k3, i1, j1);
                    if (i3 < l2) {
                        this.generateIcebergBlock(worldIn, rand, position, i1, i3, k3, j3, l2, l3, flag1, j, d0, flag, iblockstate);
                    }
                }
            }
        }

        boolean flag2 = flag1 ? rand.nextDouble() > 0.1D : rand.nextDouble() > 0.7D;
        if (flag2) {
            this.generateCutOut(rand, worldIn, j1, l, position, flag1, i, d0, j);
        }

        return true;
    }

    private void generateCutOut(@Nonnull Random rand, @Nonnull World world, int p_205184_3_, int p_205184_4_, @Nonnull BlockPos pos, boolean p_205184_6_, int p_205184_7_, double p_205184_8_, int p_205184_10_) {
        int i = rand.nextBoolean() ? -1 : 1;
        int j = rand.nextBoolean() ? -1 : 1;
        int k = rand.nextInt(Math.max(p_205184_3_ / 2 - 2, 1));
        if (rand.nextBoolean()) {
            k = p_205184_3_ / 2 + 1 - rand.nextInt(Math.max(p_205184_3_ - p_205184_3_ / 2 - 1, 1));
        }

        int l = rand.nextInt(Math.max(p_205184_3_ / 2 - 2, 1));
        if (rand.nextBoolean()) {
            l = p_205184_3_ / 2 + 1 - rand.nextInt(Math.max(p_205184_3_ - p_205184_3_ / 2 - 1, 1));
        }

        if (p_205184_6_) {
            k = l = rand.nextInt(Math.max(p_205184_7_ - 5, 1));
        }

        BlockPos blockpos = new BlockPos(i * k, 0, j * l);
        double d0 = p_205184_6_ ? p_205184_8_ + (Math.PI / 2D) : rand.nextDouble() * 2.0D * Math.PI;

        for(int i1 = 0; i1 < p_205184_4_ - 3; ++i1) {
            int j1 = this.heightDependentRadiusRound(rand, i1, p_205184_4_, p_205184_3_);
            this.carve(j1, i1, pos, world, false, d0, blockpos, p_205184_7_, p_205184_10_);
        }

        for(int k1 = -1; k1 > -p_205184_4_ + rand.nextInt(5); --k1) {
            int l1 = this.heightDependentRadiusSteep(rand, -k1, p_205184_4_, p_205184_3_);
            this.carve(l1, k1, pos, world, true, d0, blockpos, p_205184_7_, p_205184_10_);
        }

    }

    private void carve(int p_205174_1_, int p_205174_2_, BlockPos p_205174_3_, World p_205174_4_, boolean p_205174_5_, double p_205174_6_, BlockPos p_205174_8_, int p_205174_9_, int p_205174_10_) {
        int i = p_205174_1_ + 1 + p_205174_9_ / 3;
        int j = Math.min(p_205174_1_ - 3, 3) + p_205174_10_ / 2 - 1;

        for(int k = -i; k < i; ++k) {
            for(int l = -i; l < i; ++l) {
                double d0 = this.signedDistanceEllipse(k, l, p_205174_8_, i, j, p_205174_6_);
                if (d0 < 0.0D) {
                    BlockPos blockpos = p_205174_3_.add(k, p_205174_2_, l);
                    Block block = p_205174_4_.getBlockState(blockpos).getBlock();
                    if (this.isBlockIceberg(block)) {
                        if (p_205174_5_) {
                            setBlockAndNotifyAdequately(p_205174_4_, blockpos, Blocks.WATER.getDefaultState());
                        } else {
                            setBlockAndNotifyAdequately(p_205174_4_, blockpos, Blocks.AIR.getDefaultState());
                            this.removeFloatingSnowLayer(p_205174_4_, blockpos);
                        }
                    }
                }
            }
        }

    }

    private void removeFloatingSnowLayer(World p_205185_1_, BlockPos p_205185_2_) {
        if (p_205185_1_.getBlockState(p_205185_2_.up()).getBlock() == Blocks.SNOW) {
            setBlockAndNotifyAdequately(p_205185_1_, p_205185_2_.up(), Blocks.AIR.getDefaultState());
        }

    }

    private void generateIcebergBlock(World p_205181_1_, Random p_205181_2_, BlockPos p_205181_3_, int p_205181_4_, int p_205181_5_, int p_205181_6_, int p_205181_7_, int p_205181_8_, int p_205181_9_, boolean p_205181_10_, int p_205181_11_, double p_205181_12_, boolean p_205181_14_, IBlockState p_205181_15_) {
        double d0 = p_205181_10_ ? this.signedDistanceEllipse(p_205181_5_, p_205181_7_, BlockPos.ORIGIN, p_205181_9_, this.getEllipseC(p_205181_6_, p_205181_4_, p_205181_11_), p_205181_12_) : this.signedDistanceCircle(p_205181_5_, p_205181_7_, BlockPos.ORIGIN, p_205181_8_, p_205181_2_);
        if (d0 < 0.0D) {
            BlockPos blockpos1 = p_205181_3_.add(p_205181_5_, p_205181_6_, p_205181_7_);
            double d1 = p_205181_10_ ? -0.5D : (double)(-6 - p_205181_2_.nextInt(3));
            if (d0 > d1 && p_205181_2_.nextDouble() > 0.9D) {
                return;
            }

            this.setIcebergBlock(blockpos1, p_205181_1_, p_205181_2_, p_205181_4_ - p_205181_6_, p_205181_4_, p_205181_10_, p_205181_14_, p_205181_15_);
        }

    }

    private void setIcebergBlock(BlockPos p_205175_1_, World p_205175_2_, Random p_205175_3_, int p_205175_4_, int p_205175_5_, boolean p_205175_6_, boolean p_205175_7_, IBlockState p_205175_8_) {
        IBlockState iblockstate = p_205175_2_.getBlockState(p_205175_1_);
        Block block = iblockstate.getBlock();
        if (iblockstate.getMaterial() == Material.AIR || block == Blocks.SNOW || block == Blocks.ICE || iblockstate.getMaterial() == Material.WATER) {
            boolean flag = !p_205175_6_ || p_205175_3_.nextDouble() > 0.05D;
            int i = p_205175_6_ ? 3 : 2;
            if (p_205175_7_ && iblockstate.getMaterial() != Material.WATER && (double)p_205175_4_ <= (double)p_205175_3_.nextInt(Math.max(1, p_205175_5_ / i)) + (double)p_205175_5_ * 0.6D && flag) {
                setBlockAndNotifyAdequately(p_205175_2_, p_205175_1_, Blocks.SNOW.getDefaultState());
            } else {
                setBlockAndNotifyAdequately(p_205175_2_, p_205175_1_, p_205175_8_);
            }
        }

    }

    private int getEllipseC(int p_205176_1_, int p_205176_2_, int p_205176_3_) {
        int i = p_205176_3_;
        if (p_205176_1_ > 0 && p_205176_2_ - p_205176_1_ <= 3) {
            i = p_205176_3_ - (4 - (p_205176_2_ - p_205176_1_));
        }

        return i;
    }

    private double signedDistanceCircle(int p_205177_1_, int p_205177_2_, BlockPos p_205177_3_, int p_205177_4_, Random p_205177_5_) {
        float f = 10.0F * MathHelper.clamp(p_205177_5_.nextFloat(), 0.2F, 0.8F) / (float)p_205177_4_;
        return (double)f + Math.pow(p_205177_1_ - p_205177_3_.getX(), 2.0D) + Math.pow(p_205177_2_ - p_205177_3_.getZ(), 2.0D) - Math.pow(p_205177_4_, 2.0D);
    }

    private double signedDistanceEllipse(int p_205180_1_, int p_205180_2_, BlockPos p_205180_3_, int p_205180_4_, int p_205180_5_, double p_205180_6_) {
        return Math.pow(((double)(p_205180_1_ - p_205180_3_.getX()) * Math.cos(p_205180_6_) - (double)(p_205180_2_ - p_205180_3_.getZ()) * Math.sin(p_205180_6_)) / (double)p_205180_4_, 2.0D) + Math.pow(((double)(p_205180_1_ - p_205180_3_.getX()) * Math.sin(p_205180_6_) + (double)(p_205180_2_ - p_205180_3_.getZ()) * Math.cos(p_205180_6_)) / (double)p_205180_5_, 2.0D) - 1.0D;
    }

    private int heightDependentRadiusRound(Random p_205183_1_, int p_205183_2_, int p_205183_3_, int p_205183_4_) {
        float f = 3.5F - p_205183_1_.nextFloat();
        float f1 = (1.0F - (float)Math.pow(p_205183_2_, 2.0D) / ((float)p_205183_3_ * f)) * (float)p_205183_4_;
        if (p_205183_3_ > 15 + p_205183_1_.nextInt(5)) {
            int i = p_205183_2_ < 3 + p_205183_1_.nextInt(6) ? p_205183_2_ / 2 : p_205183_2_;
            f1 = (1.0F - (float)i / ((float)p_205183_3_ * f * 0.4F)) * (float)p_205183_4_;
        }

        return MathHelper.ceil(f1 / 2.0F);
    }

    private int heightDependentRadiusEllipse(int p_205178_1_, int p_205178_2_, int p_205178_3_) {
        float f1 = (1.0F - (float)Math.pow(p_205178_1_, 2.0D) / ((float) p_205178_2_)) * (float)p_205178_3_;
        return MathHelper.ceil(f1 / 2.0F);
    }

    private int heightDependentRadiusSteep(Random p_205187_1_, int p_205187_2_, int p_205187_3_, int p_205187_4_) {
        float f = 1.0F + p_205187_1_.nextFloat() / 2.0F;
        float f1 = (1.0F - (float)p_205187_2_ / ((float)p_205187_3_ * f)) * (float)p_205187_4_;
        return MathHelper.ceil(f1 / 2.0F);
    }

    private boolean isBlockIceberg(@Nonnull Block block) {
        return block == Blocks.PACKED_ICE || block == Blocks.SNOW || block == SubaquaticBlocks.BLUE_ICE;
    }

    private boolean isDownAir(@Nonnull World world, @Nonnull BlockPos pos) {
        return world.getBlockState(pos.down()).getMaterial() == Material.AIR;
    }

    private void smooth(World p_205186_1_, BlockPos p_205186_2_, int p_205186_3_, int p_205186_4_, boolean p_205186_5_, int p_205186_6_) {
        int i = p_205186_5_ ? p_205186_6_ : p_205186_3_ / 2;

        for(int j = -i; j <= i; ++j) {
            for(int k = -i; k <= i; ++k) {
                for(int l = 0; l <= p_205186_4_; ++l) {
                    BlockPos blockpos = p_205186_2_.add(j, l, k);
                    Block block = p_205186_1_.getBlockState(blockpos).getBlock();
                    if (this.isBlockIceberg(block)) {
                        if (this.isDownAir(p_205186_1_, blockpos)) {
                            setBlockAndNotifyAdequately(p_205186_1_, blockpos, Blocks.AIR.getDefaultState());
                            setBlockAndNotifyAdequately(p_205186_1_, blockpos.up(), Blocks.AIR.getDefaultState());
                        } else if (this.isBlockIceberg(block)) {
                            Block[] ablock = new Block[]{p_205186_1_.getBlockState(blockpos.west()).getBlock(), p_205186_1_.getBlockState(blockpos.east()).getBlock(), p_205186_1_.getBlockState(blockpos.north()).getBlock(), p_205186_1_.getBlockState(blockpos.south()).getBlock()};
                            int i1 = 0;

                            for(Block block1 : ablock) {
                                if (!this.isBlockIceberg(block1)) {
                                    ++i1;
                                }
                            }

                            if (i1 >= 3) {
                                setBlockAndNotifyAdequately(p_205186_1_, blockpos, Blocks.AIR.getDefaultState());
                            }
                        }
                    }
                }
            }
        }
    }
}
