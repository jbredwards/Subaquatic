package git.jbredwards.subaquatic.mod.common.capability.util;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 *
 * @author jbred
 *
 */
public class FishBucketData
{
    @Nonnull
    public static final Map<EntityEntry, Function<FishBucketData, TextureAtlasSprite>> OVERLAY_TEXTURES = new HashMap<>();

    @Nonnull
    public static final FishBucketData EMPTY = new FishBucketData();
    public final List<String> tooltip = new ArrayList<>();
    public NBTTagCompound fishNbt;
    public EntityEntry entity;

    @Nonnull
    public NBTTagCompound serializeNBT() {
        if(this == EMPTY) return new NBTTagCompound();
        final NBTTagCompound nbt = new NBTTagCompound();

        nbt.setString("Entity", entity.delegate.name().toString());
        nbt.setTag("FishNBT", fishNbt);

        if(!tooltip.isEmpty()) {
            final NBTTagList tooltipNbt = new NBTTagList();
            tooltip.forEach(str -> tooltipNbt.appendTag(new NBTTagString(str)));
            nbt.setTag("Tooltip", tooltipNbt);
        }

        return nbt;
    }

    @Nonnull
    public static FishBucketData deserializeNBT(@Nonnull NBTTagCompound nbt) {
        if(nbt.isEmpty()) return EMPTY;
        final FishBucketData data = new FishBucketData();

        data.entity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(nbt.getString("Entity")));
        if(data.entity == null) return EMPTY;

        data.fishNbt = nbt.getCompoundTag("FishNBT");
        if(nbt.hasKey("Tooltip", Constants.NBT.TAG_LIST)) {
            nbt.getTagList("Tooltip", Constants.NBT.TAG_STRING).forEach(nbtStr -> {
                if(nbtStr instanceof NBTTagString) data.tooltip.add(((NBTTagString)nbtStr).getString());
            });
        }

        return data;
    }
}
