/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client.item.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import git.jbredwards.ocean_api.api.BucketableEntityBehavior;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.client.texture.MaskTextureAtlasSprite;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticTropicalFishConfig;
import git.jbredwards.subaquatic.mod.common.entity.util.TropicalFishData;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BakedItemModel;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public final class ModelTropicalFishBucketOverlay extends ModelFishBucketOverlay
{
    @Nonnull public static final ResourceLocation MISSING = new ResourceLocation(Subaquatic.MODID, "items/fish_bucket_overlays/missing");
    @Nonnull public static final ModelTropicalFishBucketOverlay INSTANCE = new ModelTropicalFishBucketOverlay();

    private ModelTropicalFishBucketOverlay() { super(ImmutableList.of(MISSING)); /* Sprites are automatically registered below. */ }
    public static void registerSprites(@Nonnull final TextureMap map) {
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
    }

    @Nonnull
    @Override
    public IModel process(@Nonnull final ImmutableMap<String, String> customData) {
        return this;
    }

    @Nonnull
    @Override
    protected ItemOverrideList getOverrides(@Nonnull final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms) {
        return new ItemOverrideList(ImmutableList.of()) {
            @Nonnull
            @Override
            public IBakedModel handleItemState(@Nonnull final IBakedModel originalModel, @Nonnull final ItemStack stack, @Nullable final World world, @Nullable final EntityLivingBase entity) {
                if(stack.isEmpty()) return originalModel;
                @Nullable final NBTTagCompound root = stack.getSubCompound(BucketableEntityBehavior.Util.NBT_KEY);
                @Nonnull final CacheKeyBuilder builder = new CacheKeyBuilder(stack, true, root);

                // Change textures based on tropical fish type.
                @Nullable final TropicalFishData fishData = root == null || !root.hasKey("Variant", Constants.NBT.TAG_ANY_NUMERIC) ? null : TropicalFishData.deserialize(root.getInteger("Variant"));
                @Nonnull final ImmutableList<ResourceLocation> layers;
                if(fishData == null) layers = textures;
                else {
                    @Nonnull final String texture = "items/fish_bucket_overlays/tropical_" + fishData.primaryShape + "_pattern_" + fishData.secondaryShape;
                    layers = ImmutableList.of(new ResourceLocation(Subaquatic.MODID, texture + "_base"), new ResourceLocation(Subaquatic.MODID, texture));
                }

                return new BakedItemModel(builder.apply(layers), originalModel.getParticleTexture(), transforms, this, false);
            }
        };
    }

    public enum Loader implements ICustomModelLoader
    {
        INSTANCE;

        @Override
        public void onResourceManagerReload(@Nonnull final IResourceManager resourceManager) {}

        @Override
        public boolean accepts(@Nonnull final ResourceLocation modelLocation) {
            return modelLocation.getNamespace().equals(Subaquatic.MODID) && modelLocation.getPath().endsWith("builtin/tropical_fish_bucket_overlay");
        }

        @Nonnull
        @Override
        public IModel loadModel(@Nonnull final ResourceLocation modelLocation) { return ModelTropicalFishBucketOverlay.INSTANCE; }
    }
}
