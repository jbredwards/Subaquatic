/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.config;

import com.google.common.io.Files;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import git.jbredwards.fluidlogged_api.api.util.FluidState;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.api.biome.IWaterColorProvider;
import git.jbredwards.subaquatic.mod.common.config.util.ConfigUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author jbred
 *
 */
public final class SubaquaticWaterColorConfig
{
    @Nonnull static final Object2IntMap<Biome> FOG_COLORS = new Object2IntOpenHashMap<>(), SURFACE_COLORS = new Object2IntOpenHashMap<>();
    @Nonnull static final Lock colorLock = new ReentrantLock(); //fixes a fast-util crash
    @Nonnull public static final Map<Fluid, Color> FLUID_PIXEL_BASE_COLORS = new HashMap<>();

    @SuppressWarnings("UnstableApiUsage")
    public static void buildWaterColors() throws IOException {
        //external configs
        ConfigUtils.parseFromMods("subaquatic/water_colors.jsonc", SubaquaticWaterColorConfig::parseWaterColors);

        //modpack config
        final File file = new File("config/subaquatic", "water_colors.jsonc");
        try { parseWaterColors(new FileReader(file)); } //read from existing
        catch(FileNotFoundException e) {
            //generate from default values
            parseWaterColors(new StringReader(defaultConfigValues));

            //create new file
            Files.createParentDirs(file);
            if(file.createNewFile()) {
                final FileWriter writer = new FileWriter(file);
                writer.write(defaultConfigValues);
                writer.close();
            }
        }
    }

    static void parseWaterColors(@Nonnull Reader reader) {
        final JsonObject waterColorsFile = new JsonParser().parse(reader).getAsJsonObject();
        waterColorsFile.entrySet().forEach(element -> {
            if(element.getValue().isJsonObject()) {
                final Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(element.getKey()));
                if(biome != null) {
                    final JsonPrimitive fogColor = element.getValue().getAsJsonObject().getAsJsonPrimitive("Fog");
                    if(fogColor != null) {
                        if(fogColor.isString()) FOG_COLORS.put(biome, Integer.decode(fogColor.getAsString()));
                        else if(fogColor.isNumber()) FOG_COLORS.put(biome, fogColor.getAsInt());
                    }

                    final JsonPrimitive surfaceColor = element.getValue().getAsJsonObject().getAsJsonPrimitive("Surface");
                    if(surfaceColor != null) {
                        if(surfaceColor.isString()) SURFACE_COLORS.put(biome, Integer.decode(surfaceColor.getAsString()));
                        else if(surfaceColor.isNumber()) SURFACE_COLORS.put(biome, surfaceColor.getAsInt());
                    }
                }
            }
        });
    }

    public static float[] getFogColorAt(@Nonnull IBlockAccess worldIn, @Nonnull BlockPos posIn) {
        return new Color(BiomeColorHelper.getColorAtPos(worldIn, posIn,
                (biomeIn, pos) -> computeIfAbsentSafe(FOG_COLORS, biomeIn,
                        biome -> biome instanceof IWaterColorProvider
                                ? ((IWaterColorProvider)biome).getWaterFogColor()
                                : biome.getWaterColorMultiplier())
        )).getColorComponents(new float[3]);
    }

    public static int getSurfaceColor(@Nonnull Biome biomeIn, int originalColor) {
        return computeIfAbsentSafe(SURFACE_COLORS, biomeIn, biome -> {
            //biome has a special color that's already preserved
            if(biome instanceof IWaterColorProvider) return ((IWaterColorProvider)biome).getWaterSurfaceColor();

            //biome is using the 1.12 swamp color, give it the 1.13 one
            else if(originalColor == 14745518) return 0x617B64;

            //biome has a special color (not the default 1.12 color), try preserving it
            else if(originalColor != 16777215) return emulateLegacyColor(originalColor);

            //biome has no special color, give it one that applies to its biome tag
            else if(!ForgeRegistries.BIOMES.containsValue(biome)) return DEFAULT_WATER_COLOR; //catch unregistered biomes
            final Set<BiomeDictionary.Type> biomeTags = BiomeDictionary.getTypes(biome);

            //high priority
            if(biomeTags.contains(BiomeDictionary.Type.NETHER)) return 0x905957;
            else if(biomeTags.contains(BiomeDictionary.Type.END)) return 0x62529E;
            else if(biomeTags.contains(BiomeDictionary.Type.MESA)) return 0x4E7F81;
            else if(biomeTags.contains(BiomeDictionary.Type.JUNGLE)) return 0x1B9ED8;
            else if(biomeTags.contains(BiomeDictionary.Type.MUSHROOM)) return 0x8A8997;

            //medium priority
            else if(biomeTags.contains(BiomeDictionary.Type.SWAMP)) return 0x617B64;
            else if(biomeTags.contains(BiomeDictionary.Type.SANDY)) return 0x32A598;
            else if(biomeTags.contains(BiomeDictionary.Type.SAVANNA)) return 0x2C8B9C;
            else if(biomeTags.contains(BiomeDictionary.Type.CONIFEROUS)) return 0x1E6B82;

            //low priority
            else if(biomeTags.contains(BiomeDictionary.Type.WASTELAND)) return 0x14559B;
            else if(biomeTags.contains(BiomeDictionary.Type.MOUNTAIN)) return 0x0E63AB;
            else if(biomeTags.contains(BiomeDictionary.Type.FOREST)) return 0x3F76E4;
            else if(biomeTags.contains(BiomeDictionary.Type.PLAINS)) return 0x3F76E4;
            else if(biomeTags.contains(BiomeDictionary.Type.SNOWY)) return 0x1156A7;
            else if(biomeTags.contains(BiomeDictionary.Type.BEACH)) return 0x3F76E4;
            else if(biomeTags.contains(BiomeDictionary.Type.COLD)) return 0x007BF7;
            else if(biomeTags.contains(BiomeDictionary.Type.HOT)) return 0x1A7AA1;

            //should never pass, but if it does apply the default 1.13 water color
            return DEFAULT_WATER_COLOR;
        });
    }

    //fixes a rare, but possible, array out of bounds exception
    @Nonnull
    static <K, V> V computeIfAbsentSafe(@Nonnull Map<K, V> map, @Nonnull K key, @Nonnull Function<? super K, ? extends V> mappingFunction) {
        colorLock.lock();
        try { return map.computeIfAbsent(key, mappingFunction); }
        finally { colorLock.unlock(); }
    }

    /**
     * Modified code from Aqua Acrobatics
     */
    static final int DEFAULT_WATER_COLOR = 0x3F76E4;
    static final int DEFAULT_WATER_COLOR_112 = 0xFFFFFF;
    static final int PERCEIVED_WATER_COLOR_112 = 0x2b3bf4;
    static int emulateLegacyColor(int originalColor) {
        if(originalColor == DEFAULT_WATER_COLOR_112) return DEFAULT_WATER_COLOR;
        final int modR = (originalColor & 0xff0000) >> 16;
        final int modG = (originalColor & 0x00ff00) >> 8;
        final int modB = (originalColor & 0x0000ff);
        final int legacyR = (PERCEIVED_WATER_COLOR_112 & 0xff0000) >> 16;
        final int legacyG = (PERCEIVED_WATER_COLOR_112 & 0x00ff00) >> 8;
        final int legacyB = (PERCEIVED_WATER_COLOR_112 & 0x0000ff);
        final int displayedR = (modR * legacyR) / 255;
        final int displayedG = (modG * legacyG) / 255;
        final int displayedB = (modB * legacyB) / 255;
        return (displayedR << 16) | (displayedG << 8) | displayedB;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public static Color getParticleColorAt(@Nonnull World worldIn, double x, double y, double z) {
        final Pair<BlockPos, FluidState> here = findClosestAround(FluidloggedUtils::getFluidState, fluidState -> !fluidState.isEmpty(), worldIn, x, y, z);
        if(here == null) return new Color(BiomeColorHelper.getWaterColorAtPos(worldIn, new BlockPos(x, y, z)));

        final BlockPos pos = here.getLeft();
        final FluidState fluidState = here.getRight();

        //use block color if applicable
        final int blockColor = Minecraft.getMinecraft().getBlockColors().colorMultiplier(fluidState.getState(), worldIn, pos, 0);
        if(blockColor != -1) return new Color(blockColor);

        //use fluid color if applicable
        final int fluidColor = fluidState.getFluid().getColor(worldIn, pos);
        return fluidColor != 0xFFFFFFFF ? new Color(fluidColor) : FLUID_PIXEL_BASE_COLORS.get(fluidState.getFluid());
    }

    @Nullable
    public static <T> Pair<BlockPos, T> findClosestAround(@Nonnull BiFunction<World, BlockPos, T> getter, @Nonnull Predicate<T> checker, @Nonnull World world, double x, double y, double z) {
        final BlockPos origin = new BlockPos(x, y, z);
        T instance = getter.apply(world, origin);

        if(checker.test(instance)) return Pair.of(origin, instance);
        final List<Pair<BlockPos, T>> instances = new ArrayList<>();
        for(BlockPos pos : BlockPos.getAllInBoxMutable(new BlockPos(x - 0.25, y - 0.25, z - 0.25), new BlockPos(x + 0.25, y + 0.25, z + 0.25))) {
            if(!pos.equals(origin)) {
                instance = getter.apply(world, pos);
                if(checker.test(instance)) instances.add(Pair.of(pos, instance));
            }
        }

        double closestDist = -1;
        Pair<BlockPos, T> closest = null;
        for(Pair<BlockPos, T> entry : instances) {
            final double distance = entry.getLeft().distanceSqToCenter(x, y, z);
            if(closestDist > distance) {
                closestDist = distance;
                closest = entry;
            }
        }

        return closest;
    }

    @Nonnull
    public static final String defaultConfigValues =
            "{\n" +
            "    //Frozen River\n" +
            "    \"minecraft:frozen_river\":{\n" +
            "        \"Surface\":\"0x185390\",\n" +
            "        \"Fog\":\"0x185390\"\n" +
            "    },\n" +
            "    //Warm Ocean\n" +
            "    \"subaquatic:warm_ocean\":{\n" +
            "        \"Surface\":\"0x43D5EE\",\n" +
            "        \"Fog\":\"0x43D5EE\"\n" +
            "    },\n" +
            "    //Deep Warm Ocean\n" +
            "    \"subaquatic:deep_warm_ocean\":{\n" +
            "        \"Surface\":\"0x43D5EE\",\n" +
            "        \"Fog\":\"0x43D5EE\"\n" +
            "    },\n" +
            "    //Lukewarm Ocean\n" +
            "    \"subaquatic:lukewarm_ocean\":{\n" +
            "        \"Surface\":\"0x45ADF2\",\n" +
            "        \"Fog\":\"0x45ADF2\"\n" +
            "    },\n" +
            "    //Deep Lukewarm Ocean\n" +
            "    \"subaquatic:deep_lukewarm_ocean\":{\n" +
            "        \"Surface\":\"0x45ADF2\",\n" +
            "        \"Fog\":\"0x45ADF2\"\n" +
            "    },\n" +
            "    //Cold Ocean\n" +
            "    \"subaquatic:cold_ocean\":{\n" +
            "        \"Surface\":\"0x6092f2\",\n" +
            "        \"Fog\":\"0x6092f2\"\n" +
            "    },\n" +
            "    //Deep Cold Ocean\n" +
            "    \"subaquatic:deep_cold_ocean\":{\n" +
            "        \"Surface\":\"0x6092f2\",\n" +
            "        \"Fog\":\"0x6092f2\"\n" +
            "    },\n" +
            "    //Frozen Ocean\n" +
            "    \"minecraft:frozen_ocean\":{\n" +
            "        \"Surface\":\"0x77a9ff\",\n" +
            "        \"Fog\":\"0x77a9ff\"\n" +
            "    },\n" +
            "    //Deep Frozen Ocean\n" +
            "    \"subaquatic:deep_frozen_ocean\":{\n" +
            "        \"Surface\":\"0x77a9ff\",\n" +
            "        \"Fog\":\"0x77a9ff\"\n" +
            "    }\n" +
            "}";
}
