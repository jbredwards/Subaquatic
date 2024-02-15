/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.block.material;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class MaterialOceanPlant extends Material
{
    public MaterialOceanPlant(@Nonnull MapColor color) {
        super(color);
        setNoPushMobility();
    }

    @Override
    public boolean isOpaque() { return false; }

    @Override
    public boolean isSolid() { return false; }

    @Override
    public boolean blocksLight() { return false; }
}
