/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.world.gen.feature.coral;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public enum CoralStructureMushroom implements ICoralStructure
{
    INSTANCE;

    @Override
    public void generate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull ICoralBlockSupplier coralBlock) {
        final int maxX = rand.nextInt(3) + 3;
        final int maxY = rand.nextInt(3) + 3;
        final int maxZ = rand.nextInt(3) + 3;
        final int down = rand.nextInt(3) + 1;

        for(int x = 0; x <= maxX; x++) {
            for(int y = 0; y <= maxY; y++) {
                for(int z = 0; z <= maxZ; z++) {
                    if((x != 0 && x != maxX || y != 0 && y != maxY)
                    && (z != 0 && z != maxZ || y != 0 && y != maxY)
                    && (x != 0 && x != maxX || z != 0 && z != maxZ)
                    && (x == 0 || x == maxX || y == 0 || y == maxY || z == 0 || z == maxZ)
                    && rand.nextFloat() < 0.9) placeCoralBlock(world, rand, pos.add(x, y - down, z), coralBlock);
                }
            }
        }
    }
}
