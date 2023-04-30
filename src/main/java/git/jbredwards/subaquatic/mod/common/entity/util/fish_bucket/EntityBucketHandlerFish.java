package git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticEntities;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class EntityBucketHandlerFish extends AbstractEntityBucketHandler
{
    @Nonnull
    @Override
    public EntityEntry getEntityEntry() { return SubaquaticEntities.FISH; }

    @Nonnull
    @Override
    protected ResourceLocation getSpriteForRender() {
        return new ResourceLocation(Subaquatic.MODID, "items/fish_bucket_overlays/fish");
    }
}
