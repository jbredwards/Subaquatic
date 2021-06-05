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

    //coral
    public static final ItemBlock TUBE_CORAL_BLOCK =   register("tube_coral_block",   new ItemBlock(ModBlocks.TUBE_CORAL_BLOCK));
    public static final ItemBlock BRAIN_CORAL_BLOCK =  register("brain_coral_block",  new ItemBlock(ModBlocks.BRAIN_CORAL_BLOCK));
    public static final ItemBlock BUBBLE_CORAL_BLOCK = register("bubble_coral_block", new ItemBlock(ModBlocks.BUBBLE_CORAL_BLOCK));
    public static final ItemBlock FIRE_CORAL_BLOCK =   register("fire_coral_block",   new ItemBlock(ModBlocks.FIRE_CORAL_BLOCK));
    public static final ItemBlock HORN_CORAL_BLOCK =   register("horn_coral_block",   new ItemBlock(ModBlocks.HORN_CORAL_BLOCK));
    public static final ItemBlock DEAD_TUBE_CORAL_BLOCK =   register("dead_tube_coral_block",   new ItemBlock(ModBlocks.DEAD_TUBE_CORAL_BLOCK));
    public static final ItemBlock DEAD_BRAIN_CORAL_BLOCK =  register("dead_brain_coral_block",  new ItemBlock(ModBlocks.DEAD_BRAIN_CORAL_BLOCK));
    public static final ItemBlock DEAD_BUBBLE_CORAL_BLOCK = register("dead_bubble_coral_block", new ItemBlock(ModBlocks.DEAD_BUBBLE_CORAL_BLOCK));
    public static final ItemBlock DEAD_FIRE_CORAL_BLOCK =   register("dead_fire_coral_block",   new ItemBlock(ModBlocks.DEAD_FIRE_CORAL_BLOCK));
    public static final ItemBlock DEAD_HORN_CORAL_BLOCK =   register("dead_horn_coral_block",   new ItemBlock(ModBlocks.DEAD_HORN_CORAL_BLOCK));

    //blocks

    //misc item-blocks
    public static final ItemBlock NAUTILUS_SHELL = register("nautilus_shell", new ItemBlock(ModBlocks.NAUTILUS_SHELL));

    //prepares the item for registration
    public static <I extends Item> I register(String name, I item) {
        item.setRegistryName(name).setUnlocalizedName(Constants.MODID + "." + name).setCreativeTab(CreativeTab.INSTANCE);
        INIT.add(item);

        return item;
    }
}
