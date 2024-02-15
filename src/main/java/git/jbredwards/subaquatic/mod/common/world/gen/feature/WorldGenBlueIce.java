/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.world.gen.feature;

import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public class WorldGenBlueIce extends WorldGenerator
{
    @Override
    public boolean generate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos position) {
        position = position.add(8, 0, 8);
        if(position.getY() >= world.getSeaLevel()) return false;
        if(world.getBlockState(position).getBlock() != Blocks.WATER && world.getBlockState(position.down()).getBlock() != Blocks.WATER) return false;

        boolean isPackedIceNear = false;
        for(EnumFacing facing : EnumFacing.values()) {
            if(facing != EnumFacing.DOWN && world.getBlockState(position.offset(facing)).getBlock() == Blocks.PACKED_ICE) {
                isPackedIceNear = true;
                break;
            }
        }

        if(!isPackedIceNear) return false;
        setBlockAndNotifyAdequately(world, position, SubaquaticBlocks.BLUE_ICE.getDefaultState());

        for(int i = 0; i < 200; i++) {
            final int y = rand.nextInt(5) - rand.nextInt(6);
            final int h = y < 2 ? 3 + y / 2 : 3;

            final BlockPos pos = position.add(rand.nextInt(h) - rand.nextInt(h), y, rand.nextInt(h) - rand.nextInt(h));
            final IBlockState state = world.getBlockState(pos);

            if(state.getMaterial() == Material.AIR || state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.PACKED_ICE || state.getBlock() == Blocks.ICE) {
                for(EnumFacing facing : EnumFacing.values()) {
                    if(world.getBlockState(pos.offset(facing)).getBlock() == SubaquaticBlocks.BLUE_ICE) {
                        setBlockAndNotifyAdequately(world, pos, SubaquaticBlocks.BLUE_ICE.getDefaultState());
                        break;
                    }
                }
            }
        }

        return true;
    }
}
