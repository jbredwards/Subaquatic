package git.jbredwards.fluidlogged_additions.common.init;

import git.jbredwards.fluidlogged_additions.util.Constants;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jbred
 *
 */
public enum ModItems
{
    ;

    //item init
    public static final List<Item> INIT = new ArrayList<>();

    //item-blocks
    public static final ItemBlock NAUTILUS_SHELL = register("nautilus_shell", new ItemBlock(ModBlocks.NAUTILUS_SHELL));

    //prepares the item for registration
    public static <I extends Item> I register(String name, I item) {
        item.setRegistryName(name).setUnlocalizedName(Constants.MODID + "." + name).setCreativeTab(CreativeTab.INSTANCE);
        INIT.add(item);

        return item;
    }
}
