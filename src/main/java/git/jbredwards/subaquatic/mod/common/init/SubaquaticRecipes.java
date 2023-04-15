package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticBoatTypesConfig;
import git.jbredwards.subaquatic.mod.common.item.ItemBoatContainer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
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
        registerCrafting(registry, "dried_kelp", new ShapelessOreRecipe(null, new ItemStack(SubaquaticItems.DRIED_KELP, 9), SubaquaticBlocks.DRIED_KELP_BLOCK));
        registerCrafting(registry, "dried_kelp_block", new ShapedOreRecipe(null, SubaquaticItems.DRIED_KELP_BLOCK, "###", "###", "###", '#', "foodDriedKelp"));
        registerCrafting(registry, "ender_chest_minecart", new ShapedOreRecipe(null, SubaquaticItems.ENDER_CHEST_MINECART, "A", "B", 'A', Blocks.ENDER_CHEST, 'B', Items.MINECART));
        registerCrafting(registry, "packed_ice", new ShapedOreRecipe(null, Blocks.PACKED_ICE, "###", "###", "###", '#', Blocks.ICE));
        registerCrafting(registry, "packed_mud", new ShapelessOreRecipe(null, SubaquaticItems.PACKED_MUD, "mud", "cropWheat"));
        registerCrafting(registry, "packed_mud_bricks", new ShapedOreRecipe(null, new ItemStack(SubaquaticItems.PACKED_MUD_BRICKS, 4), "##", "##", '#', SubaquaticItems.PACKED_MUD));
        registerCrafting(registry, "pumpkin_pie", new ShapelessOreRecipe(null, Items.PUMPKIN_PIE, "cropPumpkin", Items.SUGAR, "egg"));
        registerCrafting(registry, "pumpkin_seeds", new ShapelessOreRecipe(null, new ItemStack(Items.PUMPKIN_SEEDS, 4), "cropPumpkin"));
        registerCrafting(registry, "rooted_dirt", new ShapelessOreRecipe(null, SubaquaticItems.ROOTED_DIRT, "dirt", SubaquaticItems.HANGING_ROOTS));

        //boat containers
        SubaquaticBoatTypesConfig.buildBoatTypes();
        SubaquaticBoatTypesConfig.BOAT_TYPES.forEach(type -> {
            final ResourceLocation typeName = type.boat.delegate.name();
            final String recipeId = '/' + typeName.getNamespace() + '/' + typeName.getPath() + '/' + type.boatMeta;

            //chest boat recipes
            registry.register(new ShapedOreRecipe(null,
                    ItemBoatContainer.createStackWithType(SubaquaticItems.CHEST_BOAT, type),
                    "C", "B", 'C', Blocks.CHEST, 'B', new ItemStack(type.boat, 1, type.boatMeta))
                    .setRegistryName(Subaquatic.MODID, "chest_boat" + recipeId));

            //ender chest boat recipes
            registry.register(new ShapedOreRecipe(null,
                    ItemBoatContainer.createStackWithType(SubaquaticItems.ENDER_CHEST_BOAT, type),
                    "C", "B", 'C', Blocks.ENDER_CHEST, 'B', new ItemStack(type.boat, 1, type.boatMeta))
                    .setRegistryName(Subaquatic.MODID, "ender_chest_boat" + recipeId));

            //furnace boat recipes
            registry.register(new ShapedOreRecipe(null,
                    ItemBoatContainer.createStackWithType(SubaquaticItems.FURNACE_BOAT, type),
                    "C", "B", 'C', Blocks.FURNACE, 'B', new ItemStack(type.boat, 1, type.boatMeta))
                    .setRegistryName(Subaquatic.MODID, "furnace_boat" + recipeId));
        });
    }

    static void registerCrafting(@Nonnull IForgeRegistry<IRecipe> registry, @Nonnull String id, @Nonnull IRecipe recipe) {
        registry.register(recipe.setRegistryName(new ResourceLocation(Subaquatic.MODID, id)));
    }

    @SubscribeEvent
    static void registerFuels(@Nonnull FurnaceFuelBurnTimeEvent event) {
        if(event.getItemStack().getItem() == SubaquaticItems.DRIED_KELP_BLOCK) event.setBurnTime(4000);
    }

    static void registerSmelting() {
        GameRegistry.addSmelting(SubaquaticItems.KELP, new ItemStack(SubaquaticItems.DRIED_KELP), 0.1f);
        GameRegistry.addSmelting(SubaquaticItems.SEA_PICKLE, new ItemStack(Items.DYE, 1, 10), 0.1f);
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
