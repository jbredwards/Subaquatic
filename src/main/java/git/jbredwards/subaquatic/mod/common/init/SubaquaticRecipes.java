package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticBoatTypesConfig;
import git.jbredwards.subaquatic.mod.common.item.ItemBoatContainer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 *
 * @author jbred
 *
 */
@Mod.EventBusSubscriber(modid = Subaquatic.MODID)
final class SubaquaticRecipes
{
    @SubscribeEvent
    static void registerRecipes(@Nonnull RegistryEvent.Register<IRecipe> event) throws IOException {
        registerCrafting(event.getRegistry());
        registerSmelting();
    }

    static void registerCrafting(@Nonnull IForgeRegistry<IRecipe> registry) throws IOException {
        //added recipes
        registerCrafting(registry, "blue_ice", new ShapedOreRecipe(null, SubaquaticItems.BLUE_ICE, "###", "###", "###", '#', Blocks.PACKED_ICE));
        registerCrafting(registry, "brown_mushroom_block", new ShapedOreRecipe(null, Blocks.BROWN_MUSHROOM_BLOCK, "##", "##", '#', Blocks.BROWN_MUSHROOM));
        registerCrafting(registry, "brown_mushroom_hyphae", new ShapedOreRecipe(null, new ItemStack(Blocks.BROWN_MUSHROOM_BLOCK, 3, 2), "##", "##", '#', new ItemStack(Blocks.BROWN_MUSHROOM_BLOCK, 1, 1)));
        registerCrafting(registry, "brown_mushroom_stem", new ShapedOreRecipe(null, new ItemStack(Blocks.BROWN_MUSHROOM_BLOCK, 1, 1), "##", "##", "##", '#', Blocks.BROWN_MUSHROOM));
        registerCrafting(registry, "dried_kelp", new ShapelessOreRecipe(null, new ItemStack(SubaquaticItems.DRIED_KELP, 9), SubaquaticBlocks.DRIED_KELP_BLOCK));
        registerCrafting(registry, "dried_kelp_block", new ShapedOreRecipe(null, SubaquaticItems.DRIED_KELP_BLOCK, "###", "###", "###", '#', "foodDriedKelp"));
        registerCrafting(registry, "ender_chest_minecart", new ShapedOreRecipe(null, SubaquaticItems.ENDER_CHEST_MINECART, "A", "B", 'A', Blocks.ENDER_CHEST, 'B', Items.MINECART));
        registerCrafting(registry, "packed_ice", new ShapedOreRecipe(null, Blocks.PACKED_ICE, "###", "###", "###", '#', Blocks.ICE));
        registerCrafting(registry, "packed_mud", new ShapelessOreRecipe(null, SubaquaticItems.PACKED_MUD, "mud", "cropWheat"));
        registerCrafting(registry, "packed_mud_bricks", new ShapedOreRecipe(null, new ItemStack(SubaquaticItems.PACKED_MUD_BRICKS, 4), "##", "##", '#', SubaquaticItems.PACKED_MUD));
        registerCrafting(registry, "packed_mud_bricks_slab", new ShapedOreRecipe(null, new ItemStack(SubaquaticItems.PACKED_MUD_BRICKS_SLAB, 6), "###", '#', SubaquaticItems.PACKED_MUD_BRICKS));
        registerCrafting(registry, "packed_mud_bricks_stairs", new ShapedOreRecipe(null, new ItemStack(SubaquaticItems.PACKED_MUD_BRICKS_STAIRS, 4), "#  ", "## ", "###", '#', SubaquaticItems.PACKED_MUD_BRICKS));
        registerCrafting(registry, "packed_mud_bricks_wall", new ShapedOreRecipe(null, new ItemStack(SubaquaticItems.PACKED_MUD_BRICKS_WALL, 6), "###", "###", '#', SubaquaticItems.PACKED_MUD_BRICKS));
        registerCrafting(registry, "pumpkin_pie", new ShapelessOreRecipe(null, Items.PUMPKIN_PIE, "cropPumpkin", Items.SUGAR, "egg"));
        registerCrafting(registry, "pumpkin_seeds", new ShapelessOreRecipe(null, new ItemStack(Items.PUMPKIN_SEEDS, 4), "cropPumpkin"));
        registerCrafting(registry, "red_mushroom_block", new ShapedOreRecipe(null, Blocks.RED_MUSHROOM_BLOCK, "##", "##", '#', Blocks.RED_MUSHROOM));
        registerCrafting(registry, "red_mushroom_hyphae", new ShapedOreRecipe(null, new ItemStack(Blocks.RED_MUSHROOM_BLOCK, 3, 2), "##", "##", '#', new ItemStack(Blocks.RED_MUSHROOM_BLOCK, 1, 1)));
        registerCrafting(registry, "red_mushroom_stem", new ShapedOreRecipe(null, new ItemStack(Blocks.RED_MUSHROOM_BLOCK, 1, 1), "##", "##", "##", '#', Blocks.RED_MUSHROOM));
        registerCrafting(registry, "rooted_dirt", new ShapelessOreRecipe(null, SubaquaticItems.ROOTED_DIRT, "dirt", SubaquaticItems.HANGING_ROOTS));
        registerCrafting(registry, "smooth_lapis_block_slab", new ShapedOreRecipe(null, new ItemStack(SubaquaticItems.SMOOTH_LAPIS_BLOCK_SLAB, 6), "###", '#', SubaquaticItems.SMOOTH_LAPIS_BLOCK));
        registerCrafting(registry, "smooth_lapis_block_stairs", new ShapedOreRecipe(null, new ItemStack(SubaquaticItems.SMOOTH_LAPIS_BLOCK_STAIRS, 4), "#  ", "## ", "###", '#', SubaquaticItems.SMOOTH_LAPIS_BLOCK));
        registerCrafting(registry, "smooth_quartz_block_slab", new ShapedOreRecipe(null, new ItemStack(SubaquaticItems.SMOOTH_QUARTZ_BLOCK_SLAB, 6), "###", '#', SubaquaticItems.SMOOTH_QUARTZ_BLOCK));
        registerCrafting(registry, "smooth_quartz_block_stairs", new ShapedOreRecipe(null, new ItemStack(SubaquaticItems.SMOOTH_QUARTZ_BLOCK_STAIRS, 4), "#  ", "## ", "###", '#', SubaquaticItems.SMOOTH_QUARTZ_BLOCK));
        registerCrafting(registry, "smooth_red_sandstone_slab", new ShapedOreRecipe(null, new ItemStack(SubaquaticItems.SMOOTH_RED_SANDSTONE_SLAB, 6), "###", '#', SubaquaticItems.SMOOTH_RED_SANDSTONE));
        registerCrafting(registry, "smooth_red_sandstone_stairs", new ShapedOreRecipe(null, new ItemStack(SubaquaticItems.SMOOTH_RED_SANDSTONE_STAIRS, 4), "#  ", "## ", "###", '#', SubaquaticItems.SMOOTH_RED_SANDSTONE));
        registerCrafting(registry, "smooth_sandstone_slab", new ShapedOreRecipe(null, new ItemStack(SubaquaticItems.SMOOTH_SANDSTONE_SLAB, 6), "###", '#', SubaquaticItems.SMOOTH_SANDSTONE));
        registerCrafting(registry, "smooth_sandstone_stairs", new ShapedOreRecipe(null, new ItemStack(SubaquaticItems.SMOOTH_SANDSTONE_STAIRS, 4), "#  ", "## ", "###", '#', SubaquaticItems.SMOOTH_SANDSTONE));
        registerCrafting(registry, "smooth_stone_stairs", new ShapedOreRecipe(null, new ItemStack(SubaquaticItems.SMOOTH_STONE_STAIRS, 4), "#  ", "## ", "###", '#', "stoneSmooth"));

        //boat containers
        SubaquaticBoatTypesConfig.buildBoatTypes();
        SubaquaticBoatTypesConfig.BOAT_TYPES.forEach(type -> {
            final ResourceLocation typeName = type.boat.delegate.name();
            final String recipeId = '/' + typeName.getNamespace() + '/' + typeName.getPath() + '/' + type.boatMeta;

            //chest boat recipes
            registerCrafting(registry, "chest_boat" + recipeId, new ShapedOreRecipe(null,
                    ItemBoatContainer.createStackWithType(SubaquaticItems.CHEST_BOAT, type),
                    "C", "B", 'C', Blocks.CHEST, 'B', new ItemStack(type.boat, 1, type.boatMeta)));

            //ender chest boat recipes
            registerCrafting(registry, "ender_chest_boat" + recipeId, new ShapedOreRecipe(null,
                    ItemBoatContainer.createStackWithType(SubaquaticItems.ENDER_CHEST_BOAT, type),
                    "C", "B", 'C', Blocks.ENDER_CHEST, 'B', new ItemStack(type.boat, 1, type.boatMeta)));

            //furnace boat recipes
            registerCrafting(registry, "furnace_boat" + recipeId, new ShapedOreRecipe(null,
                    ItemBoatContainer.createStackWithType(SubaquaticItems.FURNACE_BOAT, type),
                    "C", "B", 'C', Blocks.FURNACE, 'B', new ItemStack(type.boat, 1, type.boatMeta)));
        });

        //replace the stone in certain recipes with smooth stone
        for(IRecipe recipe : new IRecipe[] { registry.getValue(new ResourceLocation("comparator")), registry.getValue(new ResourceLocation("repeater")), registry.getValue(new ResourceLocation("stone_slab")) })
            if(recipe instanceof ShapedRecipes) ((ShapedRecipes)recipe).recipeItems.replaceAll(ingredient -> ingredient.apply(new ItemStack(Blocks.STONE)) ? new OreIngredient("stoneSmooth") : ingredient);
    }

    static void registerCrafting(@Nonnull IForgeRegistry<IRecipe> registry, @Nonnull String id, @Nonnull IRecipe recipe) {
        registry.register(recipe.setRegistryName(new ResourceLocation(Subaquatic.MODID, id)));
    }

    @SubscribeEvent
    static void registerFuels(@Nonnull FurnaceFuelBurnTimeEvent event) {
        if(event.getItemStack().getItem() == SubaquaticItems.DRIED_KELP_BLOCK) event.setBurnTime(4000);
    }

    static void registerSmelting() {
        GameRegistry.addSmelting(SubaquaticItems.COD, new ItemStack(SubaquaticItems.COOKED_COD), 0.35f);
        GameRegistry.addSmelting(SubaquaticItems.KELP, new ItemStack(SubaquaticItems.DRIED_KELP), 0.1f);
        GameRegistry.addSmelting(SubaquaticItems.SEA_PICKLE, new ItemStack(Items.DYE, 1, 10), 0.1f);
        GameRegistry.addSmelting(Blocks.LAPIS_BLOCK, new ItemStack(SubaquaticItems.SMOOTH_LAPIS_BLOCK, 1, 0), 0.1f);
        GameRegistry.addSmelting(Blocks.QUARTZ_BLOCK, new ItemStack(SubaquaticItems.SMOOTH_QUARTZ_BLOCK, 1, 0), 0.1f);
        GameRegistry.addSmelting(Blocks.RED_SANDSTONE, new ItemStack(SubaquaticItems.SMOOTH_RED_SANDSTONE, 1, 0), 0.1f);
        GameRegistry.addSmelting(Blocks.SANDSTONE, new ItemStack(SubaquaticItems.SMOOTH_SANDSTONE, 1, 0), 0.1f);
        GameRegistry.addSmelting(new ItemStack(Blocks.STONE, 1, 0), new ItemStack(SubaquaticItems.SMOOTH_STONE, 1, 0), 0.1f);

        //corals
        GameRegistry.addSmelting(new ItemStack(SubaquaticItems.BRAIN_CORAL_BLOCK, 1, 0), new ItemStack(SubaquaticItems.BRAIN_CORAL_BLOCK, 1, 1), 0.1f);
        GameRegistry.addSmelting(new ItemStack(SubaquaticItems.BRAIN_CORAL_FAN, 1, 0), new ItemStack(SubaquaticItems.BRAIN_CORAL_FAN, 1, 1), 0.1f);
        GameRegistry.addSmelting(new ItemStack(SubaquaticItems.BRAIN_CORAL_FIN, 1, 0), new ItemStack(SubaquaticItems.BRAIN_CORAL_FIN, 1, 1), 0.1f);
        GameRegistry.addSmelting(new ItemStack(SubaquaticItems.BUBBLE_CORAL_BLOCK, 1, 0), new ItemStack(SubaquaticItems.BUBBLE_CORAL_BLOCK, 1, 1), 0.1f);
        GameRegistry.addSmelting(new ItemStack(SubaquaticItems.BUBBLE_CORAL_FAN, 1, 0), new ItemStack(SubaquaticItems.BUBBLE_CORAL_FAN, 1, 1), 0.1f);
        GameRegistry.addSmelting(new ItemStack(SubaquaticItems.BUBBLE_CORAL_FIN, 1, 0), new ItemStack(SubaquaticItems.BUBBLE_CORAL_FIN, 1, 1), 0.1f);
        GameRegistry.addSmelting(new ItemStack(SubaquaticItems.FIRE_CORAL_BLOCK, 1, 0), new ItemStack(SubaquaticItems.FIRE_CORAL_BLOCK, 1, 1), 0.1f);
        GameRegistry.addSmelting(new ItemStack(SubaquaticItems.FIRE_CORAL_FAN, 1, 0), new ItemStack(SubaquaticItems.FIRE_CORAL_FAN, 1, 1), 0.1f);
        GameRegistry.addSmelting(new ItemStack(SubaquaticItems.FIRE_CORAL_FIN, 1, 0), new ItemStack(SubaquaticItems.FIRE_CORAL_FIN, 1, 1), 0.1f);
        GameRegistry.addSmelting(new ItemStack(SubaquaticItems.HORN_CORAL_BLOCK, 1, 0), new ItemStack(SubaquaticItems.HORN_CORAL_BLOCK, 1, 1), 0.1f);
        GameRegistry.addSmelting(new ItemStack(SubaquaticItems.HORN_CORAL_FAN, 1, 0), new ItemStack(SubaquaticItems.HORN_CORAL_FAN, 1, 1), 0.1f);
        GameRegistry.addSmelting(new ItemStack(SubaquaticItems.HORN_CORAL_FIN, 1, 0), new ItemStack(SubaquaticItems.HORN_CORAL_FIN, 1, 1), 0.1f);
        GameRegistry.addSmelting(new ItemStack(SubaquaticItems.TUBE_CORAL_BLOCK, 1, 0), new ItemStack(SubaquaticItems.TUBE_CORAL_BLOCK, 1, 1), 0.1f);
        GameRegistry.addSmelting(new ItemStack(SubaquaticItems.TUBE_CORAL_FAN, 1, 0), new ItemStack(SubaquaticItems.TUBE_CORAL_FAN, 1, 1), 0.1f);
        GameRegistry.addSmelting(new ItemStack(SubaquaticItems.TUBE_CORAL_FIN, 1, 0), new ItemStack(SubaquaticItems.TUBE_CORAL_FIN, 1, 1), 0.1f);
    }
}
