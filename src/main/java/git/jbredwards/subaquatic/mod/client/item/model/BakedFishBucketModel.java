package git.jbredwards.subaquatic.mod.client.item.model;

import com.google.common.collect.ImmutableList;
import git.jbredwards.subaquatic.mod.common.capability.IFishBucket;
import git.jbredwards.subaquatic.mod.common.capability.util.FishBucketData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.util.*;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public class BakedFishBucketModel extends BakedModelWrapper<IBakedModel>
{
    @Nonnull protected static final Map<TextureAtlasSprite, List<BakedQuad>> OVERLAY_QUADS = new HashMap<>();
    @Nonnull protected final FishBucketData fishData;

    public BakedFishBucketModel(@Nonnull IBakedModel originalModel) { this(originalModel, FishBucketData.EMPTY); }
    public BakedFishBucketModel(@Nonnull IBakedModel originalModel, @Nonnull FishBucketData fishDataIn) {
        super(originalModel);
        fishData = fishDataIn;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if(fishData == FishBucketData.EMPTY || side != null) return super.getQuads(state, side, rand);
        final TextureAtlasSprite sprite = FishBucketData.OVERLAY_TEXTURES.computeIfAbsent(fishData.entity, entry ->
                        data -> Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite()).apply(fishData);

        final ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        builder.addAll(super.getQuads(state, null, rand));
        builder.addAll(OVERLAY_QUADS.computeIfAbsent(sprite, entity -> {
            final TRSRTransformation identity = TRSRTransformation.identity();
            final TRSRTransformation transform = new TRSRTransformation(
                new Vector3f(0, -0.00005f, -0.005f), identity.getLeftRot(), new Vector3f(1, 1.0001f, 1.01f), identity.getRightRot());

            return ItemLayerModel.getQuadsForSprite(3, sprite, DefaultVertexFormats.ITEM, Optional.of(transform));
        }));

        return builder.build();
    }

    @Nonnull
    @Override
    public ItemOverrideList getOverrides() {
        return new ItemOverrideList(Collections.emptyList()) {
            @Nonnull
            @Override
            public IBakedModel handleItemState(@Nonnull IBakedModel originalModelIn, @Nonnull ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
                final IBakedModel bucketOverride = originalModel.getOverrides().handleItemState(originalModel, stack, world, entity);
                final IFishBucket cap = IFishBucket.get(stack);

                return cap != null ? new BakedFishBucketModel(bucketOverride, cap.getData()) : bucketOverride;
            }
        };
    }

    @Nonnull
    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(@Nonnull ItemCameraTransforms.TransformType cameraTransformType) {
        final Pair<? extends IBakedModel, Matrix4f> oldPerspective = super.handlePerspective(cameraTransformType);
        return Pair.of(new BakedFishBucketModel(oldPerspective.getKey(), fishData), oldPerspective.getValue());
    }
}
