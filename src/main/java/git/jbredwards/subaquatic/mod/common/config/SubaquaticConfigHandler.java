package git.jbredwards.subaquatic.mod.common.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import git.jbredwards.subaquatic.mod.Subaquatic;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.ToIntFunction;

/**
 *
 * @author jbred
 *
 */
@Config(modid = Subaquatic.MODID)
@Mod.EventBusSubscriber(modid = Subaquatic.MODID)
public final class SubaquaticConfigHandler
{
    @Config.SlidingOption
    @Config.RangeInt(min = 0, max = 15)
    @Config.LangKey("config.subaquatic.biomeColorBlendRadius")
    public static int biomeColorBlendRadius = 3;

    @Config.Ignore
    @Nonnull public static final Object2IntMap<Biome> FOG_COLORS = new Object2IntOpenHashMap<>(), SURFACE_COLORS = new Object2IntOpenHashMap<>();
    @Nonnull static final Map<Block, BubbleColumnPredicate> BUBBLE_COLUMN_PULL = new HashMap<>(), BUBBLE_COLUMN_PUSH = new HashMap<>();

    @Nullable
    public static BubbleColumnPredicate getBubbleColumnConditions(@Nonnull Block soil, boolean isPull) {
        return (isPull ? BUBBLE_COLUMN_PULL : BUBBLE_COLUMN_PUSH).get(soil);
    }

    public static float[] getFogColorAt(@Nonnull IBlockAccess worldIn, @Nonnull BlockPos posIn, @Nonnull ToIntFunction<Biome> fallback) {
        return new Color(BiomeColorHelper.getColorAtPos(worldIn, posIn,
                (biome, pos) -> FOG_COLORS.getOrDefault(biome, fallback.applyAsInt(biome))))
                .getColorComponents(new float[3]);
    }

    public static void init() {
        FOG_COLORS.clear();
        SURFACE_COLORS.clear();
        for(String colors : waterColors) {
            final JsonElement colorsJson = new JsonParser().parse(colors);
            if(colorsJson.isJsonObject()) {
                final JsonObject json = colorsJson.getAsJsonObject();
                if(json.has("Biome")) {
                    final @Nullable Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(json.get("Biome").getAsString()));
                    if(biome != null) {
                        if(json.has("Surface")) SURFACE_COLORS.put(biome, json.get("Surface").getAsInt());
                        if(json.has("Fog")) FOG_COLORS.put(biome, json.get("Fog").getAsInt());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    static void syncConfig(@Nonnull ConfigChangedEvent.OnConfigChangedEvent event) {
        if(Subaquatic.MODID.equals(event.getModID())) {
            init();
            ConfigManager.sync(Subaquatic.MODID, Config.Type.INSTANCE);
        }
    }

    //TODO: this exists for testing purposes and will be removed once bubble columns are added
    static {
        BUBBLE_COLUMN_PULL.put(Blocks.MAGMA, new BubbleColumnPredicate(Blocks.MAGMA, new int[0], new HashSet<>()));
        BUBBLE_COLUMN_PUSH.put(Blocks.SOUL_SAND, new BubbleColumnPredicate(Blocks.SOUL_SAND, new int[0], new HashSet<>()));
    }

    @Config.LangKey("config.subaquatic.waterColors")
    @Nonnull public static String[] waterColors = new String[] {
            "{Biome:\"plains\",Surface:4501493,Fog:4501493}",
            "{Biome:\"desert\",Surface:3319192,Fog:3319192}",
            "{Biome:\"extreme_hills\",Surface:31735,Fog:31735}",
            "{Biome:\"forest\",Surface:2004978,Fog:2004978}",
            "{Biome:\"mutated_plains\",Surface:4501493,Fog:4501493}",
            "{Biome:\"mutated_forest\",Surface:2139084,Fog:2139084}",
            "{Biome:\"taiga\",Surface:2650242,Fog:2650242}",
            "{Biome:\"taiga_hills\",Surface:1993602,Fog:1993602}",
            "{Biome:\"swampland\",Surface:6388580,Fog:5006681}",
            "{Biome:\"mutated_swampland\",Surface:6388580,Fog:5005654}",
            "{Biome:\"river\",Surface:34047,Fog:34047}",
            "{Biome:\"hell\",Surface:9460055,Fog:9460055}",
            "{Biome:\"sky\",Surface:6443678,Fog:6443678}",
            "{Biome:\"frozen_river\",Surface:1594256,Fog:1594256}",
            "{Biome:\"ice_flats\",Surface:1332635,Fog:1332635}",
            "{Biome:\"mutated_ice_flats\",Surface:1332635,Fog:1332635}",
            "{Biome:\"ice_mountains\",Surface:1136295,Fog:1136295}",
            "{Biome:\"mushroom_island\",Surface:9079191,Fog:9079191}",
            "{Biome:\"mushroom_island_shore\",Surface:8487315,Fog:8487315}",
            "{Biome:\"beaches\",Surface:1408171,Fog:1408171}",
            "{Biome:\"desert_hills\",Surface:1735329,Fog:1735329}",
            "{Biome:\"forest_hills\",Surface:355281,Fog:355281}",
            "{Biome:\"taiga_hills\",Surface:2319747,Fog:2319747}",
            "{Biome:\"smaller_extreme_hills\",Surface:285909,Fog:285909}",
            "{Biome:\"jungle\",Surface:1352389,Fog:1352389}",
            "{Biome:\"jungle_hills\",Surface:1810136,Fog:1810136}",
            "{Biome:\"mutated_jungle\",Surface:1810136,Fog:1810136}",
            "{Biome:\"jungle_edge\",Surface:887523,Fog:887523}",
            "{Biome:\"mutated_jungle_edge\",Surface:887523,Fog:887523}",
            "{Biome:\"stone_beach\",Surface:878523,Fog:878523}",
            "{Biome:\"cold_beach\",Surface:1336229,Fog:1336229}",
            "{Biome:\"birch_forest\",Surface:423886,Fog:423886}",
            "{Biome:\"mutated_birch_forest\",Surface:423886,Fog:423886}",
            "{Biome:\"birch_forest_hills\",Surface:685252,Fog:685252}",
            "{Biome:\"mutated_birch_forest_hills\",Surface:685252,Fog:685252}",
            "{Biome:\"roofed_forest\",Surface:3894481,Fog:3894481}",
            "{Biome:\"mutated_roofed_forest\",Surface:3894481,Fog:3894481}",
            "{Biome:\"taiga_cold\",Surface:2121347,Fog:2121347}",
            "{Biome:\"mutated_taiga_cold\",Surface:2121347,Fog:2121347}",
            "{Biome:\"taiga_cold_hills\",Surface:2382712,Fog:2382712}",
            "{Biome:\"redwood_taiga\",Surface:2977143,Fog:2977143}",
            "{Biome:\"mutated_redwood_taiga\",Surface:2977143,Fog:2977143}",
            "{Biome:\"mutated_redwood_taiga_hills\",Surface:2977143,Fog:2977143}",
            "{Biome:\"redwood_taiga_hills\",Surface:2646904,Fog:2646904}",
            "{Biome:\"extreme_hills_with_trees\",Surface:943019,Fog:943019}",
            "{Biome:\"mutated_extreme_hills\",Surface:943019,Fog:943019}",
            "{Biome:\"mutated_extreme_hills_with_trees\",Surface:943019,Fog:943019}",
            "{Biome:\"savanna\",Surface:2919324,Fog:2919324}",
            "{Biome:\"savanna_rock\",Surface:2461864,Fog:2461864}",
            "{Biome:\"mutated_savanna\",Surface:2461864,Fog:2461864}",
            "{Biome:\"mutated_savanna_rock\",Surface:2461864,Fog:2461864}",
            "{Biome:\"mesa\",Surface:5144449,Fog:5144449}",
            "{Biome:\"mutated_mesa\",Surface:4816793,Fog:4816793}",
            "{Biome:\"mesa_rock\",Surface:4816793,Fog:4816793}",
            "{Biome:\"mesa_clear_rock\",Surface:5603486,Fog:5603486}",
            "{Biome:\"mutated_mesa_rock\",Surface:5603486,Fog:5603486}",
            "{Biome:\"mutated_mesa_clear_rock\",Surface:5603486,Fog:5603486}",
            "{Biome:\"ocean\",Surface:1542100,Fog:1140144}",
            "{Biome:\"deep_ocean\",Surface:1542100,Fog:1336229}",
            "{Biome:\"frozen_ocean\",Surface:2453685,Fog:1526149}",
            "{Biome:\"mutated_desert\",Surface:2004978,Fog:2004978}",
            "{Biome:\"mutated_taiga\",Surface:2650242,Fog:2650242}"
    };
}
