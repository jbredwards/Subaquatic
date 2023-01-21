package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.entity.item.EntityBoatContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jbred
 *
 */
public final class SubaquaticEntities
{
    // Init
    @Nonnull public static final List<EntityEntry> INIT = new ArrayList<>();
    static int id = 0;

    // Entities
    @Nonnull public static final EntityEntry BOAT_CONTAINER = register("boat_container", EntityEntryBuilder.create().entity(EntityBoatContainer.class).tracker(80, 3, true));

    @Nonnull
    static EntityEntry register(@Nonnull String name, @Nonnull EntityEntryBuilder<?> builder) {
        final EntityEntry entry = builder.id(new ResourceLocation(Subaquatic.MODID, name), id++).name(name).build();
        INIT.add(entry);
        return entry;
    }
}
