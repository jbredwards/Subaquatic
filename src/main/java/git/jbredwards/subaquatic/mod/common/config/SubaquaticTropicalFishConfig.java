package git.jbredwards.subaquatic.mod.common.config;

import com.google.common.io.Files;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import git.jbredwards.subaquatic.mod.common.config.util.ConfigUtils;
import git.jbredwards.subaquatic.mod.common.entity.util.TropicalFishData;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.EnumDyeColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;

/**
 *
 * @author jbred
 *
 */
public final class SubaquaticTropicalFishConfig
{
    @Nonnull
    public static final IntList DEFAULT_TYPES = new IntArrayList();

    @SuppressWarnings("UnstableApiUsage")
    public static void buildFishTypes() throws IOException {
        final File file = new File("config/subaquatic", "tropical_fish_types.jsonc");
        //read from existing
        try { parseFishTypes(new FileReader(file)); }
        catch(FileNotFoundException e) {
            //generate from default values
            parseFishTypes(new StringReader(defaultConfigValues));

            //create new file
            Files.createParentDirs(file);
            if(file.createNewFile()) {
                final FileWriter writer = new FileWriter(file);
                writer.write(defaultConfigValues);
                writer.close();
            }
        }

        //allow other mods to add their fish types
        ConfigUtils.parseFromMods("subaquatic/tropical_fish_types.jsonc", SubaquaticTropicalFishConfig::parseFishTypes);
    }

    static void parseFishTypes(@Nonnull Reader reader) {
        new JsonParser().parse(reader).getAsJsonArray().forEach(element -> {
            if(element.isJsonObject()) {
                final TropicalFishData data = getTropicalFishData(element.getAsJsonObject());
                if(data != null) DEFAULT_TYPES.add(data.serialize());
            }
        });
    }

    @Nullable
    public static TropicalFishData getTropicalFishData(@Nonnull JsonObject json) {
        if(json.has("primaryShape") && json.has("secondaryShape") && json.has("primaryColor") && json.has("secondaryColor")) {
            return new TropicalFishData(
                    json.get("primaryShape").getAsInt(),
                    getColor(json.get("primaryColor")),
                    json.get("secondaryShape").getAsInt(),
                    getColor(json.get("secondaryColor")));
        }

        return null;
    }

    @Nonnull
    public static EnumDyeColor getColor(@Nonnull JsonElement json) {
        if(json.isJsonPrimitive()) {
            for(EnumDyeColor color : EnumDyeColor.values())
                if(color.getName().equals(json.getAsString()))
                    return color;

            return EnumDyeColor.byMetadata(json.getAsInt());
        }

        return EnumDyeColor.BLACK;
    }

    @Nonnull
    static final String defaultConfigValues =
            "//each shape corresponds to a type, the following table represents the key:\n" +
            "//any shapes can be added (not just the ones in the table), though you'll have to add textures & translations yourself via a resourcepack\n" +
            "//[0, 0] = KOB\n" +
            "//[0, 1] = SUNSTREAK\n" +
            "//[0, 2] = SNOOPER\n" +
            "//[0, 3] = DASHER\n" +
            "//[0, 4] = BRINELY\n" +
            "//[0, 5] = SPOTTY\n" +
            "//[1, 0] = FLOPPER\n" +
            "//[1, 1] = STRIPEY\n" +
            "//[1, 2] = GLITTER\n" +
            "//[1, 3] = BLOCKFISH\n" +
            "//[1, 4] = BETTY\n" +
            "//[1, 5] = CLAYFISH\n" +
            "[\n" +
            "    {\n" +
            "        //stripey\n" +
            "        \"primaryShape\":1,\n" +
            "        \"primaryColor\":\"orange\",\n" +
            "        \"secondaryShape\":1,\n" +
            "        \"secondaryColor\":\"gray\"\n" +
            "    },\n" +
            "    {\n" +
            "        //flopper\n" +
            "        \"primaryShape\":1,\n" +
            "        \"primaryColor\":\"gray\",\n" +
            "        \"secondaryShape\":0,\n" +
            "        \"secondaryColor\":\"gray\"\n" +
            "    },\n" +
            "    {\n" +
            "        //flopper\n" +
            "        \"primaryShape\":1,\n" +
            "        \"primaryColor\":\"gray\",\n" +
            "        \"secondaryShape\":0,\n" +
            "        \"secondaryColor\":\"blue\"\n" +
            "    },\n" +
            "    {\n" +
            "        //clayfish\n" +
            "        \"primaryShape\":1,\n" +
            "        \"primaryColor\":\"white\",\n" +
            "        \"secondaryShape\":5,\n" +
            "        \"secondaryColor\":\"gray\"\n" +
            "    },\n" +
            "    {\n" +
            "        //sunstreak\n" +
            "        \"primaryShape\":1,\n" +
            "        \"primaryColor\":\"gray\",\n" +
            "        \"secondaryShape\":0,\n" +
            "        \"secondaryColor\":\"gray\"\n" +
            "    },\n" +
            "    {\n" +
            "        //kob\n" +
            "        \"primaryShape\":0,\n" +
            "        \"primaryColor\":\"orange\",\n" +
            "        \"secondaryShape\":0,\n" +
            "        \"secondaryColor\":\"white\"\n" +
            "    },\n" +
            "    {\n" +
            "        //spotty\n" +
            "        \"primaryShape\":0,\n" +
            "        \"primaryColor\":\"pink\",\n" +
            "        \"secondaryShape\":5,\n" +
            "        \"secondaryColor\":\"light_blue\"\n" +
            "    },\n" +
            "    {\n" +
            "        //blockfish\n" +
            "        \"primaryShape\":1,\n" +
            "        \"primaryColor\":\"purple\",\n" +
            "        \"secondaryShape\":3,\n" +
            "        \"secondaryColor\":\"yellow\"\n" +
            "    },\n" +
            "    {\n" +
            "        //clayfish\n" +
            "        \"primaryShape\":1,\n" +
            "        \"primaryColor\":\"white\",\n" +
            "        \"secondaryShape\":5,\n" +
            "        \"secondaryColor\":\"red\"\n" +
            "    },\n" +
            "    {\n" +
            "        //spotty\n" +
            "        \"primaryShape\":0,\n" +
            "        \"primaryColor\":\"white\",\n" +
            "        \"secondaryShape\":5,\n" +
            "        \"secondaryColor\":\"yellow\"\n" +
            "    },\n" +
            "    {\n" +
            "        //glitter\n" +
            "        \"primaryShape\":1,\n" +
            "        \"primaryColor\":\"white\",\n" +
            "        \"secondaryShape\":2,\n" +
            "        \"secondaryColor\":\"gray\"\n" +
            "    },\n" +
            "    {\n" +
            "        //clayfish\n" +
            "        \"primaryShape\":1,\n" +
            "        \"primaryColor\":\"white\",\n" +
            "        \"secondaryShape\":5,\n" +
            "        \"secondaryColor\":\"orange\"\n" +
            "    },\n" +
            "    {\n" +
            "        //dasher\n" +
            "        \"primaryShape\":1,\n" +
            "        \"primaryColor\":\"cyan\",\n" +
            "        \"secondaryShape\":3,\n" +
            "        \"secondaryColor\":\"pink\"\n" +
            "    },\n" +
            "    {\n" +
            "        //brinely\n" +
            "        \"primaryShape\":0,\n" +
            "        \"primaryColor\":\"lime\",\n" +
            "        \"secondaryShape\":4,\n" +
            "        \"secondaryColor\":\"light_blue\"\n" +
            "    },\n" +
            "    {\n" +
            "        //betty\n" +
            "        \"primaryShape\":1,\n" +
            "        \"primaryColor\":\"red\",\n" +
            "        \"secondaryShape\":4,\n" +
            "        \"secondaryColor\":\"white\"\n" +
            "    },\n" +
            "    {\n" +
            "        //snooper\n" +
            "        \"primaryShape\":0,\n" +
            "        \"primaryColor\":\"gray\",\n" +
            "        \"secondaryShape\":2,\n" +
            "        \"secondaryColor\":\"red\"\n" +
            "    },\n" +
            "    {\n" +
            "        //blockfish\n" +
            "        \"primaryShape\":1,\n" +
            "        \"primaryColor\":\"red\",\n" +
            "        \"secondaryShape\":3,\n" +
            "        \"secondaryColor\":\"white\"\n" +
            "    },\n" +
            "    {\n" +
            "        //flopper\n" +
            "        \"primaryShape\":1,\n" +
            "        \"primaryColor\":\"white\",\n" +
            "        \"secondaryShape\":0,\n" +
            "        \"secondaryColor\":\"yellow\"\n" +
            "    },\n" +
            "    {\n" +
            "        //kob\n" +
            "        \"primaryShape\":0,\n" +
            "        \"primaryColor\":\"red\",\n" +
            "        \"secondaryShape\":0,\n" +
            "        \"secondaryColor\":\"white\"\n" +
            "    },\n" +
            "    {\n" +
            "        //sunstreak\n" +
            "        \"primaryShape\":0,\n" +
            "        \"primaryColor\":\"gray\",\n" +
            "        \"secondaryShape\":1,\n" +
            "        \"secondaryColor\":\"white\"\n" +
            "    },\n" +
            "    {\n" +
            "        //dasher\n" +
            "        \"primaryShape\":0,\n" +
            "        \"primaryColor\":\"cyan\",\n" +
            "        \"secondaryShape\":3,\n" +
            "        \"secondaryColor\":\"yellow\"\n" +
            "    },\n" +
            "    {\n" +
            "        //flopper\n" +
            "        \"primaryShape\":1,\n" +
            "        \"primaryColor\":\"yellow\",\n" +
            "        \"secondaryShape\":0,\n" +
            "        \"secondaryColor\":\"yellow\"\n" +
            "    }\n" +
            "]";
}
