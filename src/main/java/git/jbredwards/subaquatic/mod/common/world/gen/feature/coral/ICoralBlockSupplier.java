/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.world.gen.feature.coral;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;

/**
 * This class exists for people using GroovyScript to give them the option of registering coral.
 * Used by this mod's coral generators and coral villager trades.
 * @author jbred
 *
 */
public interface ICoralBlockSupplier
{
    /**
     * @return the coral block
     */
    @Nonnull
    Block getBlock();

    /**
     * @return the fluid that must be at the block in order for this to generate
     */
    @Nonnull
    Fluid getNeededFluid();

    /**
     * @return the state to generate, with the correct direction applied
     */
    @Nonnull
    IBlockState withDirection(@Nonnull EnumFacing direction);
}
