package git.jbredwards.fluidlogged_additions.common.event;

import git.jbredwards.fluidlogged_additions.client.util.IModelProperties;
import git.jbredwards.fluidlogged_additions.common.init.ModBlocks;
import git.jbredwards.fluidlogged_additions.common.init.ModItems;
import git.jbredwards.fluidlogged_additions.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

import static git.jbredwards.fluidlogged_additions.util.Constants.MODID;

/**
 *
 * @author jbred
 *
 */
@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MODID)
public final class RegistryHandler
{
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ModItems.INIT.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(ModBlocks.INIT.toArray(new Block[0]));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
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
