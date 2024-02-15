/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.world.gen.feature;

import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler.Server.World.GlowLichen;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import git.jbredwards.subaquatic.mod.common.tileentity.TileEntityGlowLichen;
import git.jbredwards.subaquatic.mod.common.world.gen.IConfigurableWorldGenerator;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public enum GeneratorGlowLichen implements IConfigurableWorldGenerator
{
    INSTANCE;

    @Override
    public void generate(@Nonnull Random random, int chunkX, int chunkZ, @Nonnull World world, @Nonnull IChunkGenerator chunkGenerator, @Nonnull IChunkProvider chunkProvider) {
        if(GlowLichen.enabled && isDimensionValid(world, GlowLichen.dimensions)) {
            final int originX = chunkX << 4 | 8;
            final int originZ = chunkZ << 4 | 8;

            final int amount = getMaxForBiome(world, originX, originZ, GlowLichen.PER_BIOME_RARITY, GlowLichen.defaultAmount);
            for(int i = 0; i < amount; i++) {
                final BlockPos pos = new BlockPos(originX + random.nextInt(16), MathHelper.getInt(random, GlowLichen.minHeight, GlowLichen.maxHeight), originZ + random.nextInt(16));
                final IBlockState here = world.getBlockState(pos);

                if((here.getMaterial() == Material.WATER || here.getBlock().isAir(here, world, pos)) && (GlowLichen.maxLight == 15 || GlowLichen.maxLight >= world.getLight(pos))) {
                    final EnumFacing[] sides = EnumFacing.HORIZONTALS.clone();
                    Collections.shuffle(Arrays.asList(sides), random);

                    for(EnumFacing side : sides) {
                        if(world.getBlockState(pos.offset(side)).getMaterial() == Material.ROCK) {
                            final TileEntityGlowLichen tile = new TileEntityGlowLichen();
                            tile.attachedSideData = 0;
                            tile.setAttachedTo(side, true);

                            world.setBlockState(pos, SubaquaticBlocks.GLOW_LICHEN.getDefaultState(), 0);
                            world.setTileEntity(pos, tile);

                            break;
                        }
                    }
                }
            }
        }
    }
}
