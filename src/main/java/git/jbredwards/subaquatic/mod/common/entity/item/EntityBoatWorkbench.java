/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.item;

import git.jbredwards.subaquatic.mod.common.entity.item.part.MultiPartContainerPart;
import git.jbredwards.subaquatic.mod.common.entity.item.part.MultiPartWorkbenchPart;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class EntityBoatWorkbench extends AbstractBoatContainer
{
    public EntityBoatWorkbench(@Nonnull World worldIn) { super(worldIn); }
    public EntityBoatWorkbench(@Nonnull World worldIn, double x, double y, double z) { super(worldIn, x, y, z); }

    @Nonnull
    @Override
    protected MultiPartContainerPart createContainerPart() {
        return new MultiPartWorkbenchPart(this, "crafting_table", 0.9f, 0.9f);
    }
}
