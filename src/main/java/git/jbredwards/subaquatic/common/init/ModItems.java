package git.jbredwards.subaquatic.common.init;

import com.google.common.collect.ImmutableList;
import git.jbredwards.subaquatic.common.block.vanilla.BlockCoralFull;
import git.jbredwards.subaquatic.common.item.ItemDurationFood;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * stores all of this mod's items
 * @author jbred
 *
 */
public enum ModItems
{
    ;

    //item init
    public static final List<Item> INIT = new ArrayList<>();

    //blocks
    public static final ItemBlock DRIED_KELP_BLOCK = register("dried_kelp_block", new ItemBlock(ModBlocks.DRIED_KELP_BLOCK));

    //coral
    public static final ItemBlock TUBE_CORAL_BLOCK   = register("tube_coral_block"  , new ItemBlock(ModBlocks.TUBE_CORAL_BLOCK));
    public static final ItemBlock BRAIN_CORAL_BLOCK  = register("brain_coral_block" , new ItemBlock(ModBlocks.BRAIN_CORAL_BLOCK));
    public static final ItemBlock BUBBLE_CORAL_BLOCK = register("bubble_coral_block", new ItemBlock(ModBlocks.BUBBLE_CORAL_BLOCK));
    public static final ItemBlock FIRE_CORAL_BLOCK   = register("fire_coral_block"  , new ItemBlock(ModBlocks.FIRE_CORAL_BLOCK));
    public static final ItemBlock HORN_CORAL_BLOCK   = register("horn_coral_block"  , new ItemBlock(ModBlocks.HORN_CORAL_BLOCK));
    public static final ItemBlock DEAD_TUBE_CORAL_BLOCK   = register("dead_tube_coral_block"  , new ItemBlock(ModBlocks.DEAD_TUBE_CORAL_BLOCK));
    public static final ItemBlock DEAD_BRAIN_CORAL_BLOCK  = register("dead_brain_coral_block" , new ItemBlock(ModBlocks.DEAD_BRAIN_CORAL_BLOCK));
    public static final ItemBlock DEAD_BUBBLE_CORAL_BLOCK = register("dead_bubble_coral_block", new ItemBlock(ModBlocks.DEAD_BUBBLE_CORAL_BLOCK));
    public static final ItemBlock DEAD_FIRE_CORAL_BLOCK   = register("dead_fire_coral_block"  , new ItemBlock(ModBlocks.DEAD_FIRE_CORAL_BLOCK));
    public static final ItemBlock DEAD_HORN_CORAL_BLOCK   = register("dead_horn_coral_block"  , new ItemBlock(ModBlocks.DEAD_HORN_CORAL_BLOCK));

    //item-blocks
    public static final ItemBlock KELP = register("kelp", new ItemBlock(ModBlocks.KELP));
    public static final ItemBlock NAUTILUS_SHELL = register("nautilus_shell", new ItemBlock(ModBlocks.NAUTILUS_SHELL));

    //items

    //food
    public static final ItemFood DRIED_KELP = register("dried_kelp", new ItemDurationFood(1, false).setMaxItemUseDuration(16));

    //prepares the item for registration
    public static <I extends Item> I register(String name, I item) {
        item.setRegistryName(name).setUnlocalizedName(Constants.MODID + "." + name).setCreativeTab(CreativeTab.INSTANCE);
        INIT.add(item);

        return item;
    }

    //ore dict registration
    public static void registerOreDict() {
        //item-blocks
        OreDictionary.registerOre("shellNautilus", NAUTILUS_SHELL);

        //auto generates the coral oredict entries
        final List<BlockCoralFull> tube   = new ImmutableList.Builder<BlockCoralFull>().add(ModBlocks.TUBE_CORAL_BLOCK  , ModBlocks.DEAD_TUBE_CORAL_BLOCK).build();
        final List<BlockCoralFull> brain  = new ImmutableList.Builder<BlockCoralFull>().add(ModBlocks.BRAIN_CORAL_BLOCK , ModBlocks.DEAD_BRAIN_CORAL_BLOCK).build();
        final List<BlockCoralFull> bubble = new ImmutableList.Builder<BlockCoralFull>().add(ModBlocks.BUBBLE_CORAL_BLOCK, ModBlocks.DEAD_BUBBLE_CORAL_BLOCK).build();
        final List<BlockCoralFull> fire   = new ImmutableList.Builder<BlockCoralFull>().add(ModBlocks.FIRE_CORAL_BLOCK  , ModBlocks.DEAD_FIRE_CORAL_BLOCK).build();
        final List<BlockCoralFull> horn   = new ImmutableList.Builder<BlockCoralFull>().add(ModBlocks.HORN_CORAL_BLOCK  , ModBlocks.DEAD_HORN_CORAL_BLOCK).build();
        final List<BlockCoralFull> all    = new ImmutableList.Builder<BlockCoralFull>().addAll(tube).addAll(brain).addAll(bubble).addAll(fire).addAll(horn).build();

        for(BlockCoralFull coral : all) {
            String type   = (tube.contains(coral) ? "Tube" : brain.contains(coral) ? "Brain" : bubble.contains(coral) ? "Bubble" : fire.contains(coral) ? "Fire" : "Horn");
            String isFull = (coral.getDefaultState().isFullCube() ? "Big"  : "Small");
            String isDead = (coral.isDead(null, null, null)       ? "Dead" : "Alive");

            OreDictionary.registerOre("coral",                          coral);
            OreDictionary.registerOre("coral" + type,                   coral);
            OreDictionary.registerOre("coral"        + isFull,          coral);
            OreDictionary.registerOre("coral"                 + isDead, coral);
            OreDictionary.registerOre("coral" + type + isFull,          coral);
            OreDictionary.registerOre("coral" + type          + isDead, coral);
            OreDictionary.registerOre("coral" +        isFull + isDead, coral);
            OreDictionary.registerOre("coral" + type + isFull + isDead, coral);
        }
    }
}
