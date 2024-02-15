/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.client.item.ICustomModel;
import git.jbredwards.subaquatic.mod.client.item.model.BakedEntityBucketModel;
import git.jbredwards.subaquatic.mod.client.item.model.ModelContainerBoat;
import git.jbredwards.subaquatic.mod.client.particle.ParticleBubblePop;
import git.jbredwards.subaquatic.mod.common.capability.IEntityBucket;
import git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket.AbstractEntityBucketHandler;
import git.jbredwards.subaquatic.mod.common.compat.inspirations.InspirationsHandler;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticWaterColorConfig;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IRegistryDelegate;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author jbred
 *
 */
@Mod.EventBusSubscriber(modid = Subaquatic.MODID, value = Side.CLIENT)
public final class ClientEventHandler
{
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void applyWaterSurfaceColor(@Nonnull BiomeEvent.GetWaterColor event) {
        event.setNewColor(SubaquaticWaterColorConfig.getSurfaceColor(event.getBiome(), event.getOriginalColor()));
    }

    @SubscribeEvent(receiveCanceled = true)
    public static void applyWaterFogColor(@Nonnull EntityViewRenderEvent.FogColors event) {
        if(event.getState().getMaterial() == Material.WATER) {
            //checks the player's head is underwater
            if(FluidloggedUtils.isCompatibleFluid(FluidloggedUtils.getFluidFromState(event.getState()), FluidRegistry.WATER)) {
                final float[] fogComp = SubaquaticWaterColorConfig.getFogColorAt(event.getEntity().world, event.getEntity().getPosition());
                event.setRed(fogComp[0] * 0.5f + 0.5f * event.getRed());
                event.setGreen(fogComp[1] * 0.5f + 0.5f * event.getGreen());
                event.setBlue(fogComp[2] * 0.5f + 0.5f * event.getBlue());
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    static void registerBlockColors(@Nonnull ColorHandlerEvent.Block event) {
        event.getBlockColors().registerBlockColorHandler((state, world, pos, tintIndex)
                -> tintIndex != 1 || world == null || pos == null ? -1 : Subaquatic.isInspirationsInstalled
                        ? InspirationsHandler.getCauldronColor(world, pos)
                        : BiomeColorHelper.getWaterColorAtPos(world, pos),
                Blocks.CAULDRON);
    }

    @SubscribeEvent
    static void registerModels(@Nonnull ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(ModelContainerBoat.Loader.INSTANCE);
        for(Block block : SubaquaticBlocks.INIT) if(block instanceof ICustomModel) ((ICustomModel)block).registerModels();
        for(Item item : SubaquaticItems.INIT) {
            if(item instanceof ICustomModel) ((ICustomModel)item).registerModels();
            else ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.delegate.name(), "inventory"));
        }

        //reset all BakedQuad caches on resource reload
        ((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager())
            .registerReloadListener((ISelectiveResourceReloadListener)(resourceManager, resourcePredicate) -> {
                if(resourcePredicate.test(VanillaResourceType.MODELS)) {
                    BakedEntityBucketModel.clearQuadsCache();
                    FluidRegistry.WATER.setColor(0xFFFFFFFF); //temporarily remove the water fluid color (this gets re-added later)
                }
            }
        );

        //vanilla mushroom stems
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Blocks.BROWN_MUSHROOM_BLOCK), 0, new ModelResourceLocation(Blocks.BROWN_MUSHROOM_BLOCK.delegate.name(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Blocks.BROWN_MUSHROOM_BLOCK), 1, new ModelResourceLocation(new ResourceLocation(Subaquatic.MODID, "brown_mushroom_block_stem"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Blocks.BROWN_MUSHROOM_BLOCK), 2, new ModelResourceLocation(new ResourceLocation(Subaquatic.MODID, "brown_mushroom_block_stem_all"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Blocks.RED_MUSHROOM_BLOCK), 0, new ModelResourceLocation(Blocks.RED_MUSHROOM_BLOCK.delegate.name(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Blocks.RED_MUSHROOM_BLOCK), 1, new ModelResourceLocation(new ResourceLocation(Subaquatic.MODID, "red_mushroom_block_stem"), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(Blocks.RED_MUSHROOM_BLOCK), 2, new ModelResourceLocation(new ResourceLocation(Subaquatic.MODID, "red_mushroom_block_stem_all"), "inventory"));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void handleModelOverrides(@Nonnull ModelBakeEvent event) {
        //apply entity bucket model overrides
        for(Item bucket : IEntityBucket.getValidBuckets()) {
            for(String variant : event.getModelLoader().getVariantNames(bucket)) {
                final ModelResourceLocation modelLocation = ModelLoader.getInventoryVariant(variant);
                final IBakedModel bucketModel = event.getModelRegistry().getObject(modelLocation);
                if(bucketModel != null) event.getModelRegistry().putObject(modelLocation, new BakedEntityBucketModel(bucketModel));
            }
        }

        //apply water fluid color override (fixes water color in buckets & bottles)
        //moved here from BlockColorEvent to fix an F3+T bug
        FluidRegistry.WATER.setColor(0xFF3f97e4);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void applyEntityBucketColorOverrides(@Nonnull ColorHandlerEvent.Item event) {
        final Map<IRegistryDelegate<Item>, IItemColor> itemColorMap = ObfuscationReflectionHelper.getPrivateValue(ItemColors.class, event.getItemColors(), "itemColorMap");
        for(Item bucket : IEntityBucket.getValidBuckets()) {
            final IItemColor oldColorHandler = itemColorMap.getOrDefault(bucket.delegate, (stack, tintIndex) -> -1);
            event.getItemColors().registerItemColorHandler((stack, tintIndex) -> {
                final IEntityBucket cap = IEntityBucket.get(stack);
                if(cap != null && cap.getHandler() != null) {
                    final int color = cap.getHandler().colorMultiplier(stack, tintIndex);
                    if(color != -1) return color;
                }

                return oldColorHandler.colorMultiplier(stack, tintIndex);
            }, bucket);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent(priority = EventPriority.HIGH)
    static void handleAirGuiOverlay(@Nonnull RenderGameOverlayEvent.Pre event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.AIR) {
            Minecraft.getMinecraft().profiler.startSection("air");

            final Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
            final int air = entity.getAir();
            if(air < 300 || entity.isInsideOfMaterial(Material.WATER)) {
                GlStateManager.enableBlend();
                final int left = event.getResolution().getScaledWidth() / 2 + 91;
                final int top = event.getResolution().getScaledHeight() - GuiIngameForge.right_height;

                final int full = MathHelper.ceil((air - 2) * 10f / 300);
                final int partial = MathHelper.ceil(air * 10f / 300) - full;

                final GuiIngame gui = Minecraft.getMinecraft().ingameGUI;
                for(int i = 0; i < full + partial; ++i) gui.drawTexturedModalRect(left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
                GuiIngameForge.right_height += 10;
                GlStateManager.disableBlend();
            }

            Minecraft.getMinecraft().profiler.endSection();
            event.setCanceled(true);

            //don't potentially mess up other mod rendering
            MinecraftForge.EVENT_BUS.post(new RenderGameOverlayEvent.Post(event, event.getType()));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    static void handleEntityBucketTooltip(@Nonnull ItemTooltipEvent event) {
        final IEntityBucket cap = IEntityBucket.get(event.getItemStack());
        if(cap != null && cap.getHandler() != null) cap.getHandler().handleTooltip(event.getToolTip(), event.getItemStack(), event.getFlags());
    }

    @SubscribeEvent(receiveCanceled = true)
    static void registerTextures(@Nonnull TextureStitchEvent.Pre event) {
        if(event.getMap() != Minecraft.getMinecraft().getTextureMapBlocks()) return;

        //ensure these sprites are always registered, as their use cases are hardcoded
        event.getMap().registerSprite(new ResourceLocation(Subaquatic.MODID, "blocks/water_flow"));
        event.getMap().registerSprite(new ResourceLocation(Subaquatic.MODID, "blocks/water_overlay"));
        event.getMap().registerSprite(new ResourceLocation(Subaquatic.MODID, "blocks/water_still"));
        event.getMap().registerSprite(new ResourceLocation(Subaquatic.MODID, "misc/underwater"));
        for(int i = 0; i < 5; i++) ParticleBubblePop.TEXTURES[i] =
                event.getMap().registerSprite(new ResourceLocation(Subaquatic.MODID, "particles/bubble_pop_" + i));

        //handle entity bucket sprites
        AbstractEntityBucketHandler.BUCKET_HANDLERS.values().forEach(handler -> handler.get().registerSprites(event.getMap()));
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
