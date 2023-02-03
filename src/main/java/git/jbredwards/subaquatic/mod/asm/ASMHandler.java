package git.jbredwards.subaquatic.mod.asm;

import git.jbredwards.fluidlogged_api.api.asm.AbstractClassTransformer;
import git.jbredwards.fluidlogged_api.api.asm.BasicLoadingPlugin;
import git.jbredwards.subaquatic.mod.asm.plugin.forge.*;
import git.jbredwards.subaquatic.mod.asm.plugin.modded.*;
import git.jbredwards.subaquatic.mod.asm.plugin.vanilla.block.*;
import git.jbredwards.subaquatic.mod.asm.plugin.vanilla.client.*;
import git.jbredwards.subaquatic.mod.asm.plugin.vanilla.entity.*;
import git.jbredwards.subaquatic.mod.asm.plugin.vanilla.world.*;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@BasicLoadingPlugin.Name("Subaquatic Plugin")
@BasicLoadingPlugin.MCVersion("1.12.2")
@BasicLoadingPlugin.SortingIndex(1001)
public final class ASMHandler implements BasicLoadingPlugin
{
    @SuppressWarnings("unused")
    public static final class Transformer extends AbstractClassTransformer
    {
        public Transformer() {
            //forge
            plugins.put("net.minecraftforge.fluids.FluidRegistry", new PluginFluidRegistry()); //Changes the water textures to allow for better coloring
            //modded
            plugins.put("com.blamejared.clumps.proxy.ClientProxy", new PluginClumps()); //Remove Clumps mod xp render override
            //plugins.put("biomesoplenty/common/world/layer/GenLayerRiverMixBOP", new PluginGenLayerRiverMixBOP()); //Account for all ocean biomes when generating rivers
            //vanilla
            plugins.put("net.minecraft.block.Block", new PluginBlock()); //Remove hardcoded values for biome fog color
            plugins.put("net.minecraft.block.BlockCauldron", new PluginBlockCauldron()); //Allows cauldrons to both have translucent water & to have water collision
            plugins.put("net.minecraft.block.BlockChorusFlower", new PluginBlockChorusFlower()); //Implement IChorusPlantSoil functionality
            plugins.put("net.minecraft.block.BlockChorusPlant", new PluginBlockChorusPlant()); //Implement IChorusPlantSoil functionality
            plugins.put("net.minecraft.block.BlockPumpkin", new PluginBlockPumpkin()); //Allow pumpkins to be placed anywhere
            plugins.put("net.minecraft.block.BlockSnow", new PluginBlockSnow()); //Prevent snow layers from being placeable on blue ice
            plugins.put("net.minecraft.block.BlockStem", new PluginBlockStem()); //Update pumpkin reference to the correct block
            plugins.put("net.minecraft.client.particle.ParticleDrip", new PluginParticleDrip()); //Water droplet particles keep the color set by this mod
            plugins.put("net.minecraft.client.renderer.entity.RenderEntityItem", new PluginRenderEntityItem()); //Don't render item bobbing while in water
            plugins.put("net.minecraft.client.renderer.ItemRenderer", new PluginItemRenderer()); //Apply biome colors to underwater overlay
            plugins.put("net.minecraft.client.Minecraft", new PluginMinecraft()); //Allow underwater music to be played
            plugins.put("net.minecraft.entity.item.EntityItem", new PluginEntityItem()); //Items float while in water
            plugins.put("net.minecraft.entity.item.EntityXPOrb", new PluginEntityItem()); //XP orbs float while in water
            plugins.put("net.minecraft.world.biome.Biome", new PluginBiome()); //Allow modded ocean biomes to have custom surface blocks
            plugins.put("net.minecraft.world.biome.BiomeBeach", new PluginBiomeBeach()); //Generate sand instead of gravel below sea level
            plugins.put("net.minecraft.world.biome.BiomeColorHelper", new PluginBiomeColorHelper()); //Get the biome colors from the radius specified in the config
            plugins.put("net.minecraft.world.gen.feature.WorldGenPumpkin", new PluginWorldGenPumpkin()); //Generate non-carved pumpkins instead of carved ones
            plugins.put("net.minecraft.world.gen.layer.GenLayer", new PluginGenLayer()); //Apply ocean biome generator
            plugins.put("net.minecraft.world.gen.layer.GenLayerAddIsland", new PluginGenLayerAddIsland()); //Account for modded shallow ocean biomes
            plugins.put("net.minecraft.world.gen.layer.GenLayerAddMushroomIsland", new PluginGenLayerAddMushroomIsland()); //Account for modded shallow ocean biomes
            plugins.put("net.minecraft.world.gen.layer.GenLayerDeepOcean", new PluginGenLayerDeepOcean()); //Take modded ocean biomes into account when determining whether to generate a deep ocean
            plugins.put("net.minecraft.world.gen.layer.GenLayerEdge", new PluginGenLayerEdge()); //Account for modded shallow oceans
            plugins.put("net.minecraft.world.gen.layer.GenLayerRemoveTooMuchOcean", new PluginGenLayerRemoveTooMuchOcean()); //Account for modded shallow ocean biomes
            plugins.put("net.minecraft.world.gen.layer.GenLayerRiverInit", new PluginGenLayerRiverInit()); //Account for modded shallow ocean biomes
            plugins.put("net.minecraft.world.gen.layer.GenLayerRiverMix", new PluginGenLayerRiverMix()); //Account for all ocean biomes when generating rivers
            plugins.put("net.minecraft.world.gen.layer.GenLayerShore", new PluginGenLayerShore()); //Account for all ocean biomes when generating shores
        }

        @Nonnull
        @Override
        public String getPluginName() { return "Subaquatic Plugin"; }
    }
}
