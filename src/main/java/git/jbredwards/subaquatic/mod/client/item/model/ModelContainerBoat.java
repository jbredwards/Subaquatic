package git.jbredwards.subaquatic.mod.client.item.model;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import git.jbredwards.subaquatic.mod.Subaquatic;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
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
    public static final ModelContainerBoat DEFAULT = new ModelContainerBoat(ModelLoader.MODEL_MISSING, null);

    @Nonnull final ResourceLocation overlay;
    @Nullable final ResourceLocation holidayTexture;

    public ModelContainerBoat(@Nonnull ResourceLocation overlayIn, @Nullable ResourceLocation holidayTextureIn) {
        overlay = overlayIn;
        holidayTexture = holidayTextureIn;
    }

    @Nonnull
    @Override
    public Collection<ResourceLocation> getTextures() {
        if(holidayTexture == null) return Collections.emptyList();
        else return Collections.singletonList(holidayTexture);
    }

    @Nonnull
    @Override
    public IBakedModel bake(@Nonnull IModelState state, @Nonnull VertexFormat format, @Nonnull Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return new BakedModel(ModelLoaderRegistry.getModelOrLogError(overlay, "Couldn't load Boat Container model depencency: " + overlay).bake(state, format, bakedTextureGetter), bakedTextureGetter, holidayTexture, Items.BOAT);
    }

    @Nonnull
    @Override
    public IModel process(@Nonnull ImmutableMap<String, String> customData) {
        if(customData.containsKey("overlay")) {
            final JsonElement overlay = new JsonParser().parse(customData.get("overlay"));
            if(overlay.isJsonPrimitive() && overlay.getAsJsonPrimitive().isString()) {
                if(customData.containsKey("holidayTexture")) {
                    //only initialize holiday texture if it will get used
                    final Calendar calendar = Calendar.getInstance();
                    if(calendar.get(Calendar.MONTH) + 1 == 12 && calendar.get(Calendar.DATE) >= 24 && calendar.get(Calendar.DATE) <= 26) {
                        final JsonElement holidayTexture = new JsonParser().parse(customData.get("holidayTexture"));
                        if(holidayTexture.isJsonPrimitive() && holidayTexture.getAsJsonPrimitive().isString()) {
                            return new ModelContainerBoat(new ModelResourceLocation(overlay.getAsString()), new ResourceLocation(holidayTexture.getAsString()));
                        }
                    }
                }

                return new ModelContainerBoat(new ModelResourceLocation(overlay.getAsString()), null);
            }

            FMLLog.log.fatal("Expect ModelResourceLocation, got: {}", customData.get("overlay"));
        }

        return this;
    }

    static final class BakedModel extends BakedModelWrapper<IBakedModel>
    {
        @Nonnull final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter;
        @Nullable final ResourceLocation holidayTexture;
        @Nonnull final Item boat;

        public BakedModel(@Nonnull IBakedModel originalModelIn, @Nonnull Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetterIn, @Nullable ResourceLocation holidayTextureIn, @Nonnull Item boatIn) {
            super(originalModelIn);
            bakedTextureGetter = bakedTextureGetterIn;
            holidayTexture = holidayTextureIn;
            boat = boatIn;
        }


    }

    public enum Loader implements ICustomModelLoader
    {
        INSTANCE;

        @Override
        public void onResourceManagerReload(@Nonnull IResourceManager resourceManager) {}

        @Override
        public boolean accepts(@Nonnull ResourceLocation modelLocation) {
            return modelLocation.getNamespace().equals(Subaquatic.MODID) && modelLocation.getPath().equals("boat_builtin");
        }

        @Nonnull
        @Override
        public IModel loadModel(@Nonnull ResourceLocation modelLocation) { return DEFAULT; }
    }
}
