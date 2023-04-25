package git.jbredwards.subaquatic.mod.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class ItemBlockHugeMushroom extends ItemMultiTexture
{
    public ItemBlockHugeMushroom(@Nonnull Block block) {
        super(block, block, new String[] {"block", "stem", "stem_all"});
    }

    @Nonnull
    @Override
    public CreativeTabs getCreativeTab() { return CreativeTabs.DECORATIONS; }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if(isInCreativeTab(tab)) {
            items.add(new ItemStack(this, 1, 0));
            items.add(new ItemStack(this, 1, 1));
            items.add(new ItemStack(this, 1, 2));
        }
    }
}
