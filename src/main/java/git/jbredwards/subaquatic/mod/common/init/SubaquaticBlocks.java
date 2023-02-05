package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.block.*;
import git.jbredwards.subaquatic.mod.common.block.BlockCoral;
import git.jbredwards.subaquatic.mod.common.block.BlockKelp;
import git.jbredwards.subaquatic.mod.common.block.material.MaterialOceanPlant;
import git.jbredwards.subaquatic.mod.common.item.SubaquaticCreativeTab;
import net.minecraft.block.*;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
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
public final class SubaquaticBlocks
{
    // Init
    @Nonnull public static final List<Block> INIT = new ArrayList<>();

    // Materials
    @Nonnull public static final MaterialLiquid BUBBLE_COLUMN_MATERIAL = new MaterialLiquid(MapColor.WATER);
    @Nonnull public static final MaterialOceanPlant OCEAN_PLANT_MATERIAL = new MaterialOceanPlant(MapColor.WATER);

    // Blocks
    @Nonnull public static final BlockPackedIce BLUE_ICE = register("blue_ice", new BlockPackedIce(), Blocks.PACKED_ICE, block -> block.setDefaultSlipperiness(0.989f));
    @Nonnull public static final BlockRotatedPillar DRIED_KELP_BLOCK = register("dried_kelp_block", new BlockRotatedPillar(Material.GRASS, MapColor.BROWN), block -> block.setSoundType(SoundType.PLANT).setResistance(2.5f / 3).setHardness(0.5f));
    @Nonnull public static final BlockCarvablePumpkin PUMPKIN = register("pumpkin", new BlockCarvablePumpkin(Material.GOURD, MapColor.ADOBE), Blocks.PUMPKIN, block -> ((BlockStem)Blocks.PUMPKIN_STEM).crop = block.setTranslationKey("pumpkin"));
    @Nonnull public static final BlockKelp KELP = register("kelp", new BlockKelp(OCEAN_PLANT_MATERIAL), block -> block.setSoundType(SubaquaticSounds.WET_GRASS));
    @Nonnull public static final BlockSeagrass SEAGRASS = register("seagrass", new BlockSeagrass(OCEAN_PLANT_MATERIAL), block -> block.setSoundType(SubaquaticSounds.WET_GRASS));
    @Nonnull public static final BlockSeaPickle SEA_PICKLE = register("sea_pickle", new BlockSeaPickle(OCEAN_PLANT_MATERIAL, MapColor.GREEN), block -> block.setSoundType(SoundType.SLIME));
    @Nonnull public static final BlockNautilusShell NAUTILUS_SHELL = register("nautilus_shell", new BlockNautilusShell(Material.CORAL, MapColor.BROWN), block -> block.setSoundType(Blocks.BONE_BLOCK.getSoundType()));
    @Nonnull public static final BlockCoral TUBE_CORAL_BLOCK = register("tube_coral_block", new BlockCoral(FluidRegistry.WATER, Material.CORAL, MapColor.BLUE), Blocks.STONE);
    @Nonnull public static final BlockCoral BRAIN_CORAL_BLOCK = register("brain_coral_block", new BlockCoral(FluidRegistry.WATER, Material.CORAL, MapColor.PINK), Blocks.STONE);
    @Nonnull public static final BlockCoral BUBBLE_CORAL_BLOCK = register("bubble_coral_block", new BlockCoral(FluidRegistry.WATER, Material.CORAL, MapColor.PURPLE), Blocks.STONE);
    @Nonnull public static final BlockCoral FIRE_CORAL_BLOCK = register("fire_coral_block", new BlockCoral(FluidRegistry.WATER, Material.CORAL, MapColor.RED), Blocks.STONE);
    @Nonnull public static final BlockCoral HORN_CORAL_BLOCK = register("horn_coral_block", new BlockCoral(FluidRegistry.WATER, Material.CORAL, MapColor.YELLOW), Blocks.STONE);
    @Nonnull public static final BlockCoralFan TUBE_CORAL_FAN = register("tube_coral_fan", new BlockCoralFan(FluidRegistry.WATER, Material.CORAL, MapColor.BLUE), Blocks.STONE);
    @Nonnull public static final BlockCoralFan BRAIN_CORAL_FAN = register("brain_coral_fan", new BlockCoralFan(FluidRegistry.WATER, Material.CORAL, MapColor.PINK), Blocks.STONE);
    @Nonnull public static final BlockCoralFan BUBBLE_CORAL_FAN = register("bubble_coral_fan", new BlockCoralFan(FluidRegistry.WATER, Material.CORAL, MapColor.PURPLE), Blocks.STONE);
    @Nonnull public static final BlockCoralFan FIRE_CORAL_FAN = register("fire_coral_fan", new BlockCoralFan(FluidRegistry.WATER, Material.CORAL, MapColor.RED), Blocks.STONE);
    @Nonnull public static final BlockCoralFan HORN_CORAL_FAN = register("horn_coral_fan", new BlockCoralFan(FluidRegistry.WATER, Material.CORAL, MapColor.YELLOW), Blocks.STONE);
    @Nonnull public static final BlockCoralFin TUBE_CORAL_FIN = register("tube_coral_fin", new BlockCoralFin(FluidRegistry.WATER, Material.CORAL, MapColor.BLUE), Blocks.STONE);
    @Nonnull public static final BlockCoralFin BRAIN_CORAL_FIN = register("brain_coral_fin", new BlockCoralFin(FluidRegistry.WATER, Material.CORAL, MapColor.PINK), Blocks.STONE);
    @Nonnull public static final BlockCoralFin BUBBLE_CORAL_FIN = register("bubble_coral_fin", new BlockCoralFin(FluidRegistry.WATER, Material.CORAL, MapColor.PURPLE), Blocks.STONE);
    @Nonnull public static final BlockCoralFin FIRE_CORAL_FIN = register("fire_coral_fin", new BlockCoralFin(FluidRegistry.WATER, Material.CORAL, MapColor.RED), Blocks.STONE);
    @Nonnull public static final BlockCoralFin HORN_CORAL_FIN = register("horn_coral_fin", new BlockCoralFin(FluidRegistry.WATER, Material.CORAL, MapColor.YELLOW), Blocks.STONE);
    //TODO @Nonnull public static final BlockBubbleColumn BUBBLE_COLUMN = register("bubble_column", new BlockBubbleColumn(BUBBLE_COLUMN_MATERIAL));

    //register burnables
    public static void registerBurnables() {
        Blocks.FIRE.setFireInfo(DRIED_KELP_BLOCK, 30, 60);
    }

    // Registry
    @Nonnull static <T extends Block> T register(@Nonnull String name, @Nonnull T block) { return register(name, block, b -> {}); }
    @Nonnull static <T extends Block> T register(@Nonnull String name, @Nonnull T block, @Nonnull Consumer<T> properties) {
        INIT.add(block.setRegistryName(Subaquatic.MODID, name).setTranslationKey(Subaquatic.MODID + "." + name).setCreativeTab(SubaquaticCreativeTab.INSTANCE));
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
