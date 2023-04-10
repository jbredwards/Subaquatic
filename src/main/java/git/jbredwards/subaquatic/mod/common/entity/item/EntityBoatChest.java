package git.jbredwards.subaquatic.mod.common.entity.item;

import git.jbredwards.subaquatic.mod.common.entity.item.part.MultiPartChestPart;
import git.jbredwards.subaquatic.mod.common.entity.item.part.MultiPartContainerPart;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class EntityBoatChest extends AbstractBoatContainer
{
    public EntityBoatChest(@Nonnull World worldIn) { super(worldIn); }
    public EntityBoatChest(@Nonnull World worldIn, double x, double y, double z) { super(worldIn, x, y, z); }

    @Nonnull
    @Override
    protected MultiPartContainerPart createContainerPart() {
        return new MultiPartChestPart(this, "chest", 0.775f, 0.775f);
    }
}
