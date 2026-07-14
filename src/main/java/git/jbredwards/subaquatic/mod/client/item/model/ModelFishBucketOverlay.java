/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client.item.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import git.jbredwards.ocean_api.mod.client.model.ModelBucketableEntityOverlay;
import git.jbredwards.subaquatic.mod.Subaquatic;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public class ModelFishBucketOverlay extends ModelBucketableEntityOverlay
{
    @Nonnull
    public static final ModelFishBucketOverlay INSTANCE = new ModelFishBucketOverlay(ImmutableList.of());
    protected ModelFishBucketOverlay(@Nonnull final ImmutableList<ResourceLocation> texturesIn) { super(texturesIn); }

    @Nonnull
    @Override
    public IModel process(@Nonnull final ImmutableMap<String, String> customData) {
        return new ModelFishBucketOverlay((ImmutableList<ResourceLocation>)super.process(customData).getTextures());
    }

    @SideOnly(Side.CLIENT)
    @Override
    public float getYOffset(@Nonnull final Item bucket) {
        // Add a hardcoded offset for the Subaquatic fish bucket textures, so I don't have to change them.
        return super.getYOffset(bucket) - 0.5f;
    }

    public enum Loader implements ICustomModelLoader
    {
        INSTANCE;

        @Override
        public void onResourceManagerReload(@Nonnull final IResourceManager resourceManager) {}

        @Override
        public boolean accepts(@Nonnull final ResourceLocation modelLocation) {
            return modelLocation.getNamespace().equals(Subaquatic.MODID) && modelLocation.getPath().endsWith("builtin/fish_bucket_overlay");
        }

        @Nonnull
        @Override
        public IModel loadModel(@Nonnull final ResourceLocation modelLocation) { return ModelFishBucketOverlay.INSTANCE; }
    }
}
