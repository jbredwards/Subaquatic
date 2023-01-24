package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.entity.item.*;
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
    @Nonnull public static final EntityEntry CHEST_BOAT = register("chest_boat", EntityEntryBuilder.create().entity(EntityBoatChest.class).tracker(80, 3, true));
    @Nonnull public static final EntityEntry ENDER_CHEST_BOAT = register("ender_chest_boat", EntityEntryBuilder.create().entity(EntityBoatEnderChest.class).tracker(80, 3, true));

    @Nonnull
    static EntityEntry register(@Nonnull String name, @Nonnull EntityEntryBuilder<?> builder) {
        final EntityEntry entry = builder.id(new ResourceLocation(Subaquatic.MODID, name), id++).name(name).build();
        INIT.add(entry);
        return entry;
    }
}
