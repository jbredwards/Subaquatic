/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.compat.inspirations;

import git.jbredwards.ocean_api.mod.compat.inspirations.InspirationsCompat;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticWaterColorConfig;
import knightminer.inspirations.common.Config;
import knightminer.inspirations.recipes.tileentity.TileCauldron;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.awt.*;

/**
 *
 * @author jbred
 *
 */
public final class InspirationsHandler
{
    public static int getCauldronColor(@Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        return InspirationsCompat.getCauldronColor(world, pos);
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public static Color getParticleColorAt(@Nonnull World world, double x, double y, double z) {
        if(Config.enableExtendedCauldron) {
            final Pair<BlockPos, TileEntity> here = SubaquaticWaterColorConfig.findClosestAround(World::getTileEntity, tile -> tile instanceof TileCauldron, world, x, y, z);
            if(here != null) {
                final TileCauldron cauldron = (TileCauldron)here.getRight();
                if(!cauldron.isWater()) return new Color(cauldron.getColor());
            }
        }

        return SubaquaticWaterColorConfig.getParticleColorAt(world, x, y, z);
    }
}
