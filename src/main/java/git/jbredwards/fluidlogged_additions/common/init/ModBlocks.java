package git.jbredwards.fluidlogged_additions.common.init;

import git.jbredwards.fluidlogged_additions.common.block.BlockCoralFull;
import git.jbredwards.fluidlogged_additions.common.block.BlockNautilusShell;
import git.jbredwards.fluidlogged_additions.common.block.Properties;
import git.jbredwards.fluidlogged_additions.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
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

    //coral stuff
    public static final BlockCoralFull TUBE_CORAL_BLOCK =   register("tube_coral_block",   new Properties<>().from(Blocks.STONE).sound(ModSounds.CORAL), new BlockCoralFull(FluidRegistry.WATER, Material.CORAL, MapColor.BLUE));
    public static final BlockCoralFull BRAIN_CORAL_BLOCK =  register("brain_coral_block",  new Properties<>().from(Blocks.STONE).sound(ModSounds.CORAL), new BlockCoralFull(FluidRegistry.WATER, Material.CORAL, MapColor.PINK));
    public static final BlockCoralFull BUBBLE_CORAL_BLOCK = register("bubble_coral_block", new Properties<>().from(Blocks.STONE).sound(ModSounds.CORAL), new BlockCoralFull(FluidRegistry.WATER, Material.CORAL, MapColor.PURPLE));
    public static final BlockCoralFull FIRE_CORAL_BLOCK =   register("fire_coral_block",   new Properties<>().from(Blocks.STONE).sound(ModSounds.CORAL), new BlockCoralFull(FluidRegistry.WATER, Material.CORAL, MapColor.RED));
    public static final BlockCoralFull HORN_CORAL_BLOCK =   register("horn_coral_block",   new Properties<>().from(Blocks.STONE).sound(ModSounds.CORAL), new BlockCoralFull(FluidRegistry.WATER, Material.CORAL, MapColor.YELLOW));
    public static final BlockCoralFull DEAD_TUBE_CORAL_BLOCK =   register("dead_tube_coral_block",   new Properties<BlockCoralFull>().from(Blocks.STONE).run(b -> b.setCorresponding(TUBE_CORAL_BLOCK)),   new BlockCoralFull(null, Material.ROCK));
    public static final BlockCoralFull DEAD_BRAIN_CORAL_BLOCK =  register("dead_brain_coral_block",  new Properties<BlockCoralFull>().from(Blocks.STONE).run(b -> b.setCorresponding(BRAIN_CORAL_BLOCK)),  new BlockCoralFull(null, Material.ROCK));
    public static final BlockCoralFull DEAD_BUBBLE_CORAL_BLOCK = register("dead_bubble_coral_block", new Properties<BlockCoralFull>().from(Blocks.STONE).run(b -> b.setCorresponding(BUBBLE_CORAL_BLOCK)), new BlockCoralFull(null, Material.ROCK));
    public static final BlockCoralFull DEAD_FIRE_CORAL_BLOCK =   register("dead_fire_coral_block",   new Properties<BlockCoralFull>().from(Blocks.STONE).run(b -> b.setCorresponding(FIRE_CORAL_BLOCK)),   new BlockCoralFull(null, Material.ROCK));
    public static final BlockCoralFull DEAD_HORN_CORAL_BLOCK =   register("dead_horn_coral_block",   new Properties<BlockCoralFull>().from(Blocks.STONE).run(b -> b.setCorresponding(HORN_CORAL_BLOCK)),   new BlockCoralFull(null, Material.ROCK));
    public static final BlockNautilusShell NAUTILUS_SHELL = register("nautilus_shell", new BlockNautilusShell(Material.CORAL, MapColor.BROWN));

    //prepares the block for registration
    public static <B extends Block> B register(String name, B block) {
        block.setRegistryName(name).setUnlocalizedName(Constants.MODID + "." + name).setCreativeTab(CreativeTab.INSTANCE);
        INIT.add(block);

        return block;
    }

    //another way of preparing blocks for registration
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <B extends Block> B register(String name, Properties prop, B block) {
        block.setHardness(prop.hardness);
        block.setResistance(prop.resistance);
        block.setLightLevel(prop.lightLevel);
        block.setSoundType(prop.sound);

        if(prop.tool != null) block.setHarvestLevel(prop.tool, prop.level);
        if(prop.func != null) prop.func.accept(block);

        return register(name, block);
    }
}
