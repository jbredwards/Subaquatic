package git.jbredwards.subaquatic.common.init;

import git.jbredwards.subaquatic.Subaquatic;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

    static void registerSmelting() {
        GameRegistry.addSmelting(ModItems.KELP, new ItemStack(ModItems.DRIED_KELP), 0.1f);
    }
}
