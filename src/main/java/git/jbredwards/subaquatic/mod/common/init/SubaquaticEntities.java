package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.entity.item.*;
import git.jbredwards.subaquatic.mod.common.entity.living.*;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
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
    @Nonnull public static final EntityEntry CHEST_BOAT = register("chest_boat",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityBoatChest.class));
    @Nonnull public static final EntityEntry ENDER_CHEST_BOAT = register("ender_chest_boat",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityBoatEnderChest.class));
    @Nonnull public static final EntityEntry SALMON = register("salmon",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntitySalmon.class).egg(10489616, 951412)
                    .spawn(EnumCreatureType.WATER_CREATURE, 5, 1, 5, Biomes.RIVER, Biomes.FROZEN_RIVER)
                    .spawn(EnumCreatureType.WATER_CREATURE, 15, 1, 5, Biomes.FROZEN_OCEAN, SubaquaticBiomes.DEEP_FROZEN_OCEAN, SubaquaticBiomes.COLD_OCEAN, SubaquaticBiomes.DEEP_COLD_OCEAN));

    @Nonnull
    static EntityEntry register(@Nonnull String name, @Nonnull EntityEntryBuilder<?> builder) {
        final EntityEntry entry = builder.id(new ResourceLocation(Subaquatic.MODID, name), id++).name(Subaquatic.MODID + '.' + name).build();
        INIT.add(entry);
        return entry;
    }
}
