/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client.item.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import git.jbredwards.subaquatic.mod.Subaquatic;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BakedItemModel;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableInt;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public final class ModelFishBucketOverlay extends ModelEntityBucketOverlay
{
    @Nonnull
    public static final ModelFishBucketOverlay INSTANCE = new ModelFishBucketOverlay(ImmutableList.of());
    private ModelFishBucketOverlay(@Nonnull final ImmutableList<ResourceLocation> texturesIn) { super(texturesIn); }

    @Nonnull
    @Override
    public IBakedModel bake(@Nonnull final IModelState state, @Nonnull final VertexFormat format, @Nonnull final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        @Nonnull final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms = PerspectiveMapWrapper.getTransforms(state);
        return new BakedItemModel(ImmutableList.of(), bakedTextureGetter.apply(TextureMap.LOCATION_MISSING_TEXTURE), transforms, new ItemOverrideList(ImmutableList.of()) {
            @Nonnull
            @Override
            public IBakedModel handleItemState(@Nonnull final IBakedModel originalModel, @Nonnull final ItemStack stack, @Nullable final World world, @Nullable final EntityLivingBase entity) {
                return stack.isEmpty() ? originalModel : new BakedItemModel(new CacheKeyBuilder(stack).apply(textures), originalModel.getParticleTexture(), transforms, this, false);
            }
        }, false);
    }

    @Nonnull
    @Override
    public IModel process(@Nonnull final ImmutableMap<String, String> customData) {
        if(!customData.containsKey("textures")) return this;

        @Nonnull final MutableInt index = new MutableInt();
        @Nonnull final ImmutableList.Builder<ResourceLocation> textures = ImmutableList.builder();
        for(@Nonnull final JsonElement texture : JsonUtils.getJsonArray(new JsonParser().parse(customData.get("textures")), "textures")) {
            textures.add(new ResourceLocation(JsonUtils.getString(texture, "textures[" + index.getAndIncrement() + ']')));
        }

        return new ModelFishBucketOverlay(textures.build());
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
