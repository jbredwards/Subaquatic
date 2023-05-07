package git.jbredwards.subaquatic.mod.client.entity.renderer;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.client.entity.model.ModelCod;
import git.jbredwards.subaquatic.mod.common.entity.living.AbstractFish;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public class RenderCod extends RenderLiving<AbstractFish>
{
    @Nonnull
    private static final ResourceLocation TEXTURE = new ResourceLocation(Subaquatic.MODID, "textures/entity/fish/cod.png");
    public RenderCod(@Nonnull RenderManager renderManagerIn) { super(renderManagerIn, new ModelCod(), 0.2f); }

    @Nonnull
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull AbstractFish entity) { return TEXTURE; }

    @Override
    protected void applyRotations(@Nonnull AbstractFish entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
        GlStateManager.rotate(4.3f * MathHelper.sin(0.6f * ageInTicks), 0, 1, 1);

        if(!entityLiving.isInWater()) {
            GlStateManager.translate(0.1, 0.1, -0.1);
            GlStateManager.rotate(90, 0, 0, 1);
        }
    }
}
