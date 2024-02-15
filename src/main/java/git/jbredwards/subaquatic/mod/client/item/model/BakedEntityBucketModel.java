/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client.item.model;

import com.google.common.collect.ImmutableList;
import git.jbredwards.subaquatic.mod.common.capability.IEntityBucket;
import git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket.AbstractEntityBucketHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
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
    protected static final Map<ResourceLocation, List<BakedQuad>> OVERLAY_QUADS = new HashMap<>(), OVERLAY_QUADS_FLIPPED = new HashMap<>();
    public static void clearQuadsCache() { OVERLAY_QUADS.clear(); OVERLAY_QUADS_FLIPPED.clear(); }

    @Nullable
    protected final AbstractEntityBucketHandler entityData;
    protected final boolean flipped;

    public BakedEntityBucketModel(@Nonnull IBakedModel originalModel) { this(originalModel, null, false); }
    public BakedEntityBucketModel(@Nonnull IBakedModel originalModel, @Nullable AbstractEntityBucketHandler entityDataIn, boolean flippedIn) {
        super(originalModel);
        entityData = entityDataIn;
        flipped = flippedIn;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if(entityData == null || side != null) return super.getQuads(state, side, rand);
        final ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

        builder.addAll(super.getQuads(state, null, rand));
        builder.addAll(entityData.getRenderQuads(flipped));
        return builder.build();
    }

    @Nonnull
    public static List<BakedQuad> getQuadsForSprite(@Nonnull ResourceLocation texture, int tintIndex, boolean flipped) {
        return (flipped ? OVERLAY_QUADS_FLIPPED : OVERLAY_QUADS).computeIfAbsent(texture, entity -> ItemLayerModel.getQuadsForSprite(tintIndex,
                ModelLoader.defaultTextureGetter().apply(texture), DefaultVertexFormats.ITEM,
                Optional.of(new TRSRTransformation(new Vector3f(0, -0.00005f, -0.005f), flipped ? new Quat4f(0, 0, 1, 0) : null, new Vector3f(1, 1.0001f, 1.01f), null))
        ));
    }

    @Nonnull
    @Override
    public ItemOverrideList getOverrides() {
        return new ItemOverrideList(Collections.emptyList()) {
            @Nonnull
            @Override
            public IBakedModel handleItemState(@Nonnull IBakedModel originalModelIn, @Nonnull ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
                final IBakedModel bucketOverride = originalModel.getOverrides().handleItemState(originalModel, stack, world, entity);
                final IEntityBucket cap = IEntityBucket.get(stack);
                if(cap != null) {
                    final FluidStack fluid = FluidUtil.getFluidContained(stack);
                    return new BakedEntityBucketModel(bucketOverride, cap.getHandler(), fluid != null && fluid.getFluid().isLighterThanAir());
                }

                return bucketOverride;
            }
        };
    }

    @Nonnull
    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(@Nonnull ItemCameraTransforms.TransformType cameraTransformType) {
        final Pair<? extends IBakedModel, Matrix4f> oldPerspective = super.handlePerspective(cameraTransformType);
        return Pair.of(new BakedEntityBucketModel(oldPerspective.getKey(), entityData, flipped), oldPerspective.getValue());
    }
}
