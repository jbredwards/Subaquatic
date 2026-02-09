/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.world.gen.feature.tree;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public class WorldGenMangroveTree extends WorldGenAbstractTree
{
    public WorldGenMangroveTree(final boolean notify) {
        super(notify);
    }

    @Override
    public boolean generate(@Nonnull final World worldIn, @Nonnull final Random rand, @Nonnull final BlockPos position) {
        // TODO
        return false;
    }
}
