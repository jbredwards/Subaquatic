package git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticEntities;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author jbred
 *
 */
public class EntityBucketHandlerCod extends AbstractEntityBucketHandler
{
    @Nonnull
    @Override
    public EntityEntry getEntityEntry() { return SubaquaticEntities.COD; }

    @Nonnull
    @Override
    public List<ResourceLocation> getSpriteDependencies() {
        return Collections.singletonList(new ResourceLocation(Subaquatic.MODID, "items/fish_bucket_overlays/cod"));
    }
}
