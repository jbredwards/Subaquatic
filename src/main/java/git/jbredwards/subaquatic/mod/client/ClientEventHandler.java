package git.jbredwards.subaquatic.mod.client;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.client.item.ICustomModel;
import git.jbredwards.subaquatic.mod.client.item.model.ModelContainerBoat;
import git.jbredwards.subaquatic.mod.client.particle.ParticleBubbleColumnPop;
import git.jbredwards.subaquatic.mod.client.particle.ParticleGlowSquidAura;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticWaterColorConfig;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;

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

    @SubscribeEvent
    static void registerBlockColors(@Nonnull ColorHandlerEvent.Block event) {
        event.getBlockColors().registerBlockColorHandler((state, world, pos, tintIndex)
                -> tintIndex != 1 || world == null || pos == null ? -1 : BiomeColorHelper.getWaterColorAtPos(world, pos),
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
    }

    @SubscribeEvent
    static void registerTextures(@Nonnull TextureStitchEvent.Pre event) {
        if(event.getMap() == Minecraft.getMinecraft().getTextureMapBlocks()) {
            //ensure these sprites are always registered, as their use cases are hardcoded
            event.getMap().registerSprite(new ResourceLocation(Subaquatic.MODID, "blocks/water_flow"));
            event.getMap().registerSprite(new ResourceLocation(Subaquatic.MODID, "blocks/water_overlay"));
            event.getMap().registerSprite(new ResourceLocation(Subaquatic.MODID, "blocks/water_still"));
            event.getMap().registerSprite(new ResourceLocation(Subaquatic.MODID, "misc/underwater"));

            for(int i = 0; i < 5; i++) ParticleBubbleColumnPop.TEXTURES[i] = event.getMap().registerSprite(new ResourceLocation(Subaquatic.MODID, "particles/bubble_pop_" + i));
            ParticleGlowSquidAura.TEXTURE = event.getMap().registerSprite(new ResourceLocation(Subaquatic.MODID, "particles/glow_squid_aura"));
        }
    }
}
