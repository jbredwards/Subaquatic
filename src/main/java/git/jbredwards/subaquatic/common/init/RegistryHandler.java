package git.jbredwards.subaquatic.common.init;

import git.jbredwards.subaquatic.Subaquatic;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 *
 * @author jbred
 *
 */
@Mod.EventBusSubscriber(modid = Subaquatic.MODID)
final class RegistryHandler
{
    @SubscribeEvent
    static void registerItems(@Nonnull RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ModItems.INIT.toArray(new Item[0]));
        ModItems.registerOreDict();
    }

    @SubscribeEvent
    static void registerBlocks(@Nonnull RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(ModBlocks.INIT.toArray(new Block[0]));
    }

    @SubscribeEvent
    static void registerSounds(@Nonnull RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(ModSounds.INIT.toArray(new SoundEvent[0]));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    static void registerModels(@Nonnull ModelRegistryEvent event) {
        for(Item item : ModItems.INIT)
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(
                    Objects.requireNonNull(item.getRegistryName()), "inventory"));
    }
}
