package git.jbredwards.subaquatic.common.init;

import git.jbredwards.subaquatic.common.block.*;
import git.jbredwards.subaquatic.common.block.vanilla.BlockCoralFull;
import git.jbredwards.subaquatic.common.block.vanilla.BlockKelp;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * stores all of this mod's blocks
 * @author jbred
 *
 */
public final class ModBlocks
{
    //block init
    public static final List<Block> INIT = new ArrayList<>();

    //block
    public static final Block DRIED_KELP_BLOCK = register("dried_kelp_block", new Properties<>().resistance(2.5f).hardness(0.5f), new Block(Material.GRASS, MapColor.BLACK_STAINED_HARDENED_CLAY));

    //coral stuff
    public static final BlockCoralFull TUBE_CORAL_BLOCK =   register("tube_coral_block",   new Properties<>().from(Blocks.STONE).sound(ModSounds.CORAL), new BlockCoralFull(FluidRegistry.WATER, Material.CORAL, MapColor.BLUE));
    public static final BlockCoralFull BRAIN_CORAL_BLOCK =  register("brain_coral_block",  new Properties<>().from(Blocks.STONE).sound(ModSounds.CORAL), new BlockCoralFull(FluidRegistry.WATER, Material.CORAL, MapColor.PINK));
    public static final BlockCoralFull BUBBLE_CORAL_BLOCK = register("bubble_coral_block", new Properties<>().from(Blocks.STONE).sound(ModSounds.CORAL), new BlockCoralFull(FluidRegistry.WATER, Material.CORAL, MapColor.PURPLE));
    public static final BlockCoralFull FIRE_CORAL_BLOCK =   register("fire_coral_block",   new Properties<>().from(Blocks.STONE).sound(ModSounds.CORAL), new BlockCoralFull(FluidRegistry.WATER, Material.CORAL, MapColor.RED));
    public static final BlockCoralFull HORN_CORAL_BLOCK =   register("horn_coral_block",   new Properties<>().from(Blocks.STONE).sound(ModSounds.CORAL), new BlockCoralFull(FluidRegistry.WATER, Material.CORAL, MapColor.YELLOW));
    public static final BlockCoralFull DEAD_TUBE_CORAL_BLOCK =   register("dead_tube_coral_block",   new Properties<BlockCoralFull>().from(Blocks.STONE).func(TUBE_CORAL_BLOCK::setCorresponding),   new BlockCoralFull(null, Material.ROCK));
    public static final BlockCoralFull DEAD_BRAIN_CORAL_BLOCK =  register("dead_brain_coral_block",  new Properties<BlockCoralFull>().from(Blocks.STONE).func(BRAIN_CORAL_BLOCK::setCorresponding),  new BlockCoralFull(null, Material.ROCK));
    public static final BlockCoralFull DEAD_BUBBLE_CORAL_BLOCK = register("dead_bubble_coral_block", new Properties<BlockCoralFull>().from(Blocks.STONE).func(BUBBLE_CORAL_BLOCK::setCorresponding), new BlockCoralFull(null, Material.ROCK));
    public static final BlockCoralFull DEAD_FIRE_CORAL_BLOCK =   register("dead_fire_coral_block",   new Properties<BlockCoralFull>().from(Blocks.STONE).func(FIRE_CORAL_BLOCK::setCorresponding),   new BlockCoralFull(null, Material.ROCK));
    public static final BlockCoralFull DEAD_HORN_CORAL_BLOCK =   register("dead_horn_coral_block",   new Properties<BlockCoralFull>().from(Blocks.STONE).func(HORN_CORAL_BLOCK::setCorresponding),   new BlockCoralFull(null, Material.ROCK));

    //item-blocks
    public static final BlockKelp KELP =                    register("kelp"          , new BlockKelp(FluidRegistry.WATER, Material.WATER));
    public static final BlockNautilusShell NAUTILUS_SHELL = register("nautilus_shell", new BlockNautilusShell(Material.CORAL, MapColor.BROWN));

    //registry
    @Nonnull static <T extends Block> T register(@Nonnull String name, @Nonnull T block) { return register(name, block, b -> {}); }
    @Nonnull static <T extends Block> T register(@Nonnull String name, @Nonnull T block, @Nonnull Consumer<T> properties) {
        INIT.add(block.setRegistryName(Constants.MODID, name).setTranslationKey(Constants.MODID + "." + name));
        block.setCreativeTab(CreativeTab.INSTANCE);
        properties.accept(block);
        return block;
    }

    //copy properties from parent prior to registration
    @Nonnull static <T extends Block> T register(@Nonnull String name, @Nonnull T block, @Nonnull Block parent) { return register(name, block, parent, b -> {}); }
    @SuppressWarnings("ConstantConditions")
    @Nonnull static <T extends Block> T register(@Nonnull String name, @Nonnull T block, @Nonnull Block parent, @Nonnull Consumer<T> properties) {
        block.setResistance(parent.getExplosionResistance(null) * 5 / 3)
                .setHardness(parent.getDefaultState().getBlockHardness(null, null))
                .setLightLevel(parent.getDefaultState().getLightValue() / 15f)
                .setSoundType(parent.getSoundType())
                .setHarvestLevel(parent.getHarvestTool(parent.getDefaultState()), parent.getHarvestLevel(parent.getDefaultState()));

        return register(name, block, properties);
    }
}
