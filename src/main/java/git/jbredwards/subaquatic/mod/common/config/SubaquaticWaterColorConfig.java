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
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.*;
import java.util.Set;

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
        return new Color(BiomeColorHelper.getColorAtPos(worldIn, posIn, (biomeIn, pos) ->
                FOG_COLORS.computeIfAbsent(biomeIn, Biome::getWaterColorMultiplier))).getColorComponents(new float[3]);
    }

    public static int getSurfaceColor(@Nonnull Biome biomeIn, int originalColor) {
        return SURFACE_COLORS.computeIfAbsent(biomeIn, biome -> {
            //biome is using the 1.12 swamp color, give it the 1.13 one
            if(originalColor == 14745518) return 0x617B64;

            //biome has a special color (not the default 1.12 color), try preserving it
            else if(originalColor != 16777215) return emulateLegacyColor(originalColor);

            //biome has no special color, give it one that applies to its biome tag
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
            else if(biomeTags.contains(BiomeDictionary.Type.FOREST)) return 0x1E97F2;
            else if(biomeTags.contains(BiomeDictionary.Type.PLAINS)) return 0x44AFF5;
            else if(biomeTags.contains(BiomeDictionary.Type.SNOWY)) return 0x1156A7;
            else if(biomeTags.contains(BiomeDictionary.Type.BEACH)) return 0x157CAB;
            else if(biomeTags.contains(BiomeDictionary.Type.COLD)) return 0x007BF7;
            else if(biomeTags.contains(BiomeDictionary.Type.HOT)) return 0x1A7AA1;

            //should never pass, but if it does apply the default 1.13 water color
            return emulateLegacyColor(originalColor);
        });
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
    public static final String defaultConfigValues = "";
}
