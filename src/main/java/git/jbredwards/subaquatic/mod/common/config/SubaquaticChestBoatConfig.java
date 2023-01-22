package git.jbredwards.subaquatic.mod.common.config;

import com.google.common.io.Files;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import git.jbredwards.subaquatic.mod.common.capability.util.BoatType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.util.function.Consumer;

/**
 *
 * @author jbred
 *
 */
public final class SubaquaticChestBoatConfig
{
    @Nonnull
    public static final Int2ObjectMap<BoatType> BOAT_TYPES = new Int2ObjectOpenHashMap<>();
    public static void forEach(@Nonnull Consumer<BoatType> action) {
        BOAT_TYPES.forEach((id, type) -> action.accept(type));
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void buildBoatTypes() throws IOException {
        final File file = new File("config/subaquatic", "boat_types.jsonc");
        //read from existing
        try { parseBoatTypes(new FileReader(file)); }
        catch(FileNotFoundException e) {
            //generate from default values
            parseBoatTypes(new StringReader(defaultConfigValues));

            //create new file
            Files.createParentDirs(file);
            if(file.createNewFile()) {
                final FileWriter writer = new FileWriter(file);
                writer.write(defaultConfigValues);
                writer.close();
            }
        }
    }

    static void parseBoatTypes(@Nonnull Reader reader) {
        final JsonObject configFile = new JsonParser().parse(reader).getAsJsonObject();
        configFile.entrySet().forEach(element -> {
            if(element.getValue().isJsonObject()) {
                final Item item = Item.getByNameOrId(element.getKey());
                if(item != null) {
                    final JsonObject configElement = element.getValue().getAsJsonObject();
                    final int meta = configElement.has("meta") ? Math.max(configElement.get("meta").getAsInt(), 0) : 0;

                    final BoatType type = new BoatType(item, meta);
                    if(FMLCommonHandler.instance().getSide().isClient() && configElement.has("texture")) {
                        final ResourceLocation texture = new ResourceLocation(configElement.get("texture").getAsString());
                        type.texture = new ResourceLocation(texture.getNamespace(), String.format("textures/%s.png", texture.getPath()));
                    }

                    BOAT_TYPES.put(getIndex(item, meta), type);
                }
            }
        });
    }

    @Nullable
    public static BoatType getTypeFrom(@Nonnull Item item, int meta) {
        return BOAT_TYPES.get(getIndex(item, meta));
    }

    @Nullable
    public static BoatType getTypeFrom(@Nonnull NBTTagCompound nbt) {
        final Item item = Item.getByNameOrId(nbt.getString("item"));
        return item != null ? getTypeFrom(item, nbt.getInteger("meta")) : null;
    }

    public static int getIndex(@Nonnull Item item, int meta) { return Item.getIdFromItem(item) << 16 | meta; }

    @Nonnull
    public static final String defaultConfigValues =
            "{\n" +
            "    //vanilla\n" +
            "    \"minecraft:boat\":{\"texture\":\"entity/boat/boat_oak\"},\n" +
            "    \"minecraft:spruce_boat\":{\"texture\":\"entity/boat/boat_spruce\"},\n" +
            "    \"minecraft:birch_boat\":{\"texture\":\"entity/boat/boat_birch\"},\n" +
            "    \"minecraft:jungle_boat\":{\"texture\":\"entity/boat/boat_jungle\"},\n" +
            "    \"minecraft:acacia_boat\":{\"texture\":\"entity/boat/boat_acacia\"},\n" +
            "    \"minecraft:dark_oak_boat\":{\"texture\":\"entity/boat/boat_darkoak\"}\n" +
            "}";
}
