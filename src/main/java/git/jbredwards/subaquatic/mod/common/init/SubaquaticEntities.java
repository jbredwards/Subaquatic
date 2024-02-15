/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.entity.item.*;
import git.jbredwards.subaquatic.mod.common.entity.living.*;
import git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket.*;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.LinkedList;
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
    public static final List<EntityEntry> INIT = new LinkedList<>();
    static int id = 0;

    // =============
    // Item Entities
    // =============

    @Nonnull
    public static final EntityEntry CHEST_BOAT = register("chest_boat",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityBoatChest.class).factory(EntityBoatChest::new));
    @Nonnull
    public static final EntityEntry ENDER_CHEST_BOAT = register("ender_chest_boat",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityBoatEnderChest.class).factory(EntityBoatEnderChest::new));
    @Nonnull
    public static final EntityEntry ENDER_CHEST_MINECART = register("ender_chest_minecart",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityMinecartEnderChest.class).factory(EntityMinecartEnderChest::new));
    @Nonnull
    public static final EntityEntry FURNACE_BOAT = register("furnace_boat",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityBoatFurnace.class).factory(EntityBoatFurnace::new));
    @Nonnull
    public static final EntityEntry CRAFTING_TABLE_BOAT = register("crafting_table_boat",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityBoatWorkbench.class).factory(EntityBoatWorkbench::new));
    @Nonnull
    public static final EntityEntry CRAFTING_TABLE_MINECART = register("crafting_table_minecart",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityMinecartWorkbench.class).factory(EntityMinecartWorkbench::new));

    // ===============
    // Living Entities
    // ===============

    @Nonnull
    public static final EntityEntry COD = register("cod",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityCod.class).factory(EntityCod::new).egg(12691306, 15058059)
                    .spawn(EnumCreatureType.WATER_CREATURE, 10, 3, 6, Biomes.OCEAN, Biomes.DEEP_OCEAN)
                    .spawn(EnumCreatureType.WATER_CREATURE, 15, 3, 6, SubaquaticBiomes.COLD_OCEAN, SubaquaticBiomes.DEEP_COLD_OCEAN, SubaquaticBiomes.LUKEWARM_OCEAN)
                    .spawn(EnumCreatureType.WATER_CREATURE, 8, 3, 6, SubaquaticBiomes.DEEP_LUKEWARM_OCEAN));
    @Nonnull
    public static final EntityEntry SALMON = register("salmon",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntitySalmon.class).factory(EntitySalmon::new).egg(10489616, 951412)
                    .spawn(EnumCreatureType.WATER_CREATURE, 5, 1, 5, Biomes.RIVER, Biomes.FROZEN_RIVER)
                    .spawn(EnumCreatureType.WATER_CREATURE, 15, 1, 5, Biomes.FROZEN_OCEAN, SubaquaticBiomes.DEEP_FROZEN_OCEAN, SubaquaticBiomes.COLD_OCEAN, SubaquaticBiomes.DEEP_COLD_OCEAN));
    @Nonnull
    public static final EntityEntry TROPICAL_FISH = register("tropical_fish",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityTropicalFish.class).factory(EntityTropicalFish::new).egg(15690005, 16775663)
                    .spawn(EnumCreatureType.WATER_CREATURE, 25, 8, 8, SubaquaticBiomes.LUKEWARM_OCEAN, SubaquaticBiomes.DEEP_LUKEWARM_OCEAN, SubaquaticBiomes.WARM_OCEAN, SubaquaticBiomes.DEEP_WARM_OCEAN));
    @Nonnull
    public static final EntityEntry PUFFERFISH = register("pufferfish",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityPufferfish.class).factory(EntityPufferfish::new).egg(16167425, 3654642)
                    .spawn(EnumCreatureType.WATER_CREATURE, 5, 1, 3, SubaquaticBiomes.LUKEWARM_OCEAN, SubaquaticBiomes.DEEP_LUKEWARM_OCEAN)
                    .spawn(EnumCreatureType.WATER_CREATURE, 15, 1, 3, SubaquaticBiomes.WARM_OCEAN, SubaquaticBiomes.DEEP_WARM_OCEAN));
    @Nonnull
    public static final EntityEntry FISH = register("fish",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityFish.class).factory(EntityFish::new).egg(0x6b9f93, 0xadbedb));

    @Nonnull
    public static final EntityEntry BOGGED = register("bogged",
            EntityEntryBuilder.create().tracker(128, 3, true).entity(EntityBogged.class).factory(EntityBogged::new).egg(9084018, 3231003)
                    .spawn(EnumCreatureType.MONSTER, 50, 4, 4, BiomeDictionary.getBiomes(BiomeDictionary.Type.SWAMP)));

    static void handleAdditionalEntityData() {
        // fix the spawning mechanics of this mod's water creatures
        INIT.forEach(entry -> { if(EntityWaterCreature.class.isAssignableFrom(entry.getEntityClass()) || EntityWaterMob.class.isAssignableFrom(entry.getEntityClass()))
                EntitySpawnPlacementRegistry.setPlacementType(entry.getEntityClass(), EntityLiving.SpawnPlacementType.IN_WATER); });

        // globally adjust the spawn rates of squids
        ForgeRegistries.BIOMES.forEach(biome -> biome.getSpawnableList(EnumCreatureType.WATER_CREATURE).forEach(entry -> {
            if(entry.entityClass == EntitySquid.class) { entry.minGroupCount = 1; entry.itemWeight = 2; }
        }));

        // remove vanilla skeleton spawns from swamps
        BiomeDictionary.getBiomes(BiomeDictionary.Type.SWAMP).forEach(biome -> biome.getSpawnableList(EnumCreatureType.MONSTER).removeIf(entry -> entry.entityClass == EntitySkeleton.class));

        // register this mod's entity bucket handlers
        AbstractEntityBucketHandler.BUCKET_HANDLERS.put(Subaquatic.MODID + ":fish", EntityBucketHandlerFish::new);
        AbstractEntityBucketHandler.BUCKET_HANDLERS.put(Subaquatic.MODID + ":cod", EntityBucketHandlerCod::new);
        AbstractEntityBucketHandler.BUCKET_HANDLERS.put(Subaquatic.MODID + ":salmon", EntityBucketHandlerSalmon::new);
        AbstractEntityBucketHandler.BUCKET_HANDLERS.put(Subaquatic.MODID + ":pufferfish", EntityBucketHandlerPufferfish::new);
        AbstractEntityBucketHandler.BUCKET_HANDLERS.put(Subaquatic.MODID + ":tropical_fish", EntityBucketHandlerTropicalFish::new);
    }

    //helper method that saves me from having to retype the same stuff for each entity
    @Nonnull
    static EntityEntry register(@Nonnull String name, @Nonnull EntityEntryBuilder<?> builder) {
        final EntityEntry entry = builder.id(new ResourceLocation(Subaquatic.MODID, name), id++).name(Subaquatic.MODID + '.' + name).build();
        INIT.add(entry);
        return entry;
    }
}
