/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.client.item.model.BakedEntityBucketModel;
import git.jbredwards.subaquatic.mod.common.capability.IEntityBucket;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

/**
 * Every registered (bucketable) entity should have a dedicated handler
 * @author jbred
 *
 */
public abstract class AbstractEntityBucketHandler implements INBTSerializable<NBTTagCompound>
{
    @Nonnull
    public static final Map<String, Supplier<AbstractEntityBucketHandler>> BUCKET_HANDLERS = new HashMap<>();
    public NBTTagCompound entityNbt;

    @Nonnull
    public abstract EntityEntry getEntityEntry();

    @Nullable
    public static AbstractEntityBucketHandler createFromNBT(@Nonnull NBTTagCompound nbt) {
        if(!nbt.hasKey("Entity", Constants.NBT.TAG_STRING)) return null;

        //handle basic info
        final AbstractEntityBucketHandler data = BUCKET_HANDLERS.get(nbt.getString("Entity")).get();
        data.deserializeNBT(nbt);
        return data;
    }

    @Nonnull
    @Override
    public final NBTTagCompound serializeNBT() {
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("Entity", getEntityEntry().delegate.name().toString());
        nbt.setTag("FishNBT", entityNbt);

        writeToNBT(nbt);
        return nbt;
    }

    @Override
    public final void deserializeNBT(@Nonnull NBTTagCompound nbt) {
        entityNbt = nbt.getCompoundTag("FishNBT");
        readFromNBT(nbt);
    }

    //handle additional info
    protected void writeToNBT(@Nonnull NBTTagCompound nbt) {}
    protected void readFromNBT(@Nonnull NBTTagCompound nbt) {}

    @Nonnull
    public ItemStack createNewStack(@Nonnull ItemStack bucket) {
        return applyEntry(bucket, Objects.requireNonNull(IEntityBucket.get(bucket)));
    }

    public void getSubTypes(@Nonnull List<ItemStack> items, @Nonnull ItemStack parentBucket) {
        items.add(createNewStack(parentBucket.copy()));
    }

    @Nonnull
    protected final ItemStack applyEntry(@Nonnull ItemStack bucket, @Nonnull IEntityBucket cap) {
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("id", getEntityEntry().delegate.name().toString());
        entityNbt = nbt;

        cap.setHandler(this);
        return bucket;
    }

    @Nonnull
    protected ResourceLocation getSpriteForRender() { return new ResourceLocation(Subaquatic.MODID, "items/fish_bucket_overlays/missing"); }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public List<BakedQuad> getRenderQuads(boolean flipped) {
        return Collections.unmodifiableList(BakedEntityBucketModel.getQuadsForSprite(getSpriteForRender(), 3, flipped));
    }

    @SideOnly(Side.CLIENT)
    public void registerSprites(@Nonnull TextureMap map) { map.registerSprite(getSpriteForRender()); }

    @SideOnly(Side.CLIENT)
    public void handleTooltip(@Nonnull List<String> tooltip, @Nonnull ItemStack bucket, @Nonnull ITooltipFlag flag) {
        tooltip.add(1, I18n.format("tooltip.subaquatic.fish_bucket", I18n.format("entity." + getEntityEntry().getName() + ".name")));
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(@Nonnull ItemStack bucket, int tintIndex) { return -1; }
}
