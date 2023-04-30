package git.jbredwards.subaquatic.mod.common.config.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 *
 * @author jbred
 *
 */
public final class ConfigUtils
{
    /**
     * Parses the config file in each mod, if the mod has one.
     */
    public static void parseFromMods(@Nonnull String fileName, @Nonnull Consumer<InputStreamReader> fileParser) {
        for(String modid : Loader.instance().getIndexedModList().keySet()) {
            final @Nullable InputStream stream = Loader.class.getResourceAsStream(String.format("/assets/%s/%s", modid, fileName));
            if(stream != null) fileParser.accept(new InputStreamReader(stream));
        }
    }

    /**
     * Reads the biomes & biome tags from the provided config and stores them in the map.
     */
    public static void readPerBiomeRarity(@Nonnull String[] perBiomeRarityConfig, @Nonnull Object2IntMap<Biome> perBiomeRarity) {
        perBiomeRarity.clear();
        Arrays.stream(perBiomeRarityConfig).map(cfg -> new JsonParser().parse(cfg).getAsJsonObject()).forEach(json -> {
            final int rarity = JsonUtils.getInt(json, "Value");

            JsonUtils.getJsonArray(json, "Biomes", new JsonArray()).forEach(biomeName -> {
                final Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(JsonUtils.getString(biomeName, "Biome")));
                if(biome != null) perBiomeRarity.put(biome, rarity);
            });

            JsonUtils.getJsonArray(json, "BiomeTags", new JsonArray()).forEach(biomeTagName -> {
                final BiomeDictionary.Type type = BiomeDictionary.Type.getType(JsonUtils.getString(biomeTagName, "BiomeTag"));
                BiomeDictionary.getBiomes(type).forEach(biome -> perBiomeRarity.put(biome, rarity));
            });
        });
    }
}
