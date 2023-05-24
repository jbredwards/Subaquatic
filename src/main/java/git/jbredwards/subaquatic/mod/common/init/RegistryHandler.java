package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.registries.DataSerializerEntry;

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
        SubaquaticBiomes.INIT.forEach(event.getRegistry()::register);
        SubaquaticBiomes.registerBiomeDictionary();
    }

    @SubscribeEvent
    static void registerBlocks(@Nonnull RegistryEvent.Register<Block> event) {
        SubaquaticBlocks.INIT.forEach(event.getRegistry()::register);
        SubaquaticBlocks.postRegistry();
    }

    @SubscribeEvent
    static void registerDataSerializers(@Nonnull RegistryEvent.Register<DataSerializerEntry> event) {
        SubaquaticDataSerializers.INIT.forEach(event.getRegistry()::register);
    }

    @SubscribeEvent
    static void registerEnchantments(@Nonnull RegistryEvent.Register<Enchantment> event) {
        SubaquaticEnchantments.INIT.forEach(event.getRegistry()::register);
    }

    @SubscribeEvent
    static void registerEntities(@Nonnull RegistryEvent.Register<EntityEntry> event) {
        SubaquaticEntities.INIT.forEach(event.getRegistry()::register);
        SubaquaticEntities.handleAdditionalEntityData();
    }

    @SubscribeEvent
    static void registerItems(@Nonnull RegistryEvent.Register<Item> event) {
        SubaquaticItems.INIT.forEach(event.getRegistry()::register);
        SubaquaticItems.postRegistry();
    }

    @SubscribeEvent
    static void registerPotions(@Nonnull RegistryEvent.Register<Potion> event) {
        SubaquaticPotions.INIT.forEach(event.getRegistry()::register);
    }

    @SubscribeEvent
    static void registerProfessions(@Nonnull RegistryEvent.Register<VillagerRegistry.VillagerProfession> event) {
        SubaquaticProfessions.INIT.forEach(event.getRegistry()::register);
        SubaquaticProfessions.handleAdditionalTrades();
    }

    @SubscribeEvent
    static void registerSounds(@Nonnull RegistryEvent.Register<SoundEvent> event) {
        SubaquaticSounds.INIT.forEach(event.getRegistry()::register);
    }
}
