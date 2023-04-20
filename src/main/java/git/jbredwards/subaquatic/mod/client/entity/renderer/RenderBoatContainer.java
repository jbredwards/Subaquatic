package git.jbredwards.subaquatic.mod.client.entity.renderer;

import git.jbredwards.subaquatic.mod.common.capability.IBoatType;
import git.jbredwards.subaquatic.mod.common.entity.item.AbstractBoatContainer;
import git.jbredwards.subaquatic.mod.common.entity.item.EntityBoatFurnace;
import git.jbredwards.subaquatic.mod.common.entity.item.part.MultiPartFurnacePart;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBoat;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Calendar;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public class RenderBoatContainer extends RenderBoat
{
    protected final boolean isChristmas;
    public RenderBoatContainer(@Nonnull RenderManager renderManagerIn) {
        super(renderManagerIn);
        final Calendar calendar = Calendar.getInstance();
        isChristmas = calendar.get(Calendar.MONTH) + 1 == 12 && calendar.get(Calendar.DATE) >= 24 && calendar.get(Calendar.DATE) <= 26;
        modelBoat = new ModelBoat() { //soft-fix for unwanted particle culling with furnace boat's lit furnace particles
            public void renderMultipass(@Nonnull Entity entityIn, float partialTicks, float unused1, float unused2, float unused3, float unused4, float scale) {
                if(entityIn instanceof EntityBoatFurnace) noWater.rotationPointY = entityIn.getPassengers().isEmpty()
                        && ((MultiPartFurnacePart)((EntityBoatFurnace)entityIn).containerPart).getField(0) > 0 ? 1.85f : -3;

                super.renderMultipass(entityIn, partialTicks, unused1, unused2, unused3, unused4, scale);
            }
        };
    }

    @Override
    public void doRender(@Nonnull EntityBoat entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if(entity instanceof AbstractBoatContainer && ((AbstractBoatContainer)entity).containerPart.shouldRenderContainer()) {
            GlStateManager.pushMatrix();
            if(renderOutlines) {
                GlStateManager.enableColorMaterial();
                GlStateManager.enableOutlineMode(getTeamColor(entity));
            }

            setupTranslation(x, y, z);
            setupRotation(entity, entityYaw, partialTicks);

            final Vec3d offset = ((AbstractBoatContainer)entity).containerPart.getContainerOffset(false);
            GlStateManager.translate(offset.x, -(offset.y + ((AbstractBoatContainer)entity).containerPart.getEyeHeight()), offset.z);
            ((AbstractBoatContainer)entity).containerPart.renderContainer(x, y, z, entityYaw, partialTicks, isChristmas);

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
        if(cap == null) return super.getEntityTexture(entity);

        final ResourceLocation texture = cap.getType().entityTexture;
        return texture != null ? texture : super.getEntityTexture(entity);
    }
}
