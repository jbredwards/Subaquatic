package git.jbredwards.subaquatic.mod.client;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.client.item.ICustomModel;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import git.jbredwards.subaquatic.mod.common.init.ModBlocks;
import git.jbredwards.subaquatic.mod.common.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.awt.*;

/**
 *
 * @author jbred
 *
 */
@Mod.EventBusSubscriber(modid = Subaquatic.MODID, value = Side.CLIENT)
public final class ClientEventHandler
{
    @SubscribeEvent(receiveCanceled = true)
    public static void applyWaterSurfaceColor(@Nonnull BiomeEvent.GetWaterColor event) {
        if(SubaquaticConfigHandler.SURFACE_COLORS.containsKey(event.getBiome())) event.setNewColor(SubaquaticConfigHandler.SURFACE_COLORS.get(event.getBiome()));
        else { //blend old biome color with old vanilla hardcoded water texture color to try to better recreate the indented old biome colors
            final float[] biomeComp = new Color(event.getOriginalColor()).getColorComponents(new float[3]);
            event.setNewColor(new Color(
                    Math.min(1, biomeComp[0] * 76f/255 + 47f/255 * 179f/255),
                    Math.min(1, biomeComp[1] * 76f/255 + 67f/255 * 179f/255),
                    Math.min(1, biomeComp[2] * 76f/255 + 244f/255 * 179f/255))
                    .getRGB());
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    public static void applyWaterFogColor(@Nonnull EntityViewRenderEvent.FogColors event) {
        if(event.getState().getMaterial() == Material.WATER) {
            //checks the player's head is underwater
            if(FluidloggedUtils.isCompatibleFluid(FluidloggedUtils.getFluidFromState(event.getState()), FluidRegistry.WATER)) {
                final float[] fogComp = SubaquaticConfigHandler.getFogColorAt(event.getEntity().world, event.getEntity().getPosition(), Biome::getWaterColor);
                event.setRed(fogComp[0] * 0.5f + 0.5f * event.getRed());
                event.setGreen(fogComp[1] * 0.5f + 0.5f * event.getGreen());
                event.setBlue(fogComp[2] * 0.5f + 0.5f * event.getBlue());
            }
        }
    }

    @SubscribeEvent
    static void registerModels(@Nonnull ModelRegistryEvent event) {
        for(Block block : ModBlocks.INIT) if(block instanceof ICustomModel) ((ICustomModel)block).registerModels();
        for(Item item : ModItems.INIT) {
            if(item instanceof ICustomModel) ((ICustomModel)item).registerModels();
            else ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.delegate.name(), "inventory"));
        }
    }

    @SubscribeEvent
    static void registerTextures(@Nonnull TextureStitchEvent.Pre event) {
        if(event.getMap() == Minecraft.getMinecraft().getTextureMapBlocks()) {
            //ensure these sprites are always registered, as their use cases are hardcoded
            event.getMap().registerSprite(new ResourceLocation(Subaquatic.MODID, "blocks/water_flow"));
            event.getMap().registerSprite(new ResourceLocation(Subaquatic.MODID, "blocks/water_overlay"));
            event.getMap().registerSprite(new ResourceLocation(Subaquatic.MODID, "blocks/water_still"));
            event.getMap().registerSprite(new ResourceLocation(Subaquatic.MODID, "misc/underwater"));
        }
    }
}
