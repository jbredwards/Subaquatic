package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.block.*;
import git.jbredwards.subaquatic.mod.common.block.BlockCoral;
import git.jbredwards.subaquatic.mod.common.block.BlockKelp;
import git.jbredwards.subaquatic.mod.common.block.material.MaterialOceanPlant;
import git.jbredwards.subaquatic.mod.common.item.SubaquaticCreativeTab;
import git.jbredwards.subaquatic.mod.common.tileentity.TileEntityGlowLichen;
import net.minecraft.block.*;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nonnull;
import java.util.LinkedList;
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
    @Nonnull public static final List<Block> INIT = new LinkedList<>();

    // Materials
    @Nonnull public static final MaterialLiquid BUBBLE_COLUMN_MATERIAL = new MaterialLiquid(MapColor.WATER);
    @Nonnull public static final MaterialOceanPlant OCEAN_PLANT_MATERIAL = new MaterialOceanPlant(MapColor.WATER);
    @Nonnull public static final Material FROGLIGHT_MATERIAL = new Material(MapColor.AIR);

    // Map Colors
    @Nonnull public static final MapColor GLOW_LICHEN_MAP_COLOR = new MapColor(61, 0x7FA796); //same id as vanilla, so hopefully no conflicts...?

    // Blocks
    @Nonnull public static final BlockPackedIce BLUE_ICE = register("blue_ice", new BlockPackedIce(), Blocks.PACKED_ICE, block -> block.setDefaultSlipperiness(0.989f));
    @Nonnull public static final BlockRotatedPillar DRIED_KELP_BLOCK = register("dried_kelp_block", new BlockRotatedPillar(Material.GRASS, MapColor.BROWN), block -> block.setSoundType(SoundType.PLANT).setHardness(0.5f).setResistance(2.5f / 3));
    @Nonnull public static final BlockCarvablePumpkin PUMPKIN = register("pumpkin", new BlockCarvablePumpkin(Material.GOURD, MapColor.ADOBE), Blocks.PUMPKIN, block -> ((BlockStem)Blocks.PUMPKIN_STEM).crop = block.setTranslationKey("pumpkin"));
    @Nonnull public static final BlockKelp KELP = register("kelp", new BlockKelp(OCEAN_PLANT_MATERIAL), block -> block.setSoundType(SubaquaticSounds.WET_GRASS).setLightOpacity(1));
    @Nonnull public static final BlockSeagrass SEAGRASS = register("seagrass", new BlockSeagrass(OCEAN_PLANT_MATERIAL), block -> block.setSoundType(SubaquaticSounds.WET_GRASS).setLightOpacity(1));
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
    @Nonnull public static final BlockBubbleColumn BUBBLE_COLUMN_DOWN = register("bubble_column_down", new BlockBubbleColumn(BUBBLE_COLUMN_MATERIAL, true), block -> block.setLightOpacity(1));
    @Nonnull public static final BlockBubbleColumn BUBBLE_COLUMN_UP = register("bubble_column_up", new BlockBubbleColumn(BUBBLE_COLUMN_MATERIAL, false), block -> block.setLightOpacity(1));
    @Nonnull public static final BlockGlowLichen GLOW_LICHEN = register("glow_lichen", new BlockGlowLichen(Material.VINE, GLOW_LICHEN_MAP_COLOR), block -> block.setSoundType(SoundType.PLANT).setLightLevel(7f/15).setHardness(0.2f).setResistance(1f/3).setHarvestLevel("axe", 0));
    @Nonnull public static final BlockFroglight FROGLIGHT = register("froglight", new BlockFroglight(FROGLIGHT_MATERIAL), block -> block.setSoundType(SubaquaticSounds.FROGLIGHT).setLightLevel(1).setHardness(0.3f).setResistance(0.5f));
    @Nonnull public static final BlockRootedDirt ROOTED_DIRT = register("rooted_dirt", new BlockRootedDirt(Material.GROUND), Blocks.DIRT, block -> block.setSoundType(SubaquaticSounds.ROOTED_DIRT));
    @Nonnull public static final BlockHangingRoots HANGING_ROOTS = register("hanging_roots", new BlockHangingRoots(Material.VINE, MapColor.DIRT), block -> block.setSoundType(SubaquaticSounds.HANGING_ROOTS));
    @Nonnull public static final BlockMud MUD = register("mud", new BlockMud(Material.GROUND, MapColor.CYAN_STAINED_HARDENED_CLAY), Blocks.DIRT, block -> block.setSoundType(SubaquaticSounds.MUD));
    @Nonnull public static final Block PACKED_MUD = register("packed_mud", new Block(Material.GROUND), Blocks.DIRT, block -> block.setSoundType(SubaquaticSounds.PACKED_MUD).setHardness(1).setResistance(5));
    @Nonnull public static final Block PACKED_MUD_BRICKS = register("packed_mud_bricks", new Block(Material.ROCK, MapColor.SILVER_STAINED_HARDENED_CLAY), block -> block.setSoundType(SubaquaticSounds.PACKED_MUD_BRICKS).setHardness(1.5f).setResistance(5).setHarvestLevel("pickaxe", 0));
    @Nonnull public static final BlockStairs PACKED_MUD_BRICKS_STAIRS = register("packed_mud_bricks_stairs", new BlockStairs(PACKED_MUD_BRICKS.getDefaultState()), PACKED_MUD_BRICKS, block -> block.useNeighborBrightness = true);
    @Nonnull public static final BlockSlabTypeless PACKED_MUD_BRICKS_SLAB = register("packed_mud_bricks_slab", new BlockSlabTypeless.Single(Material.ROCK, MapColor.SILVER_STAINED_HARDENED_CLAY, () -> SubaquaticItems.PACKED_MUD_BRICKS_SLAB), PACKED_MUD_BRICKS);
    @Nonnull public static final BlockSlabTypeless PACKED_MUD_BRICKS_SLAB_DOUBLE = register("packed_mud_bricks_slab_double", new BlockSlabTypeless.Double(Material.ROCK, MapColor.SILVER_STAINED_HARDENED_CLAY, () -> SubaquaticItems.PACKED_MUD_BRICKS_SLAB), PACKED_MUD_BRICKS);
    @Nonnull public static final BlockWallTypeless PACKED_MUD_BRICKS_WALL = register("packed_mud_bricks_wall", new BlockWallTypeless(PACKED_MUD_BRICKS), PACKED_MUD_BRICKS);
    @Nonnull public static final Block SMOOTH_STONE = register("smooth_stone", new Block(Material.ROCK), Blocks.STONE);
    @Nonnull public static final BlockStairs SMOOTH_STONE_STAIRS = register("smooth_stone_stairs", new BlockStairs(SMOOTH_STONE.getDefaultState()), SMOOTH_STONE, block -> block.useNeighborBrightness = true);
    @Nonnull public static final Block SMOOTH_SANDSTONE = register("smooth_sandstone", new Block(Material.ROCK, MapColor.SAND), Blocks.SANDSTONE);
    @Nonnull public static final BlockStairs SMOOTH_SANDSTONE_STAIRS = register("smooth_sandstone_stairs", new BlockStairs(SMOOTH_SANDSTONE.getDefaultState()), SMOOTH_SANDSTONE, block -> block.useNeighborBrightness = true);
    @Nonnull public static final BlockSlabTypeless SMOOTH_SANDSTONE_SLAB = register("smooth_sandstone_slab", new BlockSlabTypeless.Single(Material.ROCK, MapColor.SAND, () -> SubaquaticItems.SMOOTH_SANDSTONE_SLAB), SMOOTH_SANDSTONE);
    @Nonnull public static final BlockSlabTypeless SMOOTH_SANDSTONE_SLAB_DOUBLE = register("smooth_sandstone_slab_double", new BlockSlabTypeless.Double(Material.ROCK, MapColor.SAND, () -> SubaquaticItems.SMOOTH_SANDSTONE_SLAB), SMOOTH_SANDSTONE);
    @Nonnull public static final Block SMOOTH_RED_SANDSTONE = register("smooth_red_sandstone", new Block(Material.ROCK, MapColor.ADOBE), Blocks.RED_SANDSTONE);
    @Nonnull public static final BlockStairs SMOOTH_RED_SANDSTONE_STAIRS = register("smooth_red_sandstone_stairs", new BlockStairs(SMOOTH_SANDSTONE.getDefaultState()), SMOOTH_RED_SANDSTONE, block -> block.useNeighborBrightness = true);
    @Nonnull public static final BlockSlabTypeless SMOOTH_RED_SANDSTONE_SLAB = register("smooth_red_sandstone_slab", new BlockSlabTypeless.Single(Material.ROCK, MapColor.ADOBE, () -> SubaquaticItems.SMOOTH_RED_SANDSTONE_SLAB), SMOOTH_RED_SANDSTONE);
    @Nonnull public static final BlockSlabTypeless SMOOTH_RED_SANDSTONE_SLAB_DOUBLE = register("smooth_red_sandstone_slab_double", new BlockSlabTypeless.Double(Material.ROCK, MapColor.ADOBE, () -> SubaquaticItems.SMOOTH_RED_SANDSTONE_SLAB), SMOOTH_RED_SANDSTONE);
    @Nonnull public static final Block SMOOTH_QUARTZ_BLOCK = register("smooth_quartz_block", new Block(Material.ROCK, MapColor.QUARTZ), Blocks.QUARTZ_BLOCK);
    @Nonnull public static final BlockStairs SMOOTH_QUARTZ_BLOCK_STAIRS = register("smooth_quartz_block_stairs", new BlockStairs(SMOOTH_QUARTZ_BLOCK.getDefaultState()), SMOOTH_QUARTZ_BLOCK, block -> block.useNeighborBrightness = true);
    @Nonnull public static final BlockSlabTypeless SMOOTH_QUARTZ_BLOCK_SLAB = register("smooth_quartz_block_slab", new BlockSlabTypeless.Single(Material.ROCK, MapColor.QUARTZ, () -> SubaquaticItems.SMOOTH_QUARTZ_BLOCK_SLAB), SMOOTH_QUARTZ_BLOCK);
    @Nonnull public static final BlockSlabTypeless SMOOTH_QUARTZ_BLOCK_SLAB_DOUBLE = register("smooth_quartz_block_slab_double", new BlockSlabTypeless.Double(Material.ROCK, MapColor.QUARTZ, () -> SubaquaticItems.SMOOTH_QUARTZ_BLOCK_SLAB), SMOOTH_QUARTZ_BLOCK);
    @Nonnull public static final Block SMOOTH_LAPIS_BLOCK = register("smooth_lapis_block", new Block(Material.ROCK, MapColor.LAPIS), Blocks.LAPIS_BLOCK);
    @Nonnull public static final BlockStairs SMOOTH_LAPIS_BLOCK_STAIRS = register("smooth_lapis_block_stairs", new BlockStairs(SMOOTH_LAPIS_BLOCK.getDefaultState()), SMOOTH_LAPIS_BLOCK, block -> block.useNeighborBrightness = true);
    @Nonnull public static final BlockSlabTypeless SMOOTH_LAPIS_BLOCK_SLAB = register("smooth_lapis_block_slab", new BlockSlabTypeless.Single(Material.ROCK, MapColor.LAPIS, () -> SubaquaticItems.SMOOTH_LAPIS_BLOCK_SLAB), SMOOTH_LAPIS_BLOCK);
    @Nonnull public static final BlockSlabTypeless SMOOTH_LAPIS_BLOCK_SLAB_DOUBLE = register("smooth_lapis_block_slab_double", new BlockSlabTypeless.Double(Material.ROCK, MapColor.LAPIS, () -> SubaquaticItems.SMOOTH_LAPIS_BLOCK_SLAB), SMOOTH_LAPIS_BLOCK);

    //register burnables & tile entities
    public static void postRegistry() {
        Blocks.FIRE.setFireInfo(DRIED_KELP_BLOCK, 30, 60);
        TileEntity.register(Subaquatic.MODID + ":glow_lichen", TileEntityGlowLichen.class);
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
