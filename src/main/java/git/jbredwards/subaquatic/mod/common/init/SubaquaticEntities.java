package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.entity.item.*;
import git.jbredwards.subaquatic.mod.common.entity.living.*;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

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
    @Nonnull
    public static final List<EntityEntry> INIT = new ArrayList<>();
    static int id = 0;

    // Entities
    @Nonnull
    public static final EntityEntry CHEST_BOAT = register("chest_boat",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityBoatChest.class));
    @Nonnull
    public static final EntityEntry ENDER_CHEST_BOAT = register("ender_chest_boat",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityBoatEnderChest.class));

    @Nonnull
    public static final EntityEntry ENDER_CHEST_MINECART = register("ender_chest_minecart",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityMinecartEnderChest.class));

    @Nonnull
    public static final EntityEntry ENDER_GLOW_SQUID = register("ender_glow_squid",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityEnderGlowSquid.class).egg(0x37234d, 0x9e7fbd)
                    .spawn(EnumCreatureType.WATER_CREATURE, 10, 1, 4, BiomeDictionary.getBiomes(BiomeDictionary.Type.END)));
    @Nonnull
    public static final EntityEntry GLOW_SQUID = register("glow_squid",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityGlowSquid.class).egg(3515039, 11730125)
                    .spawn(EnumCreatureType.WATER_CREATURE, 2, 1, 4, BiomeDictionary.getBiomes(BiomeDictionary.Type.SWAMP)));
    @Nonnull
    public static final EntityEntry SALMON = register("salmon",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntitySalmon.class).egg(10489616, 951412)
                    .spawn(EnumCreatureType.WATER_CREATURE, 5, 1, 5, Biomes.RIVER, Biomes.FROZEN_RIVER)
                    .spawn(EnumCreatureType.WATER_CREATURE, 15, 1, 5, Biomes.FROZEN_OCEAN, SubaquaticBiomes.DEEP_FROZEN_OCEAN, SubaquaticBiomes.COLD_OCEAN, SubaquaticBiomes.DEEP_COLD_OCEAN));

    static void handleSpawns() {
        //fix the spawning mechanics of this mod's water creatures
        INIT.forEach(entry -> { if(EntityWaterCreature.class.isAssignableFrom(entry.getEntityClass()) || EntityWaterMob.class.isAssignableFrom(entry.getEntityClass()))
                EntitySpawnPlacementRegistry.setPlacementType(entry.getEntityClass(), EntityLiving.SpawnPlacementType.IN_WATER); });

        //swamps spawn glow squids instead of normal squids
        BiomeDictionary.getBiomes(BiomeDictionary.Type.SWAMP)
                .forEach(biome -> biome.getSpawnableList(EnumCreatureType.WATER_CREATURE).removeIf(entry -> entry.entityClass == EntitySquid.class));

        //globally adjust the spawn rates of squids
        ForgeRegistries.BIOMES.forEach(biome -> biome.getSpawnableList(EnumCreatureType.WATER_CREATURE).forEach(entry -> {
            if(entry.entityClass == EntitySquid.class) { entry.minGroupCount = 1; entry.itemWeight = 2; }
        }));
    }

    @Nonnull
    static EntityEntry register(@Nonnull String name, @Nonnull EntityEntryBuilder<?> builder) {
        final EntityEntry entry = builder.id(new ResourceLocation(Subaquatic.MODID, name), id++).name(Subaquatic.MODID + '.' + name).build();
        INIT.add(entry);
        return entry;
    }
}
