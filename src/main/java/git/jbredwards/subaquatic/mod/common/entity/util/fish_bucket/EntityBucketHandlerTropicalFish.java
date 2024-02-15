/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket;

import com.google.common.collect.ImmutableList;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.client.item.model.BakedEntityBucketModel;
import git.jbredwards.subaquatic.mod.client.texture.MaskTextureAtlasSprite;
import git.jbredwards.subaquatic.mod.common.capability.IEntityBucket;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticTropicalFishConfig;
import git.jbredwards.subaquatic.mod.common.entity.util.TropicalFishData;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticEntities;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 *
 * @author jbred
 *
 */
public class EntityBucketHandlerTropicalFish extends AbstractEntityBucketHandler
{
    @Nullable
    public TropicalFishData fishData;

    @Nonnull
    @Override
    public EntityEntry getEntityEntry() { return SubaquaticEntities.TROPICAL_FISH; }

    @Override
    protected void writeToNBT(@Nonnull NBTTagCompound nbt) {
        if(fishData != null) nbt.setInteger("Variant", fishData.serialize());
    }

    @Override
    protected void readFromNBT(@Nonnull NBTTagCompound nbt) {
        if(nbt.hasKey("Variant", Constants.NBT.TAG_INT)) fishData = TropicalFishData.deserialize(nbt.getInteger("Variant"));
    }

    @Override
    public void getSubTypes(@Nonnull List<ItemStack> items, @Nonnull ItemStack parentBucket) {
        SubaquaticTropicalFishConfig.DEFAULT_TYPES.forEach(data -> {
            final EntityBucketHandlerTropicalFish handler = new EntityBucketHandlerTropicalFish();
            handler.fishData = data;

            final ItemStack bucket = parentBucket.copy();
            items.add(handler.applyEntry(bucket, Objects.requireNonNull(IEntityBucket.get(bucket))));
        });
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerSprites(@Nonnull TextureMap map) {
        final IntSet primaryShapes = new IntOpenHashSet(), secondaryShapes = new IntOpenHashSet();

        //these are coded to randomly spawn, ensure they always have a sprite
        primaryShapes.addAll(Arrays.asList(0, 1));
        secondaryShapes.addAll(Arrays.asList(0, 1, 2, 3, 4, 5));

        //account for any possible new types
        SubaquaticTropicalFishConfig.DEFAULT_TYPES.forEach(type -> {
            primaryShapes.add(type.primaryShape);
            secondaryShapes.add(type.secondaryShape);
        });

        //generate all sprites
        primaryShapes.forEach(primaryShape -> {
            final String base = "items/fish_bucket_overlays/tropical_" + primaryShape;
            final ResourceLocation baseLocation = new ResourceLocation(Subaquatic.MODID, base);
            secondaryShapes.forEach(secondaryShape -> {
                final ResourceLocation maskLocation = new ResourceLocation(Subaquatic.MODID, base + "_pattern_" + secondaryShape);
                map.setTextureEntry(new MaskTextureAtlasSprite(baseLocation, maskLocation));
            });
        });

        super.registerSprites(map);
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public List<BakedQuad> getRenderQuads(boolean flipped) {
        if(fishData != null) {
            final String texture = "items/fish_bucket_overlays/tropical_" + fishData.primaryShape + "_pattern_" + fishData.secondaryShape;
            final ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

            builder.addAll(BakedEntityBucketModel.getQuadsForSprite(new ResourceLocation(Subaquatic.MODID, texture + "_base"), 3, flipped));
            builder.addAll(BakedEntityBucketModel.getQuadsForSprite(new ResourceLocation(Subaquatic.MODID, texture), 4, flipped));
            return builder.build();
        }

        return super.getRenderQuads(flipped);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handleTooltip(@Nonnull List<String> tooltip, @Nonnull ItemStack bucket, @Nonnull ITooltipFlag flag) {
        if(fishData != null) {
            //for testing purposes
            /*if(flag.isAdvanced()) {
                tooltip.add(1, "Secondary Shape: " + fishData.secondaryShape);
                tooltip.add(1, "Primary Shape: " + fishData.primaryShape);
                tooltip.add(1, "Serialized: " + fishData.serialize());
            }*/

            if(fishData.hasTranslation(I18n::hasKey)) tooltip.add(1, fishData.getTranslatedName(I18n::format));
            else {
                final List<String> fallback = new LinkedList<>();
                fallback.add(fishData.getTranslatedShape(I18n::format));
                fallback.add(fishData.getTranslatedColor(I18n::format));

                tooltip.addAll(1, fallback);
            }
        }

        else tooltip.add(1, I18n.format("tooltip.subaquatic.fish_bucket.tropical_fish.missing"));
        super.handleTooltip(tooltip, bucket, flag);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(@Nonnull ItemStack bucket, int tintIndex) {
        if(fishData == null) return -1;
        switch(tintIndex) {
            case 3: return fishData.primaryColor.getColorValue();
            case 4: return fishData.secondaryColor.getColorValue();
            default: return -1;
        }
    }

    @Nonnull
    @Override
    public ItemStack createNewStack(@Nonnull ItemStack bucket) {
        final IEntityBucket cap = IEntityBucket.get(bucket);
        assert cap != null;
        fishData = TropicalFishData.DEFAULT;

        return applyEntry(bucket, cap);
    }

    @Nonnull
    public ItemStack createNewStackRandom(@Nonnull ItemStack bucket, @Nonnull Random random) {
        final IEntityBucket cap = IEntityBucket.get(bucket);
        assert cap != null;
        fishData = !SubaquaticTropicalFishConfig.DEFAULT_TYPES.isEmpty()
                ? SubaquaticTropicalFishConfig.DEFAULT_TYPES.get(random.nextInt(SubaquaticTropicalFishConfig.DEFAULT_TYPES.size()))
                : TropicalFishData.DEFAULT;

        return applyEntry(bucket, cap);
    }

    @Nonnull
    public ItemStack createNewStackTrueRandom(@Nonnull ItemStack bucket, @Nonnull Random random) {
        final IEntityBucket cap = IEntityBucket.get(bucket);
        assert cap != null;
        fishData = TropicalFishData.deserialize(random.nextInt(2) | random.nextInt(6) << 8 | random.nextInt(15) << 16 | random.nextInt(15) << 24);

        return applyEntry(bucket, cap);
    }
}
