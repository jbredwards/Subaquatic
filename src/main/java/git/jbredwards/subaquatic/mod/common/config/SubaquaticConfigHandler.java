package git.jbredwards.subaquatic.mod.common.config;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.mod.Subaquatic;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTUtil;
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
import java.util.HashSet;
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

            @SuppressWarnings("unused") //used through asm
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
            @Config.LangKey("config.subaquatic.client.particle.playBubblePopSound")
            public static boolean playBubblePopSound = false;
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
        }

        @Config.LangKey("config.subaquatic.common.entity")
        public static Entity entity;
        public static final class Entity
        {
            @Nonnull
            @Config.LangKey("config.subaquatic.fishBucketFluidBlacklist")
            public static String[] fishBucketFluidBlacklist = new String[] {"biomesoplenty:honey","biomesoplenty:poison","biomesoplenty:sand"};
        }

        @Config.LangKey("config.subaquatic.common.item")
        public static Item item;
        public static final class Item
        {
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
            @Config.LangKey("config.subaquatic.server.block.mushroomBlockFortune")
            public static boolean mushroomBlockFortune = true;

            @SuppressWarnings("unused") //used through asm
            @Config.LangKey("config.subaquatic.server.block.mushroomBlockSilkTouch")
            public static boolean mushroomBlockSilkTouch = true;

            @Config.LangKey("config.subaquatic.server.block.mushroomStemsDropMushrooms")
            public static boolean mushroomStemsDropMushrooms = false;
        }

        @Config.LangKey("config.subaquatic.server.world")
        public static World world;
        public static final class World
        {
            @Nonnull
            @Config.LangKey("config.subaquatic.server.world.bubbleColumnSoilDown")
            public static String[] bubbleColumnSoilDown = new String[] {"{Name:\"minecraft:magma\"}"};

            @Nonnull
            @Config.LangKey("config.subaquatic.server.world.bubbleColumnSoilUp")
            public static String[] bubbleColumnSoilUp = new String[] {"{Name:\"minecraft:soul_sand\"}"};

            @Config.LangKey("config.subaquatic.server.world.growRootedDirt")
            public static boolean growRootedDirt = true;

            @Config.RangeInt(min = 0, max = 64)
            @Config.LangKey("config.subaquatic.server.world.oceanBiomeSize")
            public static int oceanBiomeSize = 6;
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
        BUBBLE_COLUMN_SOIL_DOWN.clear();
        for(String id : Server.World.bubbleColumnSoilDown) {
            try {
                final IBlockState soil = NBTUtil.readBlockState(JsonToNBT.getTagFromJson(id));
                if(soil.getBlock() != Blocks.AIR) BUBBLE_COLUMN_SOIL_DOWN.add(soil);
            }
            catch(NBTException e) { e.printStackTrace(); }
        }

        BUBBLE_COLUMN_SOIL_UP.clear();
        for(String id : Server.World.bubbleColumnSoilUp) {
            try {
                final IBlockState soil = NBTUtil.readBlockState(JsonToNBT.getTagFromJson(id));
                if(soil.getBlock() != Blocks.AIR) BUBBLE_COLUMN_SOIL_UP.add(soil);
            }
            catch(NBTException e) { e.printStackTrace(); }
        }

        FISH_BUCKET_FLUID_BLACKLIST.clear();
        for(String id : Common.Entity.fishBucketFluidBlacklist) {
            @Nullable Fluid fluid = FluidloggedUtils.getFluidFromBlock(Block.getBlockFromName(id));
            if(fluid == null) fluid = FluidRegistry.getFluid(id);

            if(fluid != null) FISH_BUCKET_FLUID_BLACKLIST.add(fluid);
        }
    }

    @SubscribeEvent
    static void syncConfig(@Nonnull ConfigChangedEvent.OnConfigChangedEvent event) {
        if(Subaquatic.MODID.equals(event.getModID())) {
            ConfigManager.sync(Subaquatic.MODID, Config.Type.INSTANCE);
            init();

            if(event.isWorldRunning() && FMLCommonHandler.instance().getSide().isClient()) reloadRender();
        }
    }

    @SideOnly(Side.CLIENT)
    static void reloadRender() { Minecraft.getMinecraft().renderGlobal.loadRenderers(); }
}
