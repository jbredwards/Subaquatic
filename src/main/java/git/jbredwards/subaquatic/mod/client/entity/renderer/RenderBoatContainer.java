package git.jbredwards.subaquatic.mod.client.entity.renderer;

import git.jbredwards.subaquatic.mod.common.capability.IBoatType;
import git.jbredwards.subaquatic.mod.common.entity.item.EntityBoatContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public class RenderBoatContainer extends RenderBoat
{
    public RenderBoatContainer(@Nonnull RenderManager renderManagerIn) { super(renderManagerIn); }

    @Override
    public void doRender(@Nonnull EntityBoat entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if(entity instanceof EntityBoatContainer && ((EntityBoatContainer)entity).containerPart.shouldRenderContainer()) {
            GlStateManager.pushMatrix();
            if(renderOutlines) {
                GlStateManager.enableColorMaterial();
                GlStateManager.enableOutlineMode(getTeamColor(entity));
            }

            setupTranslation(x, y, z);
            setupRotation(entity, entityYaw, partialTicks);

            final Vec3d offset = ((EntityBoatContainer)entity).containerPart.getContainerOffset();
            GlStateManager.translate(offset.x, offset.y, offset.z);
            ((EntityBoatContainer)entity).containerPart.renderContainer(x, y, z, entityYaw, partialTicks);

            if(renderOutlines) {
                GlStateManager.disableOutlineMode();
                GlStateManager.disableColorMaterial();
            }

            GlStateManager.popMatrix();
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityBoat entity) {
        final IBoatType cap = IBoatType.get(entity);
        return cap != null ? cap.getType().getValue() : super.getEntityTexture(entity);
    }
}
