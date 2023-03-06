package git.jbredwards.subaquatic.mod.common.capability.util;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public class BoatType
{
    @Nonnull
    public static final BoatType DEFAULT = new BoatType(Items.BOAT, 0);

    @Nullable
    @SideOnly(Side.CLIENT)
    public ResourceLocation entityTexture;

    @Nonnull
    public final Item boat;
    public final int boatMeta;

    public BoatType(@Nonnull Item boatIn, int boatMetaIn) {
        boat = boatIn;
        boatMeta = boatMetaIn;
    }

    @Nonnull
    public NBTTagCompound serializeNBT() {
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("item", boat.delegate.name().toString());
        nbt.setInteger("meta", boatMeta);
        return nbt;
    }
}
