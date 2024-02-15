/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.compat.inspirations;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import knightminer.inspirations.library.util.TextureBlockUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public final class InspirationsModelCauldron implements IModel
{
    @Nonnull
    public static final InspirationsModelCauldron MODEL = new InspirationsModelCauldron(ModelBakery.MODEL_MISSING, TextureMap.LOCATION_MISSING_TEXTURE);

    @Nonnull
    final ResourceLocation modelLocation, modelTexture;
    private InspirationsModelCauldron(@Nonnull ResourceLocation modelLocationIn, @Nonnull ResourceLocation modelTextureIn) {
        modelLocation = modelLocationIn;
        modelTexture = modelTextureIn;
    }

    @Nonnull
    @Override
    public Collection<ResourceLocation> getTextures() { return ImmutableList.of(modelTexture); }

    @Nonnull
    @Override
    public IBakedModel bake(@Nonnull IModelState state, @Nonnull VertexFormat format, @Nonnull Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return new BakedModel(ModelLoaderRegistry.getModelOrLogError(modelLocation, "Couldn't load InspirationsModelCauldron dependency: " + modelLocation)
                .bake(state, format, bakedTextureGetter), bakedTextureGetter.apply(modelTexture), bakedTextureGetter);
    }

    @Nonnull
    @Override
    public IModel process(@Nonnull ImmutableMap<String, String> customData) {
        if(customData.containsKey("model")) {
            final JsonElement overlayModel = new JsonParser().parse(customData.get("model"));
            if(overlayModel.isJsonPrimitive() && overlayModel.getAsJsonPrimitive().isString()) {
                final JsonElement overlayTexture = new JsonParser().parse(customData.get("texture"));
                if(overlayTexture.isJsonPrimitive() && overlayTexture.getAsJsonPrimitive().isString()) {
                    return new InspirationsModelCauldron(
                            new ResourceLocation(overlayModel.getAsString()),
                            new ResourceLocation(overlayTexture.getAsString()));
                }
            }
        }

        return MODEL;
    }

    private static final class BakedModel extends BakedModelWrapper<IBakedModel>
    {
        @Nonnull final TextureAtlasSprite modelTexture;
        @Nonnull final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter;

        public BakedModel(@Nonnull IBakedModel originalModelIn, @Nonnull TextureAtlasSprite modelTextureIn, @Nonnull Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetterIn) {
            super(originalModelIn);
            modelTexture = modelTextureIn;
            bakedTextureGetter = bakedTextureGetterIn;
        }

        @Nonnull
        @Override
        public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
            if(state instanceof IExtendedBlockState) {
                final String textureStr = ((IExtendedBlockState)state).getValue(TextureBlockUtil.TEXTURE_PROP);
                final TextureAtlasSprite texture = textureStr == null ? modelTexture : bakedTextureGetter.apply(new ResourceLocation(textureStr));

                final ImmutableList.Builder<BakedQuad> quads = ImmutableList.builder();
                super.getQuads(state, side, rand).forEach(quad -> quads.add(new BakedQuadRetextured(quad, texture)));
                return quads.build();
            }

            return super.getQuads(state, side, rand);
        }
    }

    public enum Loader implements ICustomModelLoader
    {
        INSTANCE;

        @Override
        public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {}

        @Override
        public boolean accepts(@Nonnull ResourceLocation modelLocation) {
            return modelLocation.getNamespace().equals("inspirations") && modelLocation.getPath().endsWith("builtin/cauldron_wrapper");
        }

        @Nonnull
        @Override
        public IModel loadModel(@Nonnull ResourceLocation modelLocation) { return MODEL; }
    }
}
