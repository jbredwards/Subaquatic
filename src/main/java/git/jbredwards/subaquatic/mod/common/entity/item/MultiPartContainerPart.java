package git.jbredwards.subaquatic.mod.common.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IInteractionObject;
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
public abstract class MultiPartContainerPart extends MultiPartEntityPart implements IInteractionObject
{
    @Nonnull
    protected final EntityBoatContainer parentBoat;
    public MultiPartContainerPart(@Nonnull IEntityMultiPart parent, @Nonnull String partName, float width, float height) {
        super(parent, partName, width, height);
        parentBoat = (EntityBoatContainer)parent;
    }

    @SideOnly(Side.CLIENT)
    public abstract void renderContainer(double x, double y, double z, float entityYaw, float partialTicks);
    public boolean shouldRenderContainer() { return true; }

    @Nullable
    @Override
    public Entity changeDimension(int dimensionIn, @Nonnull ITeleporter teleporter) { return null; }
    protected void onDimensionChanged() {}

    @Override
    public boolean canBeCollidedWith() { return parentBoat.canBeCollidedWith(); }

    @Override
    protected abstract void readEntityFromNBT(@Nonnull NBTTagCompound compound);

    @Override
    protected abstract void writeEntityToNBT(@Nonnull NBTTagCompound compound);
}
