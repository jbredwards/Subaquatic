package git.jbredwards.fluidlogged_additions.common.init;

import git.jbredwards.fluidlogged_additions.common.block.BlockCoralFull;
import git.jbredwards.fluidlogged_additions.common.block.BlockNautilusShell;
import git.jbredwards.fluidlogged_additions.common.block.Properties;
import git.jbredwards.fluidlogged_additions.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jbred
 *
 */
public enum ModBlocks
{
    ;

    //block init
    public static final List<Block> INIT = new ArrayList<>();

    /*public static final BlockCoralFull TUBE_CORAL_BLOCK =   register("tube_coral_block", BlockCoralFull.class, new ItemBlock(new BlockCoralFull(FluidRegistry.WATER, Material.PLANTS, MapColor.BLUE)));
    public static final BlockCoralFull BRAIN_CORAL_BLOCK =  register("brain_coral_block", BlockCoralFull.class, new ItemBlock(new BlockCoralFull(FluidRegistry.WATER, Material.PLANTS, MapColor.PINK)));
    public static final BlockCoralFull BUBBLE_CORAL_BLOCK = register("bubble_coral_block", BlockCoralFull.class, new ItemBlock(new BlockCoralFull(FluidRegistry.WATER, Material.PLANTS, MapColor.PURPLE)));
    public static final BlockCoralFull FIRE_CORAL_BLOCK =   register("fire_coral_block", BlockCoralFull.class, new ItemBlock(new BlockCoralFull(FluidRegistry.WATER, Material.PLANTS, MapColor.RED)));
    public static final BlockCoralFull HORN_CORAL_BLOCK =   register("horn_coral_block", BlockCoralFull.class, new ItemBlock(new BlockCoralFull(FluidRegistry.WATER, Material.PLANTS, MapColor.YELLOW)));
    public static final BlockCoralFull DEAD_TUBE_CORAL_BLOCK =   register("dead_tube_coral_block", BlockCoralFull.class, new ItemBlock(new BlockCoralFull(null, Material.PLANTS, MapColor.BLUE)));
    public static final BlockCoralFull DEAD_BRAIN_CORAL_BLOCK =  register("dead_brain_coral_block", BlockCoralFull.class, new ItemBlock(new BlockCoralFull(null, Material.PLANTS, MapColor.PINK)));
    public static final BlockCoralFull DEAD_BUBBLE_CORAL_BLOCK = register("dead_bubble_coral_block", BlockCoralFull.class, new ItemBlock(new BlockCoralFull(null, Material.PLANTS, MapColor.PURPLE)));
    public static final BlockCoralFull DEAD_FIRE_CORAL_BLOCK =   register("dead_fire_coral_block", BlockCoralFull.class, new ItemBlock(new BlockCoralFull(null, Material.PLANTS, MapColor.RED)));
    public static final BlockCoralFull DEAD_HORN_CORAL_BLOCK =   register("dead_horn_coral_block", BlockCoralFull.class, new ItemBlock(new BlockCoralFull(null, Material.PLANTS, MapColor.YELLOW)));
*/
    public static final BlockNautilusShell NAUTILUS_SHELL = register("nautilus_shell", new BlockNautilusShell(Material.CORAL, MapColor.BROWN));

    //prepares the block for registration
    public static <B extends Block> B register(String name, B block) {
        block.setRegistryName(name).setUnlocalizedName(Constants.MODID + "." + name).setCreativeTab(CreativeTab.INSTANCE);
        INIT.add(block);

        return block;
    }

    //for setting up blocks
    public static <B extends Block> B setup(Properties prop, B block) {
        block.setHarvestLevel(prop.tool, prop.level);
        block.setHardness(prop.hardness);
        block.setResistance(prop.resistance);
        block.setLightLevel(prop.lightLevel);

        return block;
    }
}
