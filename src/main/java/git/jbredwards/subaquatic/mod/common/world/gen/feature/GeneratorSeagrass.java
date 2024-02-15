/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.world.gen.feature;

import git.jbredwards.subaquatic.mod.common.block.BlockSeagrass;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler.Server.World.Seagrass;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import git.jbredwards.subaquatic.mod.common.world.gen.IConfigurableWorldGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public enum GeneratorSeagrass implements IConfigurableWorldGenerator
{
    INSTANCE;

    @Override
    public void generate(@Nonnull Random random, int chunkX, int chunkZ, @Nonnull World world, @Nonnull IChunkGenerator chunkGenerator, @Nonnull IChunkProvider chunkProvider) {
        if(Seagrass.enabled && isDimensionValid(world, Seagrass.dimensions)) {
            final int originX = chunkX << 4 | 8;
            final int originZ = chunkZ << 4 | 8;
            final int amountForGen = getMaxForBiome(world, originX, originZ, Seagrass.PER_BIOME_RARITY, Seagrass.defaultAmount);

            for(int i = 0; i < amountForGen; i++) {
                final int offsetX = random.nextInt(16);
                final int offsetZ = random.nextInt(16);

                final BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(originX + offsetX, 0, originZ + offsetZ));
                final Biome biome = world.getBiome(pos);

                if(!biome.isSnowyBiome() && world.getBlockState(pos.down()).isTopSolid() && SubaquaticBlocks.SEAGRASS.canPlaceBlockAt(world, pos)) {
                    world.setBlockState(pos, SubaquaticBlocks.SEAGRASS.getDefaultState(), 2);
                    //try growing a double tall plant
                    if(random.nextFloat() < Seagrass.chanceForDouble && SubaquaticBlocks.SEAGRASS.canPlaceBlockAt(world, pos.up()))
                        world.setBlockState(pos.up(), SubaquaticBlocks.SEAGRASS.getDefaultState().withProperty(BlockSeagrass.TYPE, BlockSeagrass.Type.TOP), 2);
                }
            }
        }
    }
}
