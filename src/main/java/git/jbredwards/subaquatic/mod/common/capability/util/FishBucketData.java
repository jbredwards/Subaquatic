package git.jbredwards.subaquatic.mod.common.capability.util;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.HashMap;
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
    @SideOnly(Side.CLIENT)
    public static final Map<EntityEntry, Function<FishBucketData, TextureAtlasSprite>> OVERLAY_TEXTURES = new HashMap<>();

    @Nonnull
    public static final FishBucketData EMPTY = new FishBucketData();
    public NBTTagCompound fishNbt;
    public EntityEntry entity;

    @Nonnull
    public NBTTagCompound serializeNBT() {
        if(this == EMPTY) return new NBTTagCompound();
        final NBTTagCompound nbt = new NBTTagCompound();

        nbt.setString("Entity", entity.delegate.name().toString());
        nbt.setTag("FishNBT", fishNbt);
        return nbt;
    }

    @Nonnull
    public static FishBucketData deserializeNBT(@Nonnull NBTTagCompound nbt) {
        if(nbt.isEmpty()) return EMPTY;
        final FishBucketData data = new FishBucketData();

        data.entity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(nbt.getString("Entity")));
        if(data.entity == null) return EMPTY;

        data.fishNbt = nbt.getCompoundTag("FishNBT");
        return data;
    }
}
