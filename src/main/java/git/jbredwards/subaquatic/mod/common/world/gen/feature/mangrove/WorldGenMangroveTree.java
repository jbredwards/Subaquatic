/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.world.gen.feature.mangrove;

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
    public final boolean tall;
    public WorldGenMangroveTree(final boolean notify, final boolean tallIn) {
        super(notify);
        tall = tallIn;
    }

    @Override
    public boolean generate(@Nonnull final World worldIn, @Nonnull final Random rand, @Nonnull final BlockPos position) {
        // TODO
        return false;
    }
}
