/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.compat.jer;

import git.jbredwards.subaquatic.mod.common.config.SubaquaticTropicalFishConfig;
import git.jbredwards.subaquatic.mod.common.entity.living.*;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticEntities;
import jeresources.api.IJERAPI;
import jeresources.api.IMobRegistry;
import jeresources.api.JERPlugin;
import jeresources.api.conditionals.LightLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.IMob;
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

@SideOnly(Side.CLIENT)
public class SubaquaticJERPlugin {
    static long lastPufferfishRenderTick = Minecraft.getSystemTime();
    static long lastTropicalFishRenderTick = Minecraft.getSystemTime();

    @JERPlugin
    public static IJERAPI JERApi;
    public static void init() {
        IMobRegistry registry = JERApi.getMobRegistry();
        World world = JERApi.getWorld();
        registerMob(registry, world, SubaquaticEntities.COD, EntityCod.LOOT);
        registerMob(registry, world, SubaquaticEntities.FISH, EntityFish.LOOT);
        registerMob(registry, world, SubaquaticEntities.PUFFERFISH, EntityPufferfish.LOOT);
        registerMob(registry, world, SubaquaticEntities.SALMON, EntitySalmon.LOOT);
        registerMob(registry, world, SubaquaticEntities.TROPICAL_FISH, EntityTropicalFish.LOOT);

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
}
