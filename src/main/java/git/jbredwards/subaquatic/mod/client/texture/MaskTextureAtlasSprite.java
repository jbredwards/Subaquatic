package git.jbredwards.subaquatic.mod.client.texture;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.function.Function;

/**
 * Made from a base + mask, useful for dynamic item models that involve multiple layers.
 * Used for tropical fish buckets.
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public class MaskTextureAtlasSprite extends TextureAtlasSprite
{
    @Nonnull
    protected final ResourceLocation baseLocation, maskLocation;
    public MaskTextureAtlasSprite(@Nonnull ResourceLocation baseLocationIn, @Nonnull ResourceLocation maskLocationIn) {
        super(maskLocationIn + "_base");
        baseLocation = baseLocationIn;
        maskLocation = maskLocationIn;
    }

    @Override
    public boolean hasCustomLoader(@Nonnull IResourceManager manager, @Nonnull ResourceLocation location) {
        return true;
    }

    @Override
    public boolean load(@Nonnull IResourceManager manager, @Nonnull ResourceLocation location, @Nonnull Function<ResourceLocation, TextureAtlasSprite> textureGetter) {
        final TextureAtlasSprite base = textureGetter.apply(baseLocation);
        final TextureAtlasSprite mask = textureGetter.apply(maskLocation);

        width = base.getIconWidth();
        height = base.getIconHeight();

        final int[][] pixels = new int[Minecraft.getMinecraft().gameSettings.mipmapLevels + 1][];
        pixels[0] = new int[width * height];

        if(mask.getIconWidth() == width && mask.getIconHeight() == height) {
            final int[][] basePixels = base.getFrameTextureData(0);
            final int[][] maskPixels = mask.getFrameTextureData(0);
            for(int i = 0; i < width * height; i++) pixels[0][i] = maskPixels[0][i] >>> 24 == 255 ? 0 : basePixels[0][i];
        }

        clearFramesTextureData();
        framesTextureData.add(pixels);
        return false;
    }

    @Nonnull
    @Override
    public Collection<ResourceLocation> getDependencies() { return ImmutableList.of(baseLocation, maskLocation); }
}
