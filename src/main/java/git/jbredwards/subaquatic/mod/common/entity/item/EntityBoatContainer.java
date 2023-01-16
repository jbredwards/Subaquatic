package git.jbredwards.subaquatic.mod.common.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public abstract class EntityBoatContainer extends EntityBoat implements IInteractionObject
{
    public EntityBoatContainer(@Nonnull World worldIn) {
        super(worldIn);
        setBoatType(Type.OAK);
    }

    public EntityBoatContainer(@Nonnull World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
        setBoatType(Type.OAK);
    }

    @Override
    protected boolean canFitPassenger(@Nonnull Entity passenger) { return getPassengers().size() == 0; }

    @SideOnly(Side.CLIENT)
    public abstract void renderContainer(double x, double y, double z, float entityYaw, float partialTicks);
    public boolean shouldRenderContainer() { return true; }
}
