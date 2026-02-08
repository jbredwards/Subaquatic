/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client.item.model;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import git.jbredwards.subaquatic.api.entity.bucketable.BucketableEntityHandler;
import git.jbredwards.subaquatic.api.entity.bucketable.BucketableEntityRegistry;
import git.jbredwards.subaquatic.mod.Subaquatic;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public abstract class ModelEntityBucketOverlay implements IModel
{
    @Nonnull
    public static final Object2FloatMap<Item> BUCKET_OFFSETS = new Object2FloatOpenHashMap<>();
    public static float getYOffset(@Nonnull final Item bucket) {
        if(BUCKET_OFFSETS.containsKey(bucket)) return BUCKET_OFFSETS.get(bucket);
        @Nonnull final ResourceLocation bucketId = Objects.requireNonNull(bucket.getRegistryName());
        @Nonnull final ResourceLocation location = new ResourceLocation(bucketId.getNamespace(), Subaquatic.MODID + "/bucket_offset/" + bucketId.getPath() + ".txt");

        // Read offset from texture packs.
        final float offset;
        try(@Nonnull final IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(location)) {
            try(@Nonnull final Scanner scanner = new Scanner(resource.getInputStream())) { offset = scanner.nextFloat(); }
        }

        // No offset specified.
        catch(@Nonnull final IOException e) {
            BUCKET_OFFSETS.put(bucket, 0);
            return 0;
        }

        BUCKET_OFFSETS.put(bucket, offset);
        return offset;
    }

    @Nonnull
    private final LoadingCache<CacheKey, ImmutableList<BakedQuad>> cache = CacheBuilder.newBuilder().maximumSize(500).build(CacheLoader.from(CacheKey::get));
    public static final class CacheKey implements Supplier<ImmutableList<BakedQuad>>
    {
        static final int size = DefaultVertexFormats.ITEM.getIntegerSize();
        static final int offset = DefaultVertexFormats.ITEM.getColorOffset() / 4; // assumes that color is aligned

        @Nonnull public final ResourceLocation texture;
        @Nonnull public final OptionalInt color;
        public final float yOffset;
        public final boolean flipped;

        public CacheKey(@Nonnull final ResourceLocation textureIn, final float yOffsetIn, final boolean flippedIn, @Nonnull final OptionalInt colorIn) {
            texture = textureIn;
            yOffset = yOffsetIn;
            flipped = flippedIn;
            color = colorIn;
        }

        @Nonnull
        @Override
        public ImmutableList<BakedQuad> get() {
            @Nonnull final ImmutableList<BakedQuad> quads = ItemLayerModel.getQuadsForSprite(-1, ModelLoader.defaultTextureGetter().apply(texture), DefaultVertexFormats.ITEM,
                    Optional.of(new TRSRTransformation(new Vector3f(0, yOffset - 0.00005f, -0.005f), flipped ? new Quat4f(0, 0, 1, 0) : null, new Vector3f(1, 1.0001f, 1.01f), null)));
            return color.isPresent() ? applyColor(quads, color.getAsInt()) : quads;
        }

        @Nonnull
        private static ImmutableList<BakedQuad> applyColor(@Nonnull final ImmutableList<BakedQuad> quads, final int argb) {
            @Nonnull final ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

            // Apply color to quads.
            quads.forEach(quad -> {
                @Nonnull final BakedQuad newQuad = new BakedQuad(quad.getVertexData().clone(), -1, quad.getFace(), quad.getSprite(), quad.shouldApplyDiffuseLighting(), quad.getFormat());
                builder.add(newQuad);

                int bgr = (argb & 0xFF) << 16 | (argb & 0xFF00) | (argb >>> 16 & 0xFF);
                bgr |= (argb & 0xFF000000) != 0 ? argb & 0xFF000000 : 0xFF000000;
                for(int i = 0; i < 4; i++) newQuad.getVertexData()[offset + size * i] = bgr;
            });

            return builder.build();
        }

        @Override
        public boolean equals(@Nullable final Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;

            @Nonnull final CacheKey cacheKey = (CacheKey)o;
            return Float.compare(cacheKey.yOffset, yOffset) == 0 && flipped == cacheKey.flipped && color == cacheKey.color && texture.equals(cacheKey.texture);
        }

        @Override
        public int hashCode() { return Objects.hash(texture, yOffset, flipped, color); }
    }

    protected final class CacheKeyBuilder implements Function<ImmutableList<ResourceLocation>, ImmutableList<BakedQuad>>
    {
        public final float yOffset;
        public final boolean flipped;

        @Nonnull
        private IntFunction<OptionalInt> color;
        public CacheKeyBuilder(@Nonnull final ItemStack stack) { this(stack, true); }
        public CacheKeyBuilder(@Nonnull final ItemStack stack, final boolean forApply) { this(stack, forApply, stack.getSubCompound(BucketableEntityRegistry.NBT_ROOT)); }
        public CacheKeyBuilder(@Nonnull final ItemStack stack, final boolean forApply, @Nullable final NBTTagCompound root) {
            yOffset = getYOffset(stack.getItem());

            @Nullable final FluidStack fluid = FluidUtil.getFluidContained(stack);
            flipped = fluid != null && fluid.getFluid().isLighterThanAir();

            if(forApply) {
                @Nullable final BucketableEntityHandler<?> handler = root != null ? BucketableEntityRegistry.getHandler(root) : null;
                if(handler != null) color = tintIndex -> handler.overlayColor(root, tintIndex);
            }

            if(color == null) color = tintIndex -> OptionalInt.empty();
        }

        @Nonnull
        @Override
        public ImmutableList<BakedQuad> apply(@Nonnull final ImmutableList<ResourceLocation> textures) {
            @Nonnull final Stream.Builder<CacheKey> builder = Stream.builder();
            for(int i = 0; i < textures.size(); i++) builder.accept(new CacheKey(textures.get(i), yOffset, flipped, color.apply(i)));
            return get(builder.build());
        }
    }

    @Nonnull
    protected ImmutableList<BakedQuad> get(@Nonnull final Stream<CacheKey> keys) {
        return keys.map(cache).flatMap(ImmutableList::stream).collect(ImmutableList.toImmutableList());
    }

    @Nonnull
    protected final ImmutableList<ResourceLocation> textures;
    protected ModelEntityBucketOverlay(@Nonnull final ImmutableList<ResourceLocation> texturesIn) { textures = texturesIn; }

    @Nonnull
    @Override
    public final Collection<ResourceLocation> getTextures() { return textures; }
}
