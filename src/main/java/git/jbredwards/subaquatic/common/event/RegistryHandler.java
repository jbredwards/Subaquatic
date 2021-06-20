package git.jbredwards.subaquatic.common.event;

import git.jbredwards.subaquatic.client.util.IModelProperties;
import git.jbredwards.subaquatic.common.init.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

/**
 *
 * @author jbred
 *
 */
public final class RegistryHandler
{
    @SuppressWarnings("unused")
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ModItems.INIT.toArray(new Item[0]));
        ModItems.registerOreDict();
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(ModBlocks.INIT.toArray(new Block[0]));
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(ModSounds.INIT.toArray(new SoundEvent[0]));
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().registerAll(ModCrafting.INIT.toArray(new IRecipe[0]));
    }

    @SuppressWarnings("unused")
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void registerModels(ModelRegistryEvent event) {
        //item model properties
        for(Item item : ModItems.INIT) {
            if(item instanceof IModelProperties) ((IModelProperties)item).setupModel(item);
            //items still need a manual model location by default
            else ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory"));
        }

        //block model properties
        for(Block block : ModBlocks.INIT) {
            if(block instanceof IModelProperties) ((IModelProperties)block).setupModel(block);
        }
    }
}
