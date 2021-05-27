package git.jbredwards.fluidlogged_additions.client.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * allows custom model properties to be setup easily through the block's or item's class
 * @author jbred
 *
 */
public interface IModelProperties
{
    //for item models
    @SideOnly(Side.CLIENT)
    default void setupModel(@Nonnull Item item) {}

    //for block models
    @SideOnly(Side.CLIENT)
    default void setupModel(@Nonnull Block block) {}
}
