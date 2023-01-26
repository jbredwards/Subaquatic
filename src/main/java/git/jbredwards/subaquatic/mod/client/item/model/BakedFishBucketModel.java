package git.jbredwards.subaquatic.mod.client.item.model;

import com.google.common.collect.ImmutableList;
import git.jbredwards.subaquatic.mod.common.capability.IFishBucket;
import git.jbredwards.subaquatic.mod.common.capability.util.FishBucketData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.common.model.TRSRTransformation;
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
@SideOnly(Side.CLIENT)
public class BakedFishBucketModel extends BakedModelWrapper<IBakedModel>
{
    @Nonnull protected final Map<EntityEntry, List<BakedQuad>> overlayCache = new HashMap<>();
    @Nonnull protected final FishBucketData fishType;

    public BakedFishBucketModel(@Nonnull IBakedModel originalModel) { this(originalModel, FishBucketData.EMPTY); }
    public BakedFishBucketModel(@Nonnull IBakedModel originalModel, @Nonnull FishBucketData fishTypeIn) {
        super(originalModel);
        fishType = fishTypeIn;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if(fishType == FishBucketData.EMPTY) return super.getQuads(state, side, rand);
        final ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

        builder.addAll(super.getQuads(state, side, rand));
        builder.addAll(overlayCache.computeIfAbsent(fishType.entity, entity ->
            ItemLayerModel.getQuadsForSprite(-1,
                    FishBucketData.OVERLAY_TEXTURES.get(entity),
                    DefaultVertexFormats.ITEM,
                    Optional.of(TRSRTransformation.identity()))
        ));

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
}
