package git.jbredwards.subaquatic.mod.common.config;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 *
 * @author jbred
 *
 */
public final class SubaquaticChestBoatConfig
{
    @Nonnull
    private static final Map<Item, ResourceLocation> BOAT_TEXTURE_GETTER = new HashMap<>();
    public static void forEach(@Nonnull BiConsumer<Item, ResourceLocation> action) { BOAT_TEXTURE_GETTER.forEach(action); }

    @Nullable
    public static Pair<Item, ResourceLocation> getTypeFrom(@Nonnull Item item) {
        return BOAT_TEXTURE_GETTER.containsKey(item) ? Pair.of(item, BOAT_TEXTURE_GETTER.get(item)) : null;
    }

    @Nullable
    public static Pair<Item, ResourceLocation> getTypeFrom(@Nonnull String itemName) {
        final Item item = Item.getByNameOrId(itemName);
        return item != null ? getTypeFrom(item) : null;
    }
}
