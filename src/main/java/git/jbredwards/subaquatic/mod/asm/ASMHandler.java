package git.jbredwards.subaquatic.mod.asm;

import git.jbredwards.fluidlogged_api.api.asm.AbstractClassTransformer;
import git.jbredwards.fluidlogged_api.api.asm.BasicLoadingPlugin;
import git.jbredwards.subaquatic.mod.asm.plugin.forge.*;
import git.jbredwards.subaquatic.mod.asm.plugin.modded.*;
import git.jbredwards.subaquatic.mod.asm.plugin.vanilla.block.*;
import git.jbredwards.subaquatic.mod.asm.plugin.vanilla.client.*;
import git.jbredwards.subaquatic.mod.asm.plugin.vanilla.entity.*;
import git.jbredwards.subaquatic.mod.asm.plugin.vanilla.item.*;
import git.jbredwards.subaquatic.mod.asm.plugin.vanilla.network.*;
import git.jbredwards.subaquatic.mod.asm.plugin.vanilla.potion.PluginPotionUtils;
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
            plugins.put("net.minecraftforge.common.util.PacketUtil", new PluginPacketUtil()); //Fix ItemStack capabilities being lost when sending an ItemStack to the client
            plugins.put("net.minecraftforge.fluids.FluidRegistry", new PluginFluidRegistry()); //Changes the water textures to allow for better coloring
            plugins.put("net.minecraftforge.fluids.FluidUtil", new PluginFluidUtil()); //Place fish contained within bucket
            plugins.put("net.minecraftforge.fml.common.registry.VillagerRegistry", new PluginVillagerRegistry()); //Add IConditionalProfession functionality
            //modded
            plugins.put("appeng.api.implementations.items.IGrowableCrystal", new PluginNoItemBobbing()); //Crystal seeds don't bob at the surface
            plugins.put("biomesoplenty.common.block.BlockBOPDirt", new PluginBlockGrass()); //Fix grass & mycelium growing underwater, also fixes MC-130137
            plugins.put("biomesoplenty.common.block.BlockBOPGrass", new PluginBlockGrass()); //Fix grass & mycelium growing underwater, also fixes MC-130137
            plugins.put("biomesoplenty.common.handler.FogEventHandler", new PluginBOPFogEventHandler()); //Don't change the underwater fog color while this mod is installed
            plugins.put("biomesoplenty.common.world.layer.GenLayerRiverMixBOP", new PluginBOPGenLayerRiverMix()); //Account for all ocean biomes when generating rivers
            plugins.put("biomesoplenty.common.world.layer.GenLayerShoreBOP", new PluginBOPGenLayerShore()); //Account for all ocean biomes when generating shores
            plugins.put("biomesoplenty.common.world.BiomeProviderBOP", new PluginBOPBiomeProvider()); //Apply ocean biome generator
            plugins.put("com.blamejared.clumps.proxy.ClientProxy", new PluginClumps()); //Remove Clumps mod XP orb render override
            plugins.put("com.blamejared.clumps.entities.EntityXPOrbBig", new PluginClumps()); //Clumps mod XP orbs float while in water
            plugins.put("com.fuzs.aquaacrobatics.block.BlockBubbleColumn", new PluginAquaAcrobatics()); //AA bubble columns implement IOxygenSupplier
            plugins.put("com.fuzs.aquaacrobatics.client.handler.FogHandler", new PluginAquaAcrobatics()); //Improve Aqua Acrobatics mod compatibility by removing the stuff from that mod which this mod also does
            plugins.put("com.fuzs.aquaacrobatics.core.mixin.client.ItemRendererMixin", new PluginAquaAcrobatics());
            plugins.put("com.fuzs.aquaacrobatics.core.mixin.client.ModelFluidMixin", new PluginAquaAcrobatics());
            plugins.put("com.fuzs.aquaacrobatics.core.mixin.BiomeColorHelperMixin", new PluginAquaAcrobatics());
            plugins.put("com.fuzs.aquaacrobatics.core.mixin.BiomeMixin", new PluginAquaAcrobatics());
            plugins.put("com.fuzs.aquaacrobatics.core.mixin.BlockGrassMixin", new PluginAquaAcrobatics());
            plugins.put("com.fuzs.aquaacrobatics.core.mixin.BlockMyceliumMixin", new PluginAquaAcrobatics());
            plugins.put("com.fuzs.aquaacrobatics.core.mixin.EntityMixin", new PluginAquaAcrobatics());
            plugins.put("com.fuzs.aquaacrobatics.core.mixin.EntityItemMixin", new PluginAquaAcrobatics());
            plugins.put("com.fuzs.aquaacrobatics.core.mixin.EntityLivingBaseMixin", new PluginAquaAcrobatics());
            plugins.put("com.fuzs.aquaacrobatics.core.thaumcraft.mixin.client.TileCrucibleRendererMixin", new PluginAquaAcrobatics());
            plugins.put("com.sewef.burnbabyburn.MainMod", new PluginBurnBabyBurn()); //Remove duplicate mod functionality
            plugins.put("com.teammetallurgy.aquaculture.items.ItemAdminFishingRod", new PluginItemFishingRod()); //Transfer modded fishing rod enchantments to the fishing hook entity
            plugins.put("com.teammetallurgy.aquaculture.items.ItemAquacultureFishingRod", new PluginItemFishingRod()); //Transfer modded fishing rod enchantments to the fishing hook entity
            plugins.put("git.jbredwards.fluidlogged_api.mod.common.EventHandler", new PluginFluidloggedAPI()); //Place any fish contained within the bucket when fluidlogging, and yes I'm asm-ing my own mod XD
            plugins.put("hellfirepvp.astralsorcery.common.item.crystal.base.ItemRockCrystalBase", new PluginNoItemBobbing()); //Rock crystals don't bob at the surface
            plugins.put("hellfirepvp.astralsorcery.common.item.tool.ItemCrystalSword", new PluginNoItemBobbing()); //Crystal swords don't bob at the surface
            plugins.put("hellfirepvp.astralsorcery.common.item.tool.ItemCrystalToolBase", new PluginNoItemBobbing()); //Crystal tools don't bob at the surface
            plugins.put("hellfirepvp.astralsorcery.common.item.ItemCraftingComponent", new PluginAstralSorcery()); //Stardust don't bob at the surface
            plugins.put("knightminer.inspirations.recipes.tileentity.TileCauldron", new PluginInspirations()); //Place fish contained within bucket
            plugins.put("net.optifine.CustomColors", new PluginOptifine()); //Fix possible Optifine NPE with bubble particles
            plugins.put("thaumcraft.client.renderers.tile.TileCrucibleRenderer", new PluginThaumcraft()); //Use old water texture
            plugins.put("thebetweenlands.common.world.biome.BiomeBetweenlands", new PluginBetweenlands()); //Preserve betweenlands biome colors
            plugins.put("vazkii.botania.client.render.tile.RenderTileAltar", new PluginBotania()); //Water inside petal apothecaries have their biome colors applied
            plugins.put("vazkii.quark.tweaks.feature.BabyZombiesBurn", new PluginQuark()); //Remove duplicate mod functionality
            plugins.put("vibrantjourneys.entities.item.EntityPVJBoat", new PluginEntityBoat()); //Bubble columns rock boats (why does PVJ duplicate boat code...?)
            //vanilla
            plugins.put("net.minecraft.block.Block", new PluginBlock()); //Remove hardcoded values for biome fog color
            plugins.put("net.minecraft.block.BlockCauldron", new PluginBlockCauldron()); //Allows cauldrons to both have translucent water & to have water collision
            plugins.put("net.minecraft.block.BlockGrass", new PluginBlockGrass()); //Fix grass growing underwater, also fixes MC-130137
            plugins.put("net.minecraft.block.BlockHugeMushroom", new PluginBlockHugeMushroom()); //Allow the huge mushroom item blocks to be more accessible & useful outside just commands
            plugins.put("net.minecraft.block.BlockLever", new PluginBlockLever()); //Add lever redstone particles
            plugins.put("net.minecraft.block.BlockMycelium", new PluginBlockGrass()); //Fix mycelium growing underwater, also fixes MC-130137
            plugins.put("net.minecraft.block.BlockPumpkin", new PluginBlockPumpkin()); //Allow pumpkins to be placed anywhere
            plugins.put("net.minecraft.block.BlockSnow", new PluginBlockSnow()); //Prevent snow layers from being placeable on blue ice
            plugins.put("net.minecraft.block.BlockStem", new PluginBlockStem()); //Update pumpkin reference to the correct block
            plugins.put("net.minecraft.client.audio.SoundManager", new PluginSoundManager()); //Allow for sounds with a high priority
            plugins.put("net.minecraft.client.particle.ParticleBubble", new PluginParticleBubble()); //Add the unused bubble pop particle from 1.13+
            plugins.put("net.minecraft.client.particle.ParticleDrip", new PluginParticleDrip()); //Water droplet particles keep the color set by this mod
            plugins.put("net.minecraft.client.particle.ParticleExplosionHuge", new PluginParticleExplosionHuge()); //Spawn bubbles for underwater explosions
            plugins.put("net.minecraft.client.renderer.entity.RenderBoat", new PluginRenderBoat()); //Render bubble column boat rocking
            plugins.put("net.minecraft.client.renderer.entity.RenderEntityItem", new PluginRenderEntityItem()); //Don't render item bobbing while in water
            //plugins.put("net.minecraft.client.renderer.EntityRenderer", new PluginEntityRenderer()); //Colors rain according to biome color
            plugins.put("net.minecraft.client.renderer.ItemRenderer", new PluginItemRenderer()); //Apply biome colors to underwater overlay
            plugins.put("net.minecraft.client.Minecraft", new PluginMinecraft()); //Allow underwater music to be played
            plugins.put("net.minecraft.entity.item.EntityBoat", new PluginEntityBoat()); //Bubble columns rock boats
            plugins.put("net.minecraft.entity.item.EntityItem", new PluginEntityItem()); //Items float while in water
            plugins.put("net.minecraft.entity.item.EntityXPOrb", new PluginEntityXPOrb()); //XP orbs float while in water
            plugins.put("net.minecraft.entity.monster.EntityZombie", new PluginEntityZombie()); //Baby zombies burn in daylight if enabled
            plugins.put("net.minecraft.entity.player.EntityPlayerMP", new PluginEntityPlayerMP()); //Check for no collision instead of air when falling on a block (MC-1691)
            plugins.put("net.minecraft.entity.Entity", new PluginEntity()); //Check for no collision instead of air when falling on a block (MC-1691)
            plugins.put("net.minecraft.entity.EntityLivingBase", new PluginEntityLivingBase()); //Backport new oxygen replenish system & add IOxygenSupplier
            plugins.put("net.minecraft.item.Item", new PluginItem()); //Custom ItemBlock implementation for mushroom blocks to allow for new metadata properties
            plugins.put("net.minecraft.item.ItemBucket", new PluginItemBucket()); //Place fish contained within bucket
            plugins.put("net.minecraft.item.ItemDye", new PluginItemDye()); //Fix multiple issues with bonemeal particles
            plugins.put("net.minecraft.item.ItemExpBottle", new PluginItemExpBottle()); //Add new config option to toggle the exp bottle enchantment glint
            plugins.put("net.minecraft.item.ItemFishingRod", new PluginItemFishingRod()); //Transfer modded fishing rod enchantments to the fishing hook entity
            plugins.put("net.minecraft.item.ItemPotion", new PluginItemPotion()); //Add new config option to toggle the potion enchantment glint
            plugins.put("net.minecraft.network.PacketBuffer", new PluginPacketBuffer()); //Fix ItemStack capabilities being lost when sending an ItemStack to the client
            plugins.put("net.minecraft.potion.PotionUtils", new PluginPotionUtils()); //Use new water fluid color
            plugins.put("net.minecraft.tileentity.TileEntityChest", new PluginTileEntityChest()); //Add bubble particles for chests when they open underwater
            plugins.put("net.minecraft.world.biome.Biome", new PluginBiome()); //Allow modded ocean biomes to have custom surface blocks
            plugins.put("net.minecraft.world.biome.BiomeBeach", new PluginBiomeBeach()); //Generate sand instead of gravel below sea level
            plugins.put("net.minecraft.world.biome.BiomeColorHelper", new PluginBiomeColorHelper()); //Get the biome colors from the radius specified in the config
            //plugins.put("net.minecraft.world.chunk.Chunk", new PluginChunk()); //Add call to OnCreateChunkFromPrimerEvent
            plugins.put("net.minecraft.world.gen.feature.WorldGenBigTree", new PluginWorldGenBigTree()); //Fix bug where the block under tall trees is not converted to dirt
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
