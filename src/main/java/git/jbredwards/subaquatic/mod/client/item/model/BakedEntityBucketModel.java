/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client.item.model;

import com.google.common.collect.ImmutableList;
import git.jbredwards.subaquatic.api.entity.bucketable.BucketableEntityHandler;
import git.jbredwards.subaquatic.api.entity.bucketable.BucketableEntityRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.*;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public class BakedEntityBucketModel extends BakedModelWrapper<IBakedModel>
{
    @Nonnull
    protected static final Map<ResourceLocation, IBakedModel> OVERLAY_MODELS = new HashMap<>();
    public static void clearQuadsCache() {
        OVERLAY_MODELS.clear();
        ModelEntityBucketOverlay.BUCKET_OFFSETS.clear();
    }

    @Nullable
    protected final IBakedModel entityData;
    public BakedEntityBucketModel(@Nonnull IBakedModel originalModel) { this(originalModel, null); }
    public BakedEntityBucketModel(@Nonnull IBakedModel originalModel, @Nullable final IBakedModel entityDataIn) {
        super(originalModel);
        entityData = entityDataIn;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if(entityData == null || side != null) return super.getQuads(state, side, rand);
        final ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

        builder.addAll(super.getQuads(state, null, rand));
        builder.addAll(entityData.getQuads(state, null, rand));
        return builder.build();
    }

    @Nonnull
    @Override
    public ItemOverrideList getOverrides() {
        return new ItemOverrideList(Collections.emptyList()) {
            @Nonnull
            @Override
            public IBakedModel handleItemState(@Nonnull IBakedModel originalModelIn, @Nonnull ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
                @Nonnull final IBakedModel bucketOverride = originalModel.getOverrides().handleItemState(originalModel, stack, world, entity);
                @Nullable final BucketableEntityHandler<?> handler = BucketableEntityRegistry.getHandler(stack);
                if(handler == null) return bucketOverride;

                @Nonnull final IBakedModel bucketOverlay = OVERLAY_MODELS.computeIfAbsent(handler.overlayModel(), key -> {
                    @Nonnull final IModel model = ModelLoaderRegistry.getModelOrMissing(key);
                    return model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM, ModelLoader.defaultTextureGetter());
                });

                return new BakedEntityBucketModel(bucketOverride, bucketOverlay.getOverrides().handleItemState(bucketOverlay, stack, world, entity));
            }
        };
    }

    @Nonnull
    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(@Nonnull ItemCameraTransforms.TransformType cameraTransformType) {
        final Pair<? extends IBakedModel, Matrix4f> oldPerspective = super.handlePerspective(cameraTransformType);
        return Pair.of(new BakedEntityBucketModel(oldPerspective.getKey(), entityData), oldPerspective.getValue());
    }

    @Nonnull
    @Deprecated
    public static List<BakedQuad> getQuadsForSprite(@Nonnull ResourceLocation texture, int tintIndex, boolean flipped) {
        return new ModelEntityBucketOverlay.CacheKey(texture, 0, flipped, OptionalInt.empty()).get();
    }
}
