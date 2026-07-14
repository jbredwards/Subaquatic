/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.init;

import com.google.common.collect.ImmutableList;
import git.jbredwards.ocean_api.api.BucketableEntityBehavior;
import git.jbredwards.ocean_api.api.entity.EntityWaterCreature;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticTropicalFishConfig;
import git.jbredwards.subaquatic.mod.common.entity.item.*;
import git.jbredwards.subaquatic.mod.common.entity.living.*;
import git.jbredwards.subaquatic.mod.common.entity.util.TropicalFishData;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.*;

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
                    .spawn(EnumCreatureType.WATER_CREATURE, 25, 8, 8, SubaquaticBiomes.LUKEWARM_OCEAN, SubaquaticBiomes.DEEP_LUKEWARM_OCEAN, SubaquaticBiomes.WARM_OCEAN, SubaquaticBiomes.DEEP_WARM_OCEAN, SubaquaticBiomes.MANGROVE_SWAMP));
    @Nonnull
    public static final EntityEntry PUFFERFISH = register("pufferfish",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityPufferfish.class).factory(EntityPufferfish::new).egg(16167425, 3654642)
                    .spawn(EnumCreatureType.WATER_CREATURE, 5, 1, 3, SubaquaticBiomes.LUKEWARM_OCEAN, SubaquaticBiomes.DEEP_LUKEWARM_OCEAN)
                    .spawn(EnumCreatureType.WATER_CREATURE, 15, 1, 3, SubaquaticBiomes.WARM_OCEAN, SubaquaticBiomes.DEEP_WARM_OCEAN));
    @Nonnull
    public static final EntityEntry FISH = register("fish",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityFish.class).factory(EntityFish::new).egg(0x6b9f93, 0xadbedb));

    @Nonnull
    public static final EntityEntry TADPOLE = register("tadpole",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityTadpole.class).factory(EntityTadpole::new).egg(7164733, 1444352));

    @Nonnull
    public static final EntityEntry FROG = register("frog",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityFrog.class).factory(EntityFrog::new).egg(13661252, 16762748)
                    /*.spawn(EnumCreatureType.CREATURE, 10, 2, 5, BiomeDictionary.getBiomes(BiomeDictionary.Type.SWAMP))*/);
    @Nonnull
    public static final EntityEntry TURTLE = register("turtle",
            EntityEntryBuilder.create().tracker(80, 3, true).entity(EntityTurtle.class).factory(EntityTurtle::new).egg(15198183, 44975)
                    .spawn(EnumCreatureType.CREATURE, 5, 2, 5, BiomeDictionary.getBiomes(BiomeDictionary.Type.BEACH)));

    // =======
    // Utility
    // =======

    static void handleAdditionalEntityData() {
        // fix the spawning mechanics of this mod's water creatures
        INIT.forEach(entry -> { if(EntityWaterCreature.class.isAssignableFrom(entry.getEntityClass()) || EntityWaterMob.class.isAssignableFrom(entry.getEntityClass()))
                EntitySpawnPlacementRegistry.setPlacementType(entry.getEntityClass(), EntityLiving.SpawnPlacementType.IN_WATER); });

        // globally adjust the spawn rates of squids
        ForgeRegistries.BIOMES.forEach(biome -> biome.getSpawnableList(EnumCreatureType.WATER_CREATURE).forEach(entry -> {
            if(entry.entityClass == EntitySquid.class) { entry.minGroupCount = 1; entry.itemWeight = 2; }
        }));

        // register this mod's entity bucket handlers
        @Nonnull final ResourceLocation path = new ResourceLocation(Subaquatic.MODID, "fish_bucket_overlays");
        BucketableEntityBehavior.REGISTRY.register(builder(FISH).minBucketWidth(6).overlayModel(new ModelResourceLocation(path, "fish"))
                .entityAsItem(new ItemStack(Items.FISH))
                .build());
        BucketableEntityBehavior.REGISTRY.register(builder(COD).minBucketWidth(6).overlayModel(new ModelResourceLocation(path, "cod"))
                .entityAsItem(new ItemStack(SubaquaticItems.COD))
                .build());
        BucketableEntityBehavior.REGISTRY.register(builder(SALMON).minBucketWidth(6).overlayModel(new ModelResourceLocation(path, "salmon"))
                .entityAsItem(new ItemStack(Items.FISH, 1, 1))
                .build());
        BucketableEntityBehavior.REGISTRY.register(SubaquaticEntities.<EntityPufferfish>builder(PUFFERFISH).minBucketWidth(8).overlayModel(new ModelResourceLocation(path, "pufferfish"))
                .entityAsItem(new ItemStack(Items.FISH, 1, 2))
                .read((fish, compound) -> fish.setPuffState(0))
                .build());
        BucketableEntityBehavior.REGISTRY.register(builder(TROPICAL_FISH).minBucketWidth(6).overlayModel(new ModelResourceLocation(path, "tropical_fish"))
                .entityAsItem(new ItemStack(Items.FISH, 1, 3))
                .subtypes(() -> SubaquaticTropicalFishConfig.DEFAULT_TYPES.stream()
                        .map(data -> {
                            @Nonnull final NBTTagCompound compound = new NBTTagCompound();
                            compound.setString("id", Objects.toString(TROPICAL_FISH.getRegistryName()));
                            compound.setInteger("Variant", data.serialize());
                            return compound;
                        })
                        .collect(ImmutableList.toImmutableList()))
                .trim(compound -> {
                    NBTTagCompound trimmed = new NBTTagCompound();
                    trimmed.setString("id", compound.getString("id"));
                    if(compound.hasKey("Variant", Constants.NBT.TAG_INT)) trimmed.setInteger("Variant", compound.getInteger("Variant"));
                    return trimmed;
                })
                .tooltip((compound, advanced) -> {
                    @Nonnull final ImmutableList.Builder<String> builder = ImmutableList.builder();
                    @Nonnull final TropicalFishData data = TropicalFishData.deserialize(compound.getInteger("Variant"));

                    if(data.hasTranslation(I18n::hasKey)) builder.add(data.getTranslatedName(I18n::format));
                    else builder.add(data.getTranslatedShape(I18n::format), data.getTranslatedColor(I18n::format));
                    return builder.build();
                })
                .overlayColor((compound, tintIndex) -> {
                    if(tintIndex != 0 && tintIndex != 1 || !compound.hasKey("Variant", Constants.NBT.TAG_INT)) return OptionalInt.empty();
                    @Nonnull final TropicalFishData data = TropicalFishData.deserialize(compound.getInteger("Variant"));
                    return OptionalInt.of(tintIndex == 0 ? data.primaryColor.getColorValue() : data.secondaryColor.getColorValue());
                })
                .build());
        BucketableEntityBehavior.REGISTRY.register(builder(TADPOLE).minBucketWidth(4).overlayModel(new ModelResourceLocation(path, "tadpole"))
                .entityAsItem(ItemStack.EMPTY)
                .build());
    }

    //helper method that saves me from having to retype the same stuff for each entity
    @Nonnull
    static EntityEntry register(@Nonnull String name, @Nonnull EntityEntryBuilder<?> builder) {
        final EntityEntry entry = builder.id(new ResourceLocation(Subaquatic.MODID, name), id++).name(Subaquatic.MODID + '.' + name).build();
        INIT.add(entry);
        return entry;
    }

    @Nonnull
    static <T extends Entity> BucketableEntityBehavior.Builder<T> builder(@Nonnull final EntityEntry entry) {
        return BucketableEntityBehavior.<T>builder()
                .setEntityClass((Class<T>)entry.getEntityClass())
                .setRegistryName(entry.getRegistryName())
                .pickupSound(SubaquaticSounds.BUCKET_FILL_FISH)
                .breathableWaterlike();
    }
}
