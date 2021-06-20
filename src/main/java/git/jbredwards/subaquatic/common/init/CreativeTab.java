package git.jbredwards.subaquatic.common.init;

import git.jbredwards.subaquatic.util.Constants;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author jbred
 *
 */
public final class CreativeTab extends CreativeTabs
{
    public static final CreativeTab INSTANCE = new CreativeTab();
    private CreativeTab() {
        super(Constants.MODID + ":tab");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModItems.NAUTILUS_SHELL);
    }
}
