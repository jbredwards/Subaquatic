/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.compat.jer;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticTropicalFishConfig;
import git.jbredwards.subaquatic.mod.common.entity.living.EntityPufferfish;
import git.jbredwards.subaquatic.mod.common.entity.living.EntityTropicalFish;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticEntities;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticItems;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticLootTables;
import jeresources.api.IJERAPI;
import jeresources.api.IMobRegistry;
import jeresources.api.JERPlugin;
import jeresources.api.conditionals.LightLevel;
import jeresources.api.drop.PlantDrop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Invadermonky
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public final class SubaquaticJERPlugin
{
    @JERPlugin
    public static IJERAPI JERApi;
    public static void init() {
        registerMobs();
        registerPlants();
    }

    // ============
    // MOB REGISTRY
    // ============

    static long lastPufferfishRenderTick = Minecraft.getSystemTime();
    static long lastTropicalFishRenderTick = Minecraft.getSystemTime();

    private static void registerMobs() {
        IMobRegistry registry = JERApi.getMobRegistry();
        World world = JERApi.getWorld();
        registerMob(registry, world, SubaquaticEntities.COD, SubaquaticLootTables.ENTITIES_COD);
        registerMob(registry, world, SubaquaticEntities.FISH, SubaquaticLootTables.ENTITIES_FISH);
        registerMob(registry, world, SubaquaticEntities.FROG, SubaquaticLootTables.ENTITIES_FROG);
        registerMob(registry, world, SubaquaticEntities.PUFFERFISH, SubaquaticLootTables.ENTITIES_PUFFERFISH);
        registerMob(registry, world, SubaquaticEntities.SALMON, SubaquaticLootTables.ENTITIES_SALMON);
        registerMob(registry, world, SubaquaticEntities.TADPOLE, SubaquaticLootTables.ENTITIES_TADPOLE);
        registerMob(registry, world, SubaquaticEntities.TROPICAL_FISH, SubaquaticLootTables.ENTITIES_TROPICAL_FISH);
        registerMob(registry, world, SubaquaticEntities.TURTLE, SubaquaticLootTables.ENTITIES_TURTLE);

        // Pufferfish: Render each puff state.
        registry.registerRenderHook(EntityPufferfish.class, (info, fish) -> {
            if(Minecraft.getSystemTime() - lastPufferfishRenderTick >= 1000) {
                ((EntityPufferfish)fish).setPuffState((int)((lastPufferfishRenderTick = Minecraft.getSystemTime()) / 1000 % 3));
            }

            return info;
        });

        // Tropical Fish: Render each naturally spawning variant.
        registry.registerRenderHook(EntityTropicalFish.class, (info, fish) -> {
            if(Minecraft.getSystemTime() - lastTropicalFishRenderTick >= 1000) {
                ((EntityTropicalFish)fish).setVariant(SubaquaticTropicalFishConfig.DEFAULT_TYPES.get((int)
                ((lastTropicalFishRenderTick = Minecraft.getSystemTime()) / 1000 % SubaquaticTropicalFishConfig.DEFAULT_TYPES.size())));
            }

            return info;
        });
    }

    private static void registerMob(@Nonnull final IMobRegistry registry, @Nonnull final World world, @Nonnull final EntityEntry entry, @Nonnull final ResourceLocation lootTable) {
        @Nonnull final EntityLiving entity = (EntityLiving)entry.newInstance(world);
        if(entity.canBreatheUnderwater()) entity.inWater = true; // Don't render fish sideways.
        registry.register(entity, entity instanceof IMob ? LightLevel.hostile : LightLevel.any, entity.experienceValue, getBiomes(entry.getEntityClass()), lootTable);
    }

    @Nonnull
    private static String[] getBiomes(@Nonnull final Class<?> entity) {
        return getBiomeNames(ForgeRegistries.BIOMES.getValuesCollection().stream().filter(biome -> {
            for(@Nonnull final EnumCreatureType type : EnumCreatureType.values()) if(biome.getSpawnableList(type).stream().anyMatch(spawn -> spawn.entityClass == entity)) return true;
            return false;
        }).toArray(Biome[]::new));
    }

    @Nonnull
    private static String[] getBiomeNames(@Nonnull final Biome... biomes) {
        List<String> biomeNames = new ArrayList<>();

        for(Biome biome : biomes) {
            biomeNames.add(biome.getBiomeName());
        }

        return biomeNames.size() > 0 ? biomeNames.toArray(new String[0]) : new String[] {I18n.format("jer.any")};
    }

    // ==============
    // PLANT REGISTRY
    // ==============

    private static void registerPlants() {
        JERApi.getPlantRegistry().registerWithSoil(new ItemStack(SubaquaticItems.KELP), Blocks.SAND.getDefaultState(), new PlantDrop(new ItemStack(SubaquaticItems.KELP), 1, 1));
        JERApi.getPlantRegistry().registerWithSoil(new ItemStack(SubaquaticItems.SEAGRASS), Blocks.SAND.getDefaultState(), new PlantDrop(withTooltip(SubaquaticItems.SEAGRASS, "shears"), 1, 1));
    }

    @Nonnull
    private static ItemStack withTooltip(@Nonnull final Item item, @Nonnull final String tooltip) {
        @Nonnull final NBTTagList tooltipNbt = new NBTTagList();
        tooltipNbt.appendTag(new NBTTagString(I18n.format(Subaquatic.MODID + ".tooltip.item.jer." + tooltip)));

        @Nonnull final ItemStack stack = new ItemStack(item);
        stack.getOrCreateSubCompound("display").setTag("Lore", tooltipNbt);
        return stack;
    }
}
