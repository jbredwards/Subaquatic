package git.jbredwards.subaquatic.mod.client.entity.renderer.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public class LayerGlowing implements LayerRenderer<EntityLivingBase>
{
    @Nonnull protected final ResourceLocation overlayTexture;
    @Nonnull protected final RenderLivingBase<?> renderer;
    protected final boolean renderIfInvisible;

    public LayerGlowing(@Nonnull ResourceLocation overlayTextureIn, @Nonnull RenderLivingBase<?> rendererIn, boolean renderIfInvisibleIn) {
        overlayTexture = overlayTextureIn;
        renderer = rendererIn;
        renderIfInvisible = renderIfInvisibleIn;
    }

    @Override
    public void doRenderLayer(@Nonnull EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(renderIfInvisible || !entity.isInvisible()) {
            GlStateManager.pushMatrix();

            renderer.bindTexture(overlayTexture);
            renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            //makes this layer transparent
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.disableLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
            GlStateManager.depthMask(!entity.isInvisible());
            GlStateManager.color(1, 1, 1);
            renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

            //cleanup
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() { return false; }
}
