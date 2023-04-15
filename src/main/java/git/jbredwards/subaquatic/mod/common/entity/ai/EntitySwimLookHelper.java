package git.jbredwards.subaquatic.mod.common.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class EntitySwimLookHelper extends EntityLookHelper
{
    protected final int maxYRotFromCenter;
    public EntitySwimLookHelper(@Nonnull EntityLiving entityLivingIn, int maxYRotFromCenterIn) {
        super(entityLivingIn);
        maxYRotFromCenter = maxYRotFromCenterIn;
    }

    @Override
    public void onUpdateLook() {
        if(isLooking) {
            isLooking = false;
            final double distX = posX - entity.posX;
            final double distY = posY - (entity.posY + entity.getEyeHeight());
            final double distZ = posZ - entity.posZ;

            final float lookYaw = (float)(MathHelper.atan2(distZ, distX) * 180 / Math.PI) - 90;
            final float lookPitch = (float)(-(MathHelper.atan2(distY, Math.sqrt(distX * distX + distZ * distZ)) * 180 / Math.PI));

            entity.rotationYawHead = updateRotation(entity.rotationYawHead, lookYaw, deltaLookYaw);
            entity.rotationPitch = updateRotation(entity.rotationPitch, lookPitch, deltaLookPitch);
        }
        else {
            if(entity.getNavigator().noPath()) entity.rotationPitch = updateRotation(entity.rotationPitch, 0, 5);
            entity.rotationYawHead = updateRotation(entity.rotationYawHead, entity.renderYawOffset, deltaLookYaw);
        }

        final float wrapped = MathHelper.wrapDegrees(entity.rotationYawHead - entity.renderYawOffset);
        if(wrapped < -maxYRotFromCenter) entity.renderYawOffset -= 4;
        else if(wrapped > maxYRotFromCenter) entity.renderYawOffset += 4;
    }
}
