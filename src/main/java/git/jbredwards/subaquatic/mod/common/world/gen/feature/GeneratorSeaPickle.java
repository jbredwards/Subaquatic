package git.jbredwards.subaquatic.mod.common.world.gen.feature;

import git.jbredwards.subaquatic.mod.common.block.BlockSeaPickle;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler.Server.World.SeaPickle;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import git.jbredwards.subaquatic.mod.common.world.gen.IConfigurableWorldGenerator;
import net.minecraft.block.material.Material;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public enum GeneratorSeaPickle implements IConfigurableWorldGenerator
{
    INSTANCE;

    @Override
    public void generate(@Nonnull Random random, int chunkX, int chunkZ, @Nonnull World world, @Nonnull IChunkGenerator chunkGenerator, @Nonnull IChunkProvider chunkProvider) {
        if(SeaPickle.enabled && isDimensionValid(world, SeaPickle.dimensions)) {
            final int originX = chunkX << 4 | 8;
            final int originZ = chunkZ << 4 | 8;
            final int amountForGen = getMaxForBiome(world, originX, originZ, SeaPickle.PER_BIOME_RARITY, SeaPickle.defaultAmount);

            for(int i = 0; i < amountForGen; i++) {
                final int amount = WeightedRandom.getRandomItem(random, SeaPickle.SIZE_TABLE).amount;
                if(amount > 0) {
                    final BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(originX + random.nextInt(16), 0, originZ + random.nextInt(16)));
                    if(SubaquaticBlocks.SEA_PICKLE.canPlaceBlockAt(world, pos) && world.getBlockState(pos).getMaterial() == Material.WATER)
                        world.setBlockState(pos, SubaquaticBlocks.SEA_PICKLE.getDefaultState().withProperty(BlockSeaPickle.PICKLES, amount).withProperty(BlockSeaPickle.GLOWING, true));
                }
            }
        }
    }
}
