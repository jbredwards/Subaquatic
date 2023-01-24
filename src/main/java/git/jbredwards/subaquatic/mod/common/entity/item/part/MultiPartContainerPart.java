package git.jbredwards.subaquatic.mod.common.entity.item.part;

import git.jbredwards.subaquatic.mod.common.entity.item.AbstractBoatContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public abstract class MultiPartContainerPart extends MultiPartEntityPart
{
    @Nonnull
    protected final AbstractBoatContainer parentBoat;
    public MultiPartContainerPart(@Nonnull IEntityMultiPart parent, @Nonnull String partName, float width, float height) {
        super(parent, partName, width, height);
        parentBoat = (AbstractBoatContainer)parent;
    }

    @Nonnull
    public String getFixType() { return "none"; }

    @SideOnly(Side.CLIENT)
    public abstract void renderContainer(double x, double y, double z, float entityYaw, float partialTicks, boolean isChristmas);
    public boolean shouldRenderContainer() { return true; }

    @Override
    public void setPortal(@Nonnull BlockPos pos) {}

    @Nullable
    @Override
    public Entity changeDimension(int dimensionIn, @Nonnull ITeleporter teleporter) { return null; }
    public void onDimensionChanged() {}

    @Override
    public boolean canBeCollidedWith() { return parentBoat.canBeCollidedWith(); }

    @Override
    protected abstract void readEntityFromNBT(@Nonnull NBTTagCompound compound);

    @Override
    protected abstract void writeEntityToNBT(@Nonnull NBTTagCompound compound);

    @Nonnull
    @Override
    public ItemStack getPickedResult(@Nonnull RayTraceResult target) { return parentBoat.getPickedResult(target); }

    @Nonnull
    public Vec3d getContainerOffset(boolean applyBoatRotation) {
        final Vec3d offset = new Vec3d(-0.375, parentBoat.getMountedYOffset() + 0.28, 0);
        return applyBoatRotation ? offset.rotateYaw(-parentBoat.rotationYaw * 0.0175f - (float)Math.PI / 2) : offset;
    }

    @Override
    public void applyEntityCollision(@Nonnull Entity entityIn) {
        if(entityIn != parent) {
            if(entityIn instanceof EntityBoat) {
                if(entityIn.getEntityBoundingBox().minY < getEntityBoundingBox().maxY)
                    super.applyEntityCollision(entityIn);
            }

            else if(entityIn.getEntityBoundingBox().minY <= getEntityBoundingBox().minY)
                super.applyEntityCollision(entityIn);
        }
    }

    @Override
    public boolean hasCustomName() { return parentBoat.hasCustomName(); }

    @Nonnull
    @Override
    public String getCustomNameTag() { return parentBoat.getCustomNameTag(); }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() { return parentBoat.getDisplayName(); }
}
