package git.jbredwards.subaquatic.mod.common.entity.item;

import git.jbredwards.subaquatic.mod.common.entity.item.part.MultiPartContainerPart;
import git.jbredwards.subaquatic.mod.common.entity.item.part.MultiPartFurnacePart;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class EntityBoatFurnace extends AbstractBoatContainer
{
    public EntityBoatFurnace(@Nonnull World worldIn) { super(worldIn); }
    public EntityBoatFurnace(@Nonnull World worldIn, double x, double y, double z) { super(worldIn, x, y, z); }

    @Nonnull
    @Override
    protected MultiPartContainerPart createContainerPart() {
        return new MultiPartFurnacePart(this, "furnace", 0.9f, 0.9f);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getBrightnessForRender() {
        return ((MultiPartFurnacePart)containerPart).getField(0) > 0
                ? Math.max(15728752, super.getBrightnessForRender())
                : super.getBrightnessForRender();
    }

    @Override
    public float getBrightness() {
        return ((MultiPartFurnacePart)containerPart).getField(0) > 0 ? 0.5f : super.getBrightness();
    }
}
