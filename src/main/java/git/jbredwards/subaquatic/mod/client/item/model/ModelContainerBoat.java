package git.jbredwards.subaquatic.mod.client.item.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.capability.IBoatType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public final class ModelContainerBoat implements IModel
{
    @Nonnull
    public static final ModelContainerBoat DEFAULT = new ModelContainerBoat(TextureMap.LOCATION_MISSING_TEXTURE);
    static final float NORTH_Z = 7.498f / 16;
    static final float SOUTH_Z = 8.502f / 16;

    @Nonnull
    final ResourceLocation overlayTexture;
    public ModelContainerBoat(@Nonnull ResourceLocation overlayTextureIn) { overlayTexture = overlayTextureIn; }

    @Nonnull
    @Override
    public Collection<ResourceLocation> getTextures() { return Collections.singletonList(overlayTexture); }

    @Nonnull
    @Override
    public IBakedModel bake(@Nonnull IModelState state, @Nonnull VertexFormat format, @Nonnull Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        final TRSRTransformation transform = state.apply(Optional.empty()).orElse(TRSRTransformation.identity());
        final TextureAtlasSprite overlay = bakedTextureGetter.apply(overlayTexture);
        final ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

        builder.addAll(ItemTextureQuadConverter.convertTexture(format, transform, overlay, overlay, NORTH_Z, EnumFacing.NORTH, 0xFFFFFFFF, 1));
        builder.addAll(ItemTextureQuadConverter.convertTexture(format, transform, overlay, overlay, SOUTH_Z, EnumFacing.SOUTH, 0xFFFFFFFF, 1));

        return new BakedModelCache(builder.build(), overlay, transform, PerspectiveMapWrapper.getTransforms(state));
    }

    @Nonnull
    @Override
    public IModel process(@Nonnull ImmutableMap<String, String> customData) {
        //if it's Christmas, use the provided holiday texture
        if(customData.containsKey("holidayTexture")) {
            final Calendar calendar = Calendar.getInstance();
            if(calendar.get(Calendar.MONTH) + 1 == 12 && calendar.get(Calendar.DATE) >= 24 && calendar.get(Calendar.DATE) <= 26) {
                final JsonElement overlay = new JsonParser().parse(customData.get("holidayTexture"));
                if(overlay.isJsonPrimitive() && overlay.getAsJsonPrimitive().isString())
                    return new ModelContainerBoat(new ResourceLocation(overlay.getAsString()));
            }
        }

        //use provided overlay texture
        if(customData.containsKey("overlayTexture")) {
            final JsonElement overlay = new JsonParser().parse(customData.get("overlayTexture"));
            if(overlay.isJsonPrimitive() && overlay.getAsJsonPrimitive().isString())
                return new ModelContainerBoat(new ResourceLocation(overlay.getAsString()));
        }

        return this;
    }

    static final class BakedModelCache extends BakedItemModel
    {
        @Nonnull final Map<Item, IBakedModel> cache = new HashMap<>();
        @Nonnull final TRSRTransformation transform;

        public BakedModelCache(@Nonnull ImmutableList<BakedQuad> overlayQuads, @Nonnull TextureAtlasSprite overlayIn, @Nonnull TRSRTransformation transformIn, @Nonnull ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transformsIn) {
            super(overlayQuads, overlayIn, transformsIn, ItemOverrideList.NONE, transformIn.isIdentity());
            transform = transformIn;
        }

        @Nonnull
        @Override
        public ItemOverrideList getOverrides() {
            return new ItemOverrideList(Collections.emptyList()) {
                @Nonnull
                @Override
                public IBakedModel handleItemState(@Nonnull IBakedModel originalModel, @Nonnull ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
                    final IBoatType cap = IBoatType.get(stack);
                    if(cap != null) return cache.computeIfAbsent(cap.getType().getKey(), boat -> {
                        final IBakedModel boatModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(boat));
                        final ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
                        builder.addAll(boatModel.getQuads(null, null, 0));
                        builder.addAll(quads);

                        return new BakedItemModel(builder.build(), boatModel.getParticleTexture(), transforms, boatModel.getOverrides(), transform.isIdentity());
                    });

                    throw new IllegalStateException("Tried to apply subaquatic:builtin/boat model to non boat container item: " + stack.getItem().delegate.name());
                }
            };
        }
    }

    public enum Loader implements ICustomModelLoader
    {
        INSTANCE;

        @Override
        public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {}

        @Override
        public boolean accepts(@Nonnull ResourceLocation modelLocation) {
            return modelLocation.getNamespace().equals(Subaquatic.MODID) && modelLocation.getPath().endsWith("builtin/boat");
        }

        @Nonnull
        @Override
        public IModel loadModel(@Nonnull ResourceLocation modelLocation) { return DEFAULT; }
    }
}
