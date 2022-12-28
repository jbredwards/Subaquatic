package git.jbredwards.subaquatic.mod.common.item;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public final class CreativeTab extends CreativeTabs
{
    public static final CreativeTab INSTANCE = new CreativeTab();
    CreativeTab() { super(Subaquatic.MODID + ".tab"); }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack createIcon() { return new ItemStack(ModItems.NAUTILUS_SHELL); }
}
