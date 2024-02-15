/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.item;

import git.jbredwards.subaquatic.mod.common.entity.item.part.MultiPartContainerPart;
import git.jbredwards.subaquatic.mod.common.entity.item.part.MultiPartEnderChestPart;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class EntityBoatEnderChest extends AbstractBoatContainer
{
    public EntityBoatEnderChest(@Nonnull World worldIn) { super(worldIn); }
    public EntityBoatEnderChest(@Nonnull World worldIn, double x, double y, double z) { super(worldIn, x, y, z); }

    @Nonnull
    @Override
    protected MultiPartContainerPart createContainerPart() {
        return new MultiPartEnderChestPart(this, "ender_chest", 0.775f, 0.775f);
    }
}
