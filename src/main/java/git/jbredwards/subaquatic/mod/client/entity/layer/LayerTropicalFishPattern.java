package git.jbredwards.subaquatic.mod.client.entity.layer;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.client.entity.model.ModelTropicalFish;
import git.jbredwards.subaquatic.mod.client.entity.renderer.RenderTropicalFish;
import git.jbredwards.subaquatic.mod.common.entity.living.EntityTropicalFish;
import git.jbredwards.subaquatic.mod.common.entity.util.TropicalFishData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
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
public class LayerTropicalFishPattern implements LayerRenderer<EntityTropicalFish>
{
    @Nonnull
    protected final ModelTropicalFish modelA = new ModelTropicalFish.A(0.008f), modelB = new ModelTropicalFish.B(0.008f);

    @Nonnull
    protected final RenderTropicalFish renderer;
    public LayerTropicalFishPattern(@Nonnull RenderTropicalFish rendererIn) { renderer = rendererIn; }

    @Override
    public void doRenderLayer(@Nonnull EntityTropicalFish entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if(!entity.isInvisible()) {
            final TropicalFishData variant = entity.getVariant();
            renderer.bindTexture(new ResourceLocation(Subaquatic.MODID, "textures/entity/fish/tropical_"
                    + ((variant.primaryShape & 1) == 0 ? 'a' : 'b') + "_pattern_" + (variant.secondaryShape) + ".png"));

            final float[] colors = variant.secondaryColor.getColorComponentValues();
            GlStateManager.color(colors[0], colors[1], colors[2]);

            final ModelTropicalFish model = (variant.primaryShape & 1) == 0 ? modelA : modelB;
            model.setModelAttributes(renderer.getMainModel());
            model.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    @Override
    public boolean shouldCombineTextures() { return true; }
}
