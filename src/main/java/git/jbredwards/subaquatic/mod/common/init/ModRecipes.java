package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@Mod.EventBusSubscriber(modid = Subaquatic.MODID)
final class ModRecipes
{
    //funnily enough the only recipes this doesn't handle atm are IRecipe instances
    @SubscribeEvent
    static void registerRecipes(@Nonnull RegistryEvent.Register<IRecipe> event) {
        registerSmelting();
    }

    @SubscribeEvent
    static void registerFuels(@Nonnull FurnaceFuelBurnTimeEvent event) {
        if(event.getItemStack().getItem() == ModItems.DRIED_KELP_BLOCK) event.setBurnTime(4000);
    }

    static void registerSmelting() {
        GameRegistry.addSmelting(ModItems.KELP, new ItemStack(ModItems.DRIED_KELP), 0.1f);
        GameRegistry.addSmelting(new ItemStack(ModItems.BRAIN_CORAL_BLOCK, 1, 0), new ItemStack(ModItems.BRAIN_CORAL_BLOCK, 1, 1), 0.2f);
        GameRegistry.addSmelting(new ItemStack(ModItems.BUBBLE_CORAL_BLOCK, 1, 0), new ItemStack(ModItems.BUBBLE_CORAL_BLOCK, 1, 1), 0.2f);
        GameRegistry.addSmelting(new ItemStack(ModItems.FIRE_CORAL_BLOCK, 1, 0), new ItemStack(ModItems.FIRE_CORAL_BLOCK, 1, 1), 0.2f);
        GameRegistry.addSmelting(new ItemStack(ModItems.HORN_CORAL_BLOCK, 1, 0), new ItemStack(ModItems.HORN_CORAL_BLOCK, 1, 1), 0.2f);
        GameRegistry.addSmelting(new ItemStack(ModItems.TUBE_CORAL_BLOCK, 1, 0), new ItemStack(ModItems.TUBE_CORAL_BLOCK, 1, 1), 0.2f);
    }
}
