package git.jbredwards.subaquatic.mod.common.config;

import com.google.common.io.Files;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeColorHelper;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.*;

/**
 *
 * @author jbred
 *
 */
public final class SubaquaticWaterColorConfig
{

    @Nonnull
    public static final Object2IntMap<Biome> FOG_COLORS = new Object2IntOpenHashMap<>(), SURFACE_COLORS = new Object2IntOpenHashMap<>();

    @SuppressWarnings("UnstableApiUsage")
    public static void buildWaterColors() throws IOException {
        final File file = new File("config/subaquatic", "water_colors.jsonc");
        //read from existing
        try { parseWaterColors(new FileReader(file)); }
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
        return new Color(BiomeColorHelper.getColorAtPos(worldIn, posIn, (biome, pos) -> FOG_COLORS.computeIfAbsent(biome, key -> 0x1f3d81))).getColorComponents(new float[3]);
    }

    public static int getSurfaceColor(@Nonnull Biome biome, int originalColor) {
        return SURFACE_COLORS.computeIfAbsent(biome, key -> emulateLegacyColor(originalColor));
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
    public static final String defaultConfigValues =
            "{\n" +
            "    //Plains\n" +
            "    \"minecraft:plains\":{\n" +
            "        \"Surface\":\"0x44AFF5\",\n" +
            "        \"Fog\":\"0x44AFF5\"\n" +
            "    },\n" +
            "    //Sunflower Plains\n" +
            "    \"minecraft:mutated_plains\":{\n" +
            "        \"Surface\":\"0x44AFF5\",\n" +
            "        \"Fog\":\"0x44AFF5\"\n" +
            "    },\n" +
            "    //Desert\n" +
            "    \"minecraft:desert\":{\n" +
            "        \"Surface\":\"0x32A598\",\n" +
            "        \"Fog\":\"0x32A598\"\n" +
            "    },\n" +
            "    //Extreme Hills\n" +
            "    \"minecraft:extreme_hills\":{\n" +
            "        \"Surface\":\"0x007BF7\",\n" +
            "        \"Fog\":\"0x007BF7\"\n" +
            "    },\n" +
            "    //Forest\n" +
            "    \"minecraft:forest\":{\n" +
            "        \"Surface\":\"0x1E97F2\",\n" +
            "        \"Fog\":\"0x1E97F2\"\n" +
            "    },\n" +
            "    //Flower Forest\n" +
            "    \"minecraft:mutated_forest\":{\n" +
            "        \"Surface\":\"0x20A3CC\",\n" +
            "        \"Fog\":\"0x20A3CC\"\n" +
            "    },\n" +
            "    //Taiga\n" +
            "    \"minecraft:taiga\":{\n" +
            "        \"Surface\":\"0x287082\",\n" +
            "        \"Fog\":\"0x287082\"\n" +
            "    },\n" +
            "    //Taiga Hills\n" +
            "    \"minecraft:mutated_taiga\":{\n" +
            "        \"Surface\":\"0x1E6B82\",\n" +
            "        \"Fog\":\"0x1E6B82\"\n" +
            "    },\n" +
            "    //Swampland\n" +
            "    \"minecraft:swampland\":{\n" +
            "        \"Surface\":\"0x617B64\",\n" +
            "        \"Fog\":\"0x4C6559\"\n" +
            "    },\n" +
            "    //Swampland Hills\n" +
            "    \"minecraft:mutated_swampland\":{\n" +
            "        \"Surface\":\"0x617B64\",\n" +
            "        \"Fog\":\"0x4C6156\"\n" +
            "    },\n" +
            "    //River\n" +
            "    \"minecraft:river\":{\n" +
            "        \"Surface\":\"0x0084FF\",\n" +
            "        \"Fog\":\"0x0084FF\"\n" +
            "    },\n" +
            "    //Nether\n" +
            "    \"minecraft:hell\":{\n" +
            "        \"Surface\":\"0x905957\",\n" +
            "        \"Fog\":\"0x905957\"\n" +
            "    },\n" +
            "    //The End\n" +
            "    \"minecraft:sky\":{\n" +
            "        \"Surface\":\"0x62529E\",\n" +
            "        \"Fog\":\"0x62529E\"\n" +
            "    },\n" +
            "    //Frozen River\n" +
            "    \"minecraft:frozen_river\":{\n" +
            "        \"Surface\":\"0x185390\",\n" +
            "        \"Fog\":\"0x185390\"\n" +
            "    },\n" +
            "    //Ice Plains\n" +
            "    \"minecraft:ice_flats\":{\n" +
            "        \"Surface\":\"0x14559B\",\n" +
            "        \"Fog\":\"0x14559B\"\n" +
            "    },\n" +
            "    //Ice Spikes\n" +
            "    \"minecraft:mutated_ice_flats\":{\n" +
            "        \"Surface\":\"0x14559B\",\n" +
            "        \"Fog\":\"0x14559B\"\n" +
            "    },\n" +
            "    //Ice Mountains\n" +
            "    \"minecraft:ice_mountains\":{\n" +
            "        \"Surface\":\"0x1156A7\",\n" +
            "        \"Fog\":\"0x1156A7\"\n" +
            "    },\n" +
            "    //Mushroom Island\n" +
            "    \"minecraft:mushroom_island\":{\n" +
            "        \"Surface\":\"0x8A8997\",\n" +
            "        \"Fog\":\"0x8A8997\"\n" +
            "    },\n" +
            "    //Mushroom Island Shore\n" +
            "    \"minecraft:mushroom_island_shore\":{\n" +
            "        \"Surface\":\"0x818193\",\n" +
            "        \"Fog\":\"0x818193\"\n" +
            "    },\n" +
            "    //Beach\n" +
            "    \"minecraft:beaches\":{\n" +
            "        \"Surface\":\"0x157CAB\",\n" +
            "        \"Fog\":\"0x157CAB\"\n" +
            "    },\n" +
            "    //Desert Hills\n" +
            "    \"minecraft:desert_hills\":{\n" +
            "        \"Surface\":\"0x1A7AA1\",\n" +
            "        \"Fog\":\"0x1A7AA1\"\n" +
            "    },\n" +
            "    //Forest Hills\n" +
            "    \"minecraft:forest_hills\":{\n" +
            "        \"Surface\":\"0x056BD1\",\n" +
            "        \"Fog\":\"0x056BD1\"\n" +
            "    },\n" +
            "    //Taiga Hills\n" +
            "    \"minecraft:taiga_hills\":{\n" +
            "        \"Surface\":\"0x236583\",\n" +
            "        \"Fog\":\"0x236583\"\n" +
            "    },\n" +
            "    //Mountain Edge\n" +
            "    \"minecraft:smaller_extreme_hills\":{\n" +
            "        \"Surface\":\"0x045CD5\",\n" +
            "        \"Fog\":\"0x045CD5\"\n" +
            "    },\n" +
            "    //Jungle\n" +
            "    \"minecraft:jungle\":{\n" +
            "        \"Surface\":\"0x14A2C5\",\n" +
            "        \"Fog\":\"0x14A2C5\"\n" +
            "    },\n" +
            "    //Jungle Hills\n" +
            "    \"minecraft:jungle_hills\":{\n" +
            "        \"Surface\":\"0x1B9ED8\",\n" +
            "        \"Fog\":\"0x1B9ED8\"\n" +
            "    },\n" +
            "    //Jungle M\n" +
            "    \"minecraft:mutated_jungle\":{\n" +
            "        \"Surface\":\"0x1B9ED8\",\n" +
            "        \"Fog\":\"0x1B9ED8\"\n" +
            "    },\n" +
            "    //Jungle Edge\n" +
            "    \"minecraft:jungle_edge\":{\n" +
            "        \"Surface\":\"0x0D8AE3\",\n" +
            "        \"Fog\":\"0x0D8AE3\"\n" +
            "    },\n" +
            "    //Jungle Edge M\n" +
            "    \"minecraft:mutated_jungle_edge\":{\n" +
            "        \"Surface\":\"0x0D8AE3\",\n" +
            "        \"Fog\":\"0x0D8AE3\"\n" +
            "    },\n" +
            "    //Stone Beach\n" +
            "    \"minecraft:stone_beach\":{\n" +
            "        \"Surface\":\"0x0D67BB\",\n" +
            "        \"Fog\":\"0x0D67BB\"\n" +
            "    },\n" +
            "    //Cold Beach\n" +
            "    \"minecraft:cold_beach\":{\n" +
            "        \"Surface\":\"0x1463A5\",\n" +
            "        \"Fog\":\"0x1463A5\"\n" +
            "    },\n" +
            "    //Birch Forest\n" +
            "    \"minecraft:birsh_forest\":{\n" +
            "        \"Surface\":\"0x0677CE\",\n" +
            "        \"Fog\":\"0x0677CE\"\n" +
            "    },\n" +
            "    //Birch Forest Hills\n" +
            "    \"minecraft:birsh_forest_hills\":{\n" +
            "        \"Surface\":\"0x0A74C4\",\n" +
            "        \"Fog\":\"0x0A74C4\"\n" +
            "    },\n" +
            "    //Dark Oak Forest\n" +
            "    \"minecraft:roofed_forest\":{\n" +
            "        \"Surface\":\"0x3B6CD1\",\n" +
            "        \"Fog\":\"0x3B6CD1\"\n" +
            "    },\n" +
            "    //Cold Tiaga\n" +
            "    \"minecraft:taiga_cold\":{\n" +
            "        \"Surface\":\"0x205E83\",\n" +
            "        \"Fog\":\"0x205E83\"\n" +
            "    },\n" +
            "    //Cold Tiaga Mountain\n" +
            "    \"minecraft:mutated_taiga_cold\":{\n" +
            "        \"Surface\":\"0x205E83\",\n" +
            "        \"Fog\":\"0x205E83\"\n" +
            "    },\n" +
            "    //Cold Tiaga Hills\n" +
            "    \"minecraft:taiga_cold_hills\":{\n" +
            "        \"Surface\":\"0x245B78\",\n" +
            "        \"Fog\":\"0x245B78\"\n" +
            "    },\n" +
            "    //Redwood Taiga\n" +
            "    \"minecraft:reedwood_taiga\":{\n" +
            "        \"Surface\":\"0x2D6D77\",\n" +
            "        \"Fog\":\"0x2D6D77\"\n" +
            "    },\n" +
            "    //Redwood Taiga Spruce\n" +
            "    \"minecraft:mutated_redwood_taiga\":{\n" +
            "        \"Surface\":\"0x2D6D77\",\n" +
            "        \"Fog\":\"0x2D6D77\"\n" +
            "    },\n" +
            "    //Redwood Taiga Spruce Hills\n" +
            "    \"minecraft:mutated_redwood_taiga_hills\":{\n" +
            "        \"Surface\":\"0x2D6D77\",\n" +
            "        \"Fog\":\"0x2D6D77\"\n" +
            "    },\n" +
            "    //Redwood Taiga Hills\n" +
            "    \"minecraft:redwood_taiga_hills\":{\n" +
            "        \"Surface\":\"0x286378\",\n" +
            "        \"Fog\":\"0x286378\"\n" +
            "    },\n" +
            "    //Extreme Hills With Trees\n" +
            "    \"minecraft:extreme_hills_with_trees\":{\n" +
            "        \"Surface\":\"0x0E63AB\",\n" +
            "        \"Fog\":\"0x0E63AB\"\n" +
            "    },\n" +
            "    //Extreme Hills M\n" +
            "    \"minecraft:mutated_extreme_hills\":{\n" +
            "        \"Surface\":\"0x0E63AB\",\n" +
            "        \"Fog\":\"0x0E63AB\"\n" +
            "    },\n" +
            "    //Extreme Hills With Trees M\n" +
            "    \"minecraft:mutated_extreme_hills_with_trees\":{\n" +
            "        \"Surface\":\"0x0E63AB\",\n" +
            "        \"Fog\":\"0x0E63AB\"\n" +
            "    },\n" +
            "    //Savanna\n" +
            "    \"minecraft:savanna\":{\n" +
            "        \"Surface\":\"0x2C8B9C\",\n" +
            "        \"Fog\":\"0x2C8B9C\"\n" +
            "    },\n" +
            "    //Savanna Plateau\n" +
            "    \"minecraft:savanna_rock\":{\n" +
            "        \"Surface\":\"0x2590A8\",\n" +
            "        \"Fog\":\"0x2590A8\"\n" +
            "    },\n" +
            "    //Savanna M\n" +
            "    \"minecraft:mutated_savanna\":{\n" +
            "        \"Surface\":\"0x2590A8\",\n" +
            "        \"Fog\":\"0x2590A8\"\n" +
            "    },\n" +
            "    //Savanna Plateau M\n" +
            "    \"minecraft:mutated_savanna_rock\":{\n" +
            "        \"Surface\":\"0x2590A8\",\n" +
            "        \"Fog\":\"0x2590A8\"\n" +
            "    },\n" +
            "    //Mesa\n" +
            "    \"minecraft:mesa\":{\n" +
            "        \"Surface\":\"0x4E7F81\",\n" +
            "        \"Fog\":\"0x4E7F81\"\n" +
            "    },\n" +
            "    //Mesa Plateau\n" +
            "    \"minecraft:mesa_clear_rock\":{\n" +
            "        \"Surface\":\"0x497F99\",\n" +
            "        \"Fog\":\"0x497F99\"\n" +
            "    },\n" +
            "    //Mesa Plateau Forest\n" +
            "    \"minecraft:mesa_rock\":{\n" +
            "        \"Surface\":\"0x497F99\",\n" +
            "        \"Fog\":\"0x497F99\"\n" +
            "    },\n" +
            "    //Mesa (Bryce)\n" +
            "    \"minecraft:mutated_mesa\":{\n" +
            "        \"Surface\":\"0x55809E\",\n" +
            "        \"Fog\":\"0x55809E\"\n" +
            "    },\n" +
            "    //Mesa Plateau M\n" +
            "    \"minecraft:mutated_mesa_clear_rock\":{\n" +
            "        \"Surface\":\"0x55809E\",\n" +
            "        \"Fog\":\"0x55809E\"\n" +
            "    },\n" +
            "    //Mesa Plateau Forest M\n" +
            "    \"minecraft:mutated_mesa_rock\":{\n" +
            "        \"Surface\":\"0x55809E\",\n" +
            "        \"Fog\":\"0x55809E\"\n" +
            "    },\n" +
            "    //Ocean\n" +
            "    \"minecraft:ocean\":{\n" +
            "        \"Surface\":\"0x1787D4\",\n" +
            "        \"Fog\":\"0x1165B0\"\n" +
            "    },\n" +
            "    //Deep Ocean\n" +
            "    \"minecraft:deep_ocean\":{\n" +
            "        \"Surface\":\"0x1787D4\",\n" +
            "        \"Fog\":\"0x1463A5\"\n" +
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
            "        \"Surface\":\"0x3D57D6\",\n" +
            "        \"Fog\":\"0x3D57D6\"\n" +
            "    },\n" +
            "    //Deep Cold Ocean\n" +
            "    \"subaquatic:deep_cold_ocean\":{\n" +
            "        \"Surface\":\"0x3D57D6\",\n" +
            "        \"Fog\":\"0x3D57D6\"\n" +
            "    },\n" +
            "    //Frozen Ocean\n" +
            "    \"minecraft:frozen_ocean\":{\n" +
            "        \"Surface\":\"0x3938C9\",\n" +
            "        \"Fog\":\"0x3938C9\"\n" +
            "    },\n" +
            "    //Deep Frozen Ocean\n" +
            "    \"subaquatic:deep_frozen_ocean\":{\n" +
            "        \"Surface\":\"0x3938C9\",\n" +
            "        \"Fog\":\"0x3938C9\"\n" +
            "    }\n" +
            "}";
}
