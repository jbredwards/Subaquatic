package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
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
final class RegistryHandler
{
    @SubscribeEvent
    static void registerBiomes(@Nonnull RegistryEvent.Register<Biome> event) {
        ModBiomes.INIT.forEach(event.getRegistry()::register);
        ModBiomes.registerBiomeDictionary();
    }

    @SubscribeEvent
    static void registerBlocks(@Nonnull RegistryEvent.Register<Block> event) {
        ModBlocks.INIT.forEach(event.getRegistry()::register);
        ModBlocks.registerBurnables();
    }

    @SubscribeEvent
    static void registerItems(@Nonnull RegistryEvent.Register<Item> event) {
        ModItems.INIT.forEach(event.getRegistry()::register);
        ModItems.registerOreDictionary();
    }

    @SubscribeEvent
    static void registerPotions(@Nonnull RegistryEvent.Register<Potion> event) {
        ModPotions.INIT.forEach(event.getRegistry()::register);
    }

    @SubscribeEvent
    static void registerSounds(@Nonnull RegistryEvent.Register<SoundEvent> event) {
        ModSounds.INIT.forEach(event.getRegistry()::register);
    }
}
