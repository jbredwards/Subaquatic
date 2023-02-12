package git.jbredwards.subaquatic.mod.client.entity.renderer;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.client.entity.model.ModelPufferfish;
import git.jbredwards.subaquatic.mod.common.entity.living.EntityPufferfish;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
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
public class RenderPufferfish extends RenderLiving<EntityPufferfish>
{
    @Nonnull
    private static final ResourceLocation TEXTURE = new ResourceLocation(Subaquatic.MODID, "textures/entity/fish/pufferfish.png");

    @Nonnull
    protected final ModelPufferfish smallModel = new ModelPufferfish.Small(), mediumModel = new ModelPufferfish.Medium(), largeModel = new ModelPufferfish.Large();
    public RenderPufferfish(@Nonnull RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelPufferfish.Small(), 0.1f);
    }

    @Nonnull
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityPufferfish entity) { return TEXTURE; }

    @Override
    public void doRender(@Nonnull EntityPufferfish entity, double x, double y, double z, float entityYaw, float partialTicks) {
        final int puffState = entity.getPuffState();
        if(puffState == 0) mainModel = smallModel;
        else if(puffState == 1) mainModel = mediumModel;
        else mainModel = largeModel;

        shadowSize = 0.1f + 0.1f * puffState;
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void applyRotations(@Nonnull EntityPufferfish entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        GlStateManager.translate(0, Math.cos(ageInTicks * 0.05) * 0.08, 0);
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
    }
}
