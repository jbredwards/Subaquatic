package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.entity.datasync.DataSerializerGeneric;
import git.jbredwards.subaquatic.mod.common.entity.util.FrogData;
import git.jbredwards.subaquatic.mod.common.entity.util.TropicalFishData;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraftforge.registries.DataSerializerEntry;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.OptionalInt;

/**
 * stores all of this mod's data serializers
 * @author jbred
 *
 */
public final class SubaquaticDataSerializers
{
    // Init
    @Nonnull
    public static final List<DataSerializerEntry> INIT = new LinkedList<>();

    // Serializers
    @Nonnull
    public static final DataSerializer<TropicalFishData> TROPICAL_FISH_DATA = register("tropical_fish_data", new DataSerializerGeneric<>(
            (buf, value) -> buf.writeVarInt(value.serialize()),
            buf -> TropicalFishData.deserialize(buf.readVarInt())));

    @Nonnull
    public static final DataSerializer<OptionalInt> OPTIONAL_INT = register("optional_int", new DataSerializerGeneric<>(
            (buf, value) -> { buf.writeBoolean(value.isPresent()); if(value.isPresent()) buf.writeVarInt(value.getAsInt()); },
            buf -> buf.readBoolean() ? OptionalInt.of(buf.readVarInt()) : OptionalInt.empty()));

    @Nonnull
    public static final DataSerializer<FrogData> FROG_DATA = register("frog_data", new DataSerializerGeneric<>(
            (buf, value) -> buf.writeString(value.name),
            buf -> FrogData.getFromName(buf.readString(Short.MAX_VALUE))));

    // Registry
    @Nonnull
    public static <T extends DataSerializer<?>> T register(@Nonnull String name, @Nonnull T serializer) {
        INIT.add(new DataSerializerEntry(serializer).setRegistryName(Subaquatic.MODID, name));
        return serializer;
    }
}
