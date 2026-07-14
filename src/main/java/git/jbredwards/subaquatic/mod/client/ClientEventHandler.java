/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.client.item.ICustomModel;
import git.jbredwards.subaquatic.mod.client.item.model.ModelContainerBoat;
import git.jbredwards.subaquatic.mod.client.item.model.ModelFishBucketOverlay;
import git.jbredwards.subaquatic.mod.client.item.model.ModelTropicalFishBucketOverlay;
import git.jbredwards.subaquatic.mod.client.particle.ParticleBubblePop;
import git.jbredwards.subaquatic.mod.common.compat.rpghud.RPGHudHandler;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticWaterColorConfig;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticItems;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Collection;

/**
 *
 * @author jbred
 *
 */
@Mod.EventBusSubscriber(modid = Subaquatic.MODID, value = Side.CLIENT)
public final class ClientEventHandler
{
    @SubscribeEvent
    static void registerModels(@Nonnull ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(ModelContainerBoat.Loader.INSTANCE);
        ModelLoaderRegistry.registerLoader(ModelFishBucketOverlay.Loader.INSTANCE);
        ModelLoaderRegistry.registerLoader(ModelTropicalFishBucketOverlay.Loader.INSTANCE);
        for(Block block : SubaquaticBlocks.INIT) if(block instanceof ICustomModel) ((ICustomModel)block).registerModels();
        for(Item item : SubaquaticItems.INIT) {
            if(item instanceof ICustomModel) ((ICustomModel)item).registerModels();
            else ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.delegate.name(), "inventory"));
        }

        //vanilla mushroom stems
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Blocks.BROWN_MUSHROOM_BLOCK), 0, new ModelResourceLocation(Blocks.BROWN_MUSHROOM_BLOCK.delegate.name(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Blocks.BROWN_MUSHROOM_BLOCK), 1, new ModelResourceLocation(new ResourceLocation(Subaquatic.MODID, "brown_mushroom_block_stem"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Blocks.BROWN_MUSHROOM_BLOCK), 2, new ModelResourceLocation(new ResourceLocation(Subaquatic.MODID, "brown_mushroom_block_stem_all"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Blocks.RED_MUSHROOM_BLOCK), 0, new ModelResourceLocation(Blocks.RED_MUSHROOM_BLOCK.delegate.name(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Blocks.RED_MUSHROOM_BLOCK), 1, new ModelResourceLocation(new ResourceLocation(Subaquatic.MODID, "red_mushroom_block_stem"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Blocks.RED_MUSHROOM_BLOCK), 2, new ModelResourceLocation(new ResourceLocation(Subaquatic.MODID, "red_mushroom_block_stem_all"), "inventory"));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    static void handleAirGuiOverlay(@Nonnull RenderGameOverlayEvent.Pre event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.AIR && (!Subaquatic.isRPGHudInstalled || RPGHudHandler.shouldRenderVanilla())) {
            Minecraft.getMinecraft().profiler.startSection("air");
            AirBubbleRenderer.renderAirBubbles(event.getResolution(), Minecraft.getMinecraft());
            Minecraft.getMinecraft().profiler.endSection();
            event.setCanceled(true);

            //don't potentially mess up other mod rendering
            MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Post(event, event.getType()));
        }
    }

    @SubscribeEvent(receiveCanceled = true)
    static void registerTextures(@Nonnull TextureStitchEvent.Pre event) {
        if(event.getMap() != Minecraft.getMinecraft().getTextureMapBlocks()) return;

        //ensure these sprites are always registered, as their use cases are hardcoded
        for(int i = 0; i < 5; i++) ParticleBubblePop.TEXTURES[i] = event.getMap().registerSprite(new ResourceLocation(Subaquatic.MODID, "particles/bubble_pop_" + i));

        //handle entity bucket sprites
        ModelTropicalFishBucketOverlay.registerSprites(event.getMap());
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    static void handleFluidParticleBaseColor(@Nonnull TextureStitchEvent.Post event) {
        if(event.getMap() != Minecraft.getMinecraft().getTextureMapBlocks()) return;

        Subaquatic.LOGGER.info("Attempting to gather the average pixel colors of each still fluid texture...");
        SubaquaticWaterColorConfig.FLUID_PIXEL_BASE_COLORS.clear();

        final Collection<Fluid> fluids = FluidRegistry.getRegisteredFluids().values();
        final ProgressManager.ProgressBar progressBar = ProgressManager.push("Fluid Texture Averages", fluids.size());

        fluids.forEach(fluid -> {
            final TextureAtlasSprite texture = event.getMap().getAtlasSprite(fluid.getStill().toString());
            long pixelRedSum = 0;
            long pixelGreenSum = 0;
            long pixelBlueSum = 0;
            int size = 0;

            progressBar.step(texture.getIconName());
            for(int frame = 0; frame < texture.getFrameCount(); frame++) {
                for(int[] textureData : texture.getFrameTextureData(frame)) {
                    for(int pixel : textureData) {
                        pixelRedSum   += pixel >> 16 & 255;
                        pixelGreenSum += pixel >> 8 & 255;
                        pixelBlueSum  += pixel & 255;
                        size++;
                    }
                }
            }

            SubaquaticWaterColorConfig.FLUID_PIXEL_BASE_COLORS.put(fluid,
                    new Color(pixelRedSum / 255f / size, pixelGreenSum / 255f / size, pixelBlueSum / 255f / size));
        });

        ProgressManager.pop(progressBar);
        Subaquatic.LOGGER.info("Success!");
    }
}
