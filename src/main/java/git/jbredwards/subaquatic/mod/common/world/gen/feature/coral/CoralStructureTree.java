/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.world.gen.feature.coral;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public enum CoralStructureTree implements ICoralStructure
{
    INSTANCE;

    @Override
    public void generate(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull ICoralBlockSupplier coralBlock) {
        final BlockPos.MutableBlockPos posToGen = new BlockPos.MutableBlockPos(pos);
        final int max = rand.nextInt(3 + 1);
        for(int i = 0; i < max; i++) {
            if(!placeCoralBlock(world, rand, posToGen.toImmutable(), coralBlock)) return;
            posToGen.move(EnumFacing.UP);
        }

        final List<EnumFacing> facesToGen = Arrays.asList(EnumFacing.HORIZONTALS);
        Collections.shuffle(facesToGen);
        facesToGen.subList(0, rand.nextInt(3) + 2).forEach(side -> {
            posToGen.setPos(pos);
            posToGen.move(side);

            int branchHeight = 0;
            final int maxHeight = rand.nextInt(5) + 2;
            for(int i = 0; i < maxHeight && placeCoralBlock(world, rand, posToGen.toImmutable(), coralBlock); i++) {
                posToGen.move(EnumFacing.UP);
                if(i == 0 || ++branchHeight >= 2 && rand.nextFloat() < 0.25) {
                    posToGen.move(side);
                    branchHeight = 0;
                }
            }
        });
    }
}
