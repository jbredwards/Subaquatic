/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.config;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.config.util.ConfigUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author jbred
 *
 */
@Config(modid = Subaquatic.MODID)
@Mod.EventBusSubscriber(modid = Subaquatic.MODID)
public final class SubaquaticConfigHandler
{
    @Config.LangKey("config.subaquatic.client")
    public static Client client;
    public static final class Client
    {
        @Config.LangKey("config.subaquatic.client.block")
        public static Block block;
        public static final class Block
        {
            @Config.LangKey("config.subaquatic.client.block.leverRedstoneParticles")
            public static boolean leverRedstoneParticles = true;
        }

        @Config.LangKey("config.subaquatic.client.entity")
        public static Entity entity;
        public static final class Entity
        {
            @Config.LangKey("config.subaquatic.client.entity.translucentXPOrbs")
            public static boolean translucentXPOrbs = true;
        }

        @Config.LangKey("config.subaquatic.client.item")
        public static Item item;
        public static final class Item
        {
            @Config.LangKey("config.subaquatic.client.item.dragonBreathEnchantGlint")
            public static boolean dragonBreathEnchantGlint = true;

            @Config.LangKey("config.subaquatic.client.item.expBottleEnchantGlint")
            public static boolean expBottleEnchantGlint = false;

            @Config.LangKey("config.subaquatic.client.item.potionEnchantGlint")
            public static boolean potionEnchantGlint = false;

            @Config.LangKey("config.subaquatic.client.item.translucentWaterBottles")
            public static boolean translucentWaterBottles = true;
        }

        @Config.LangKey("config.subaquatic.client.particle")
        public static Particle particle;
        public static final class Particle
        {
            @Config.LangKey("config.subaquatic.client.particle.longerLastingBubbles")
            public static boolean longerLastingBubbles = true;

            @Config.LangKey("config.subaquatic.client.particle.playBubblePopSound")
            public static boolean playBubblePopSound = true;

            @Config.RangeInt(min = 0)
            @Config.LangKey("config.subaquatic.client.particle.underwaterChestMaxBubbleCount")
            public static int underwaterChestMaxBubbleCount = 10;

            @Config.RangeInt(min = 0)
            @Config.LangKey("config.subaquatic.client.particle.underwaterChestMinBubbleCount")
            public static int underwaterChestMinBubbleCount = 5;

            @Config.RangeInt(min = 0)
            @Config.LangKey("config.subaquatic.client.particle.underwaterExplosionBubbleCount")
            public static int underwaterExplosionBubbleCount = 4;
        }

        @Config.LangKey("config.subaquatic.client.world")
        public static World world;
        public static final class World
        {
            @SuppressWarnings("unused") //used through asm
            @Config.SlidingOption
            @Config.RangeInt(min = 0, max = 15)
            @Config.LangKey("config.subaquatic.client.world.biomeColorBlendRadius")
            public static int biomeColorBlendRadius = 5;

            @Config.Ignore //wip, currently crashes with nothirium
            @Config.LangKey("config.subaquatic.client.world.coloredRain")
            public static boolean coloredRain = true;
        }
    }

    @Config.LangKey("config.subaquatic.common")
    public static Common common;
    public static final class Common
    {
        @Config.LangKey("config.subaquatic.common.block")
        public static Block block;
        public static final class Block
        {
            @Config.LangKey("config.subaquatic.common.block.cauldronFluidPhysics")
            public static boolean cauldronFluidPhysics = true;

            @Config.LangKey("config.subaquatic.common.block.deadCoralBonemeal")
            public static boolean deadCoralBonemeal = true;
        }

        @Config.LangKey("config.subaquatic.common.entity")
        public static Entity entity;
        public static final class Entity
        {
            @Config.LangKey("config.subaquatic.common.entity.gradualAirReplenish")
            public static boolean gradualAirReplenish = true;

            @Config.LangKey("config.subaquatic.common.entity.itemsFloat")
            public static boolean itemsFloat = true;

            @Config.LangKey("config.subaquatic.common.entity.xpOrbsFloat")
            public static boolean xpOrbsFloat = true;
        }

        @Config.LangKey("config.subaquatic.common.item")
        public static Item item;
        public static final class Item
        {
            @Config.RangeInt(min = 0, max = 16)
            @Config.LangKey("config.subaquatic.common.item.compactFishingMaxLvl")
            public static int compactFishingMaxLvl = 5;

            @Config.LangKey("config.subaquatic.common.item.compactFishingMending")
            public static boolean compactFishingMending = false;

            @Nonnull
            @Config.LangKey("config.subaquatic.common.item.fishBucketFluidBlacklist")
            public static String[] fishBucketFluidBlacklist = new String[] {"biomesoplenty:honey","biomesoplenty:poison","biomesoplenty:sand"};

            @Config.LangKey("config.subaquatic.common.item.placeableNautilusShell")
            public static boolean placeableNautilusShell = true;
        }
    }

    @Config.LangKey("config.subaquatic.server")
    public static Server server;
    public static final class Server
    {
        @Config.LangKey("config.subaquatic.server.block")
        public static Block block;
        public static final class Block
        {
            @Nonnull
            @Config.LangKey("config.subaquatic.server.block.bubbleColumnSoilDown")
            public static String[] bubbleColumnSoilDown = new String[] {"{Name:\"minecraft:magma\"}"};

            @Nonnull
            @Config.LangKey("config.subaquatic.server.block.bubbleColumnSoilUp")
            public static String[] bubbleColumnSoilUp = new String[] {"{Name:\"minecraft:soul_sand\"}"};

            @Config.LangKey("config.subaquatic.server.block.coralNeedsSilkTouch")
            public static boolean coralNeedsSilkTouch = true;

            @Config.LangKey("config.subaquatic.server.block.coralPlantsShearable")
            public static boolean coralPlantsShearable = true;

            @Config.LangKey("config.subaquatic.server.block.mushroomBlockFortune")
            public static boolean mushroomBlockFortune = true;

            @SuppressWarnings("unused") //used through asm
            @Config.LangKey("config.subaquatic.server.block.mushroomBlockSilkTouch")
            public static boolean mushroomBlockSilkTouch = true;

            @Config.LangKey("config.subaquatic.server.block.mushroomStemsDropMushrooms")
            public static boolean mushroomStemsDropMushrooms = false;

            @Config.LangKey("config.subaquatic.server.block.tillRootedDirtGivesRoot")
            public static boolean tillRootedDirtGivesRoot = true;
        }

        @Config.LangKey("config.subaquatic.server.entity")
        public static Entity entity;
        public static final class Entity
        {
            @Config.LangKey("config.subaquatic.server.entity.babyZombiesBurnInDaylight")
            public static boolean babyZombiesBurnInDaylight = true;

            @Config.LangKey("config.subaquatic.server.entity.villagerMarineBiologist")
            public static boolean villagerMarineBiologist = false;

            @Config.LangKey("config.subaquatic.server.entity.zombieVillagerMarineBiologist")
            public static boolean zombieVillagerMarineBiologist = true;
        }

        @Config.LangKey("config.subaquatic.server.item")
        public static Item item;
        public static final class Item
        {
            /*@Config.RangeDouble(min = -1)
            @Config.LangKey("config.subaquatic.server.item.aquaticBonemealHealWaterCreature")
            public static float aquaticBonemealHealWaterCreature = -1;

            @Config.RangeDouble(min = -1)
            @Config.LangKey("config.subaquatic.server.item.aquaticBonemealHealWaterBreathingCreature")
            public static float aquaticBonemealHealWaterBreathingCreature = -1;*/

            @Config.LangKey("config.subaquatic.server.item.realisticFishing")
            public static boolean realisticFishing = true;
        }

        @Config.LangKey("config.subaquatic.server.world")
        public static World world;
        public static final class World
        {
            @Config.LangKey("config.subaquatic.server.world.general")
            public static General general;
            public static final class General
            {
                @Config.RangeDouble(min = 0, max = 1)
                @Config.LangKey("config.subaquatic.server.world.general.generateFacelessPumpkinsChance")
                public static double generateFacelessPumpkinsChance = 1;

                @Config.LangKey("config.subaquatic.server.world.general.generateRootedDirt")
                public static boolean generateRootedDirt = true;

                @Config.RangeInt(min = 0, max = 64)
                @Config.LangKey("config.subaquatic.server.world.general.oceanBiomeSize")
                public static int oceanBiomeSize = 6;
            }

            @Config.LangKey("config.subaquatic.server.world.coral")
            public static Coral coral;
            public static final class Coral
            {
                //generic world gen settings

                @Nonnull
                @Config.Ignore
                public static final Object2IntMap<Biome> PER_BIOME_RARITY = new Object2IntOpenHashMap<>();

                @Config.LangKey("config.subaquatic.server.world.generic.defaultAmount")
                public static int defaultAmount = 0;

                @Nonnull
                @Config.LangKey("config.subaquatic.server.world.generic.dimensions")
                public static int[] dimensions = new int[] {0};

                @Config.LangKey("config.subaquatic.server.world.generic.enabled")
                public static boolean enabled = true;

                @Nonnull
                @Config.LangKey("config.subaquatic.server.world.generic.perBiomeRarity")
                public static String[] perBiomeRarity = new String[] {
                        "{Value:20, Biomes:['subaquatic:warm_ocean','subaquatic:deep_warm_ocean']}"
                };
            }

            @Config.LangKey("config.subaquatic.server.world.glowLichen")
            public static GlowLichen glowLichen;
            public static final class GlowLichen
            {
                //generic world gen settings

                @Nonnull
                @Config.Ignore
                public static final Object2IntMap<Biome> PER_BIOME_RARITY = new Object2IntOpenHashMap<>();

                @Config.LangKey("config.subaquatic.server.world.generic.defaultAmount")
                public static int defaultAmount = 130;

                @Nonnull
                @Config.LangKey("config.subaquatic.server.world.generic.dimensions")
                public static int[] dimensions = new int[] {0};

                @Config.LangKey("config.subaquatic.server.world.generic.enabled")
                public static boolean enabled = true;

                @Nonnull
                @Config.LangKey("config.subaquatic.server.world.generic.perBiomeRarity")
                public static String[] perBiomeRarity = new String[] {};

                @Config.RangeInt(min = 1, max = 256)
                @Config.LangKey("config.subaquatic.server.world.generic.maxHeight")
                public static int maxHeight = 120;

                @Config.RangeInt(min = 1, max = 256)
                @Config.LangKey("config.subaquatic.server.world.generic.minHeight")
                public static int minHeight = 1;

                //glow lichen specific

                @Config.RangeInt(min = 0, max = 15)
                @Config.LangKey("config.subaquatic.server.world.glowLichen.maxLight")
                public static int maxLight = 0;
            }

            @Config.LangKey("config.subaquatic.server.world.kelp")
            public static Kelp kelp;
            public static final class Kelp
            {
                //generic world gen settings

                @Nonnull
                @Config.Ignore
                public static final Object2IntMap<Biome> PER_BIOME_RARITY = new Object2IntOpenHashMap<>();

                @Config.LangKey("config.subaquatic.server.world.generic.defaultAmount")
                public static int defaultAmount = 0;

                @Nonnull
                @Config.LangKey("config.subaquatic.server.world.generic.dimensions")
                public static int[] dimensions = new int[] {0};

                @Config.LangKey("config.subaquatic.server.world.generic.enabled")
                public static boolean enabled = true;

                @Nonnull
                @Config.LangKey("config.subaquatic.server.world.generic.perBiomeRarity")
                public static String[] perBiomeRarity = new String[] {
                        "{Value:80, BiomeTags:['ocean']}",
                        "{Value:120, Biomes:['minecraft:ocean','minecraft:deep_ocean']}",
                        "{Value:0, Biomes:['minecraft:frozen_ocean','subaquatic:deep_frozen_ocean','subaquatic:warm_ocean','subaquatic:deep_warm_ocean']}"
                };

                //kelp specific

                @Config.RangeInt(min = 1)
                @Config.LangKey("config.subaquatic.server.world.kelp.maxHeight")
                public static int maxHeight = 10;

                @Config.RangeInt(min = 1)
                @Config.LangKey("config.subaquatic.server.world.kelp.minHeight")
                public static int minHeight = 1;
            }

            @Config.LangKey("config.subaquatic.server.world.seagrass")
            public static Seagrass seagrass;
            public static final class Seagrass
            {
                //generic world gen settings

                @Nonnull
                @Config.Ignore
                public static final Object2IntMap<Biome> PER_BIOME_RARITY = new Object2IntOpenHashMap<>();

                @Config.LangKey("config.subaquatic.server.world.generic.defaultAmount")
                public static int defaultAmount = 32;

                @Nonnull
                @Config.LangKey("config.subaquatic.server.world.generic.dimensions")
                public static int[] dimensions = new int[] {0};

                @Config.LangKey("config.subaquatic.server.world.generic.enabled")
                public static boolean enabled = true;

                @Nonnull
                @Config.LangKey("config.subaquatic.server.world.generic.perBiomeRarity")
                public static String[] perBiomeRarity = new String[] {
                        "{Value:80, BiomeTags:['ocean']}",
                        "{Value:64, BiomeTags:['swamp']}",
                        "{Value:48, BiomeTags:['river'], Biomes:['minecraft:ocean','minecraft:deep_ocean']}",
                        "{Value:40, Biomes:['subaquatic:deep_cold_ocean']}",
                        "{Value:32, Biomes:['subaquatic:cold_ocean']}",
                        "{Value:8, Biomes:['minecraft:frozen_ocean','subaquatic:deep_frozen_ocean']}"
                };

                //seagrass specific

                @Config.RangeDouble(min = 0, max = 1)
                @Config.LangKey("config.subaquatic.server.world.seagrass.chanceForDouble")
                public static double chanceForDouble = 0.3;
            }

            @Config.LangKey("config.subaquatic.server.world.seaPickle")
            public static SeaPickle seaPickle;
            public static final class SeaPickle
            {
                //generic world gen settings

                @Nonnull
                @Config.Ignore
                public static final Object2IntMap<Biome> PER_BIOME_RARITY = new Object2IntOpenHashMap<>();

                @Config.LangKey("config.subaquatic.server.world.generic.defaultAmount")
                public static int defaultAmount = 0;

                @Nonnull
                @Config.LangKey("config.subaquatic.server.world.generic.dimensions")
                public static int[] dimensions = new int[] {0};

                @Config.LangKey("config.subaquatic.server.world.generic.enabled")
                public static boolean enabled = true;

                @Nonnull
                @Config.LangKey("config.subaquatic.server.world.generic.perBiomeRarity")
                public static String[] perBiomeRarity = new String[] {
                        "{Value:1, Biomes:['subaquatic:warm_ocean','subaquatic:deep_warm_ocean']}"
                };

                //sea pickle specific

                @Nonnull
                @Config.Ignore
                public static final List<SeaPickleSize> SIZE_TABLE = new ArrayList<>();
                public static final class SeaPickleSize extends WeightedRandom.Item
                {
                    public final int amount;
                    public SeaPickleSize(int itemWeightIn, int amountIn) {
                        super(itemWeightIn);
                        amount = amountIn;
                    }
                }

                @Config.LangKey("config.subaquatic.server.world.seaPickle.size0Weight")
                public static int size0Weight = 24;

                @Config.LangKey("config.subaquatic.server.world.seaPickle.size1Weight")
                public static int size1Weight = 1;

                @Config.LangKey("config.subaquatic.server.world.seaPickle.size2Weight")
                public static int size2Weight = 1;

                @Config.LangKey("config.subaquatic.server.world.seaPickle.size3Weight")
                public static int size3Weight = 1;

                @Config.LangKey("config.subaquatic.server.world.seaPickle.size4Weight")
                public static int size4Weight = 1;
            }
        }
    }

    @Nonnull
    @Config.Ignore
    public static final Set<IBlockState> BUBBLE_COLUMN_SOIL_DOWN = new HashSet<>();

    @Nonnull
    @Config.Ignore
    public static final Set<IBlockState> BUBBLE_COLUMN_SOIL_UP = new HashSet<>();

    @Nonnull
    @Config.Ignore
    public static final Set<Fluid> FISH_BUCKET_FLUID_BLACKLIST = new HashSet<>();

    //internal
    public static void init() {
        //downward bubble column source blocks
        BUBBLE_COLUMN_SOIL_DOWN.clear();
        for(String id : Server.Block.bubbleColumnSoilDown) {
            try {
                final IBlockState soil = NBTUtil.readBlockState(JsonToNBT.getTagFromJson(id));
                if(soil.getBlock() != Blocks.AIR) BUBBLE_COLUMN_SOIL_DOWN.add(soil);
            }
            catch(NBTException e) { e.printStackTrace(); }
        }

        //upward bubble column source blocks
        BUBBLE_COLUMN_SOIL_UP.clear();
        for(String id : Server.Block.bubbleColumnSoilUp) {
            try {
                final IBlockState soil = NBTUtil.readBlockState(JsonToNBT.getTagFromJson(id));
                if(soil.getBlock() != Blocks.AIR) BUBBLE_COLUMN_SOIL_UP.add(soil);
            }
            catch(NBTException e) { e.printStackTrace(); }
        }

        //fish bucket fluid blacklist
        FISH_BUCKET_FLUID_BLACKLIST.clear();
        for(String id : Common.Item.fishBucketFluidBlacklist) {
            @Nullable Fluid fluid = FluidloggedUtils.getFluidFromBlock(Block.getBlockFromName(id));
            if(fluid == null) fluid = FluidRegistry.getFluid(id);

            if(fluid != null && fluid.canBePlacedInWorld() && fluid.getBlock().getDefaultState().getMaterial() == Material.WATER)
                FISH_BUCKET_FLUID_BLACKLIST.add(fluid); //only add to the blacklist if the fluid would normally be able to hold fish
        }

        //sea pickle cluster gen weights
        Server.World.SeaPickle.SIZE_TABLE.clear();
        Server.World.SeaPickle.SIZE_TABLE.add(new Server.World.SeaPickle.SeaPickleSize(Server.World.SeaPickle.size0Weight, 0));
        Server.World.SeaPickle.SIZE_TABLE.add(new Server.World.SeaPickle.SeaPickleSize(Server.World.SeaPickle.size1Weight, 1));
        Server.World.SeaPickle.SIZE_TABLE.add(new Server.World.SeaPickle.SeaPickleSize(Server.World.SeaPickle.size2Weight, 2));
        Server.World.SeaPickle.SIZE_TABLE.add(new Server.World.SeaPickle.SeaPickleSize(Server.World.SeaPickle.size3Weight, 3));
        Server.World.SeaPickle.SIZE_TABLE.add(new Server.World.SeaPickle.SeaPickleSize(Server.World.SeaPickle.size4Weight, 4));

        //per-biome rarity world gen
        ConfigUtils.readPerBiomeRarity(Server.World.Coral.perBiomeRarity, Server.World.Coral.PER_BIOME_RARITY);
        ConfigUtils.readPerBiomeRarity(Server.World.GlowLichen.perBiomeRarity, Server.World.GlowLichen.PER_BIOME_RARITY);
        ConfigUtils.readPerBiomeRarity(Server.World.Kelp.perBiomeRarity, Server.World.Kelp.PER_BIOME_RARITY);
        ConfigUtils.readPerBiomeRarity(Server.World.Seagrass.perBiomeRarity, Server.World.Seagrass.PER_BIOME_RARITY);
        ConfigUtils.readPerBiomeRarity(Server.World.SeaPickle.perBiomeRarity, Server.World.SeaPickle.PER_BIOME_RARITY);
    }

    @SubscribeEvent
    static void syncConfig(@Nonnull ConfigChangedEvent.OnConfigChangedEvent event) {
        if(Subaquatic.MODID.equals(event.getModID())) {
            init();

            ConfigManager.sync(Subaquatic.MODID, Config.Type.INSTANCE); //ran after init(), so any potentially broken configs are not saved
            if(event.isWorldRunning() && FMLCommonHandler.instance().getSide().isClient()) reloadRender();
        }
    }

    @SideOnly(Side.CLIENT)
    static void reloadRender() { Minecraft.getMinecraft().renderGlobal.loadRenderers(); }
}
