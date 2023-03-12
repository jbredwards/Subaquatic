package git.jbredwards.subaquatic.mod;

import com.cleanroommc.assetmover.AssetMoverAPI;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.api.biome.IOceanBiome;
import git.jbredwards.subaquatic.mod.client.entity.renderer.*;
import git.jbredwards.subaquatic.mod.client.particle.factory.ParticleFactoryColorize;
import git.jbredwards.subaquatic.mod.common.capability.IBoatType;
import git.jbredwards.subaquatic.mod.common.capability.IBubbleColumn;
import git.jbredwards.subaquatic.mod.common.capability.IFishBucket;
import git.jbredwards.subaquatic.mod.common.compat.inspirations.InspirationsHandler;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticTropicalFishConfig;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticWaterColorConfig;
import git.jbredwards.subaquatic.mod.common.entity.item.AbstractBoatContainer;
import git.jbredwards.subaquatic.mod.common.entity.item.EntityMinecartEnderChest;
import git.jbredwards.subaquatic.mod.common.entity.item.part.MultiPartAbstractInventoryPart;
import git.jbredwards.subaquatic.mod.common.entity.living.*;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBiomes;
import git.jbredwards.subaquatic.mod.common.message.CMessageOpenBoatInventory;
import git.jbredwards.subaquatic.mod.common.message.SMessageAbstractChestPart;
import git.jbredwards.subaquatic.mod.common.message.SMessageBoatType;
import git.jbredwards.subaquatic.mod.common.world.gen.feature.GeneratorCoral;
import git.jbredwards.subaquatic.mod.common.world.gen.feature.GeneratorKelp;
import git.jbredwards.subaquatic.mod.common.world.gen.feature.GeneratorSeaPickle;
import git.jbredwards.subaquatic.mod.common.world.gen.feature.GeneratorSeagrass;
import git.jbredwards.subaquatic.mod.common.world.gen.layer.GenLayerOceanBiomes;
import git.jbredwards.subaquatic.mod.common.world.gen.primer.PrimerSeaPickle;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.entity.RenderMinecart;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Biomes;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author jbred
 *
 */
@Mod(modid = Subaquatic.MODID, name = Subaquatic.NAME, version = "1.0.2", dependencies = "required-after:fluidlogged_api@[2.1.0,);required-client:assetmover;")
public final class Subaquatic
{
    @Nonnull public static final String MODID = "subaquatic", NAME = "Subaquatic";
    @Nonnull public static final Logger LOGGER = LogManager.getFormatterLogger(NAME);

    @SuppressWarnings("NotNullFieldNotInitialized")
    @Nonnull public static SimpleNetworkWrapper WRAPPER;

    public static final boolean isBOPInstalled = Loader.isModLoaded("biomesoplenty");
    public static final boolean isInspirationsInstalled = Loader.isModLoaded("inspirations");

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    static void constructClient(@Nonnull FMLConstructionEvent event) {
        LOGGER.info("Attempting to gather the vanilla assets required by this mod, this may take a while if it's your first load...");
        final String[][] assets = new Gson().fromJson(new InputStreamReader(Objects.requireNonNull(
                Loader.class.getResourceAsStream(String.format("/assets/%s/assetmover.jsonc", MODID)))),
                String[][].class);

        final ProgressManager.ProgressBar progressBar = ProgressManager.push("AssetMover", assets.length);
        for(String[] asset : assets) { //display progress, otherwise it looks like the game froze
            progressBar.step(asset[2].replaceFirst(String.format("assets/%s/", MODID), ""));
            AssetMoverAPI.fromMinecraft(asset[0], ImmutableMap.of(asset[1], asset[2]));
        }

        ProgressManager.pop(progressBar);
        LOGGER.info("Success!");
    }

    @Mod.EventHandler
    static void preInit(@Nonnull FMLPreInitializationEvent event) {
        //capabilities
        CapabilityManager.INSTANCE.register(IBubbleColumn.class, IBubbleColumn.Storage.INSTANCE, IBubbleColumn.Impl::new);
        CapabilityManager.INSTANCE.register(IBoatType.class, IBoatType.Storage.INSTANCE, IBoatType.Impl::new);
        CapabilityManager.INSTANCE.register(IFishBucket.class, IFishBucket.Storage.INSTANCE, IFishBucket.Impl::new);
        MinecraftForge.EVENT_BUS.register(IBubbleColumn.class);
        MinecraftForge.EVENT_BUS.register(IBoatType.class);
        MinecraftForge.EVENT_BUS.register(IFishBucket.class);
        //message registries
        WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        WRAPPER.registerMessage(SMessageBoatType.Handler.INSTANCE, SMessageBoatType.class, 0, Side.CLIENT);
        WRAPPER.registerMessage(SMessageAbstractChestPart.Handler.INSTANCE, SMessageAbstractChestPart.class, 1, Side.CLIENT);
        WRAPPER.registerMessage(CMessageOpenBoatInventory.Handler.INSTANCE, CMessageOpenBoatInventory.class, 2, Side.SERVER);
        //world generators
        GeneratorCoral.initDefaults();
        GameRegistry.registerWorldGenerator(GeneratorCoral.INSTANCE, 3);
        GameRegistry.registerWorldGenerator(GeneratorKelp.INSTANCE, 4);
        GameRegistry.registerWorldGenerator(GeneratorSeagrass.INSTANCE, 5);
        GameRegistry.registerWorldGenerator(GeneratorSeaPickle.INSTANCE, 6);
        MinecraftForge.TERRAIN_GEN_BUS.register(PrimerSeaPickle.class);
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    static void preInitClient(@Nonnull FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(AbstractBoatContainer.class, RenderBoatContainer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityMinecartEnderChest.class, RenderMinecart::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityCod.class, RenderCod::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPufferfish.class, RenderPufferfish::new);
        RenderingRegistry.registerEntityRenderingHandler(EntitySalmon.class, RenderSalmon::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTropicalFish.class, RenderTropicalFish::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityXPOrb.class, RenderTranslucentXPOrb::new);
        //inspirations compat
        if(isInspirationsInstalled) MinecraftForge.EVENT_BUS.register(InspirationsHandler.class);
    }

    @Mod.EventHandler
    static void init(@Nonnull FMLInitializationEvent event) {
        MinecraftForge.TERRAIN_GEN_BUS.register(GenLayerOceanBiomes.class);
        //entity data fixers
        AbstractBoatContainer.registerFixer(FMLCommonHandler.instance().getDataFixer());
        MultiPartAbstractInventoryPart.registerFixer(FMLCommonHandler.instance().getDataFixer());
        //automatically add all IOceanBiome instances to the Forge ocean biomes list
        ForgeRegistries.BIOMES.forEach(biome -> { if(biome instanceof IOceanBiome) BiomeManager.oceanBiomes.add(biome); });
        //generate ocean biome id sets
        BiomeManager.oceanBiomes.forEach(biome -> {
            final int biomeId = Biome.getIdForBiome(biome);
            IOceanBiome.OCEAN_IDS.add(biomeId);

            if(biome instanceof IOceanBiome && ((IOceanBiome)biome).getDeepOceanBiomeId() != -1) IOceanBiome.SHALLOW_OCEAN_IDS.add(biomeId);
        });
        //automatically update valid ocean monument spawn biomes
        StructureOceanMonument.SPAWN_BIOMES = new ArrayList<>(ImmutableList.<Biome>builder()
                .add(Biomes.DEEP_OCEAN)
                .addAll(BiomeManager.oceanBiomes.stream()
                        .filter(biome -> biome instanceof IOceanBiome && ((IOceanBiome)biome).getDeepOceanBiomeId() == -1)
                        .collect(Collectors.toList()))
                .build());
        //automatically update valid ocean monument neighbor biomes
        StructureOceanMonument.WATER_BIOMES = new ArrayList<>(ImmutableList.<Biome>builder()
                .addAll(BiomeManager.oceanBiomes)
                .addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.RIVER))
                .build());
        //ocean monuments spawning in these biomes causes problems
        StructureOceanMonument.SPAWN_BIOMES.removeIf(biome -> biome == Biomes.FROZEN_OCEAN || biome == SubaquaticBiomes.DEEP_FROZEN_OCEAN);
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    static void initClient(@Nonnull FMLInitializationEvent event) {
        //override some vanilla particle factories to allow for custom coloring
        final ParticleManager manager = Minecraft.getMinecraft().effectRenderer;
        manager.registerParticle(EnumParticleTypes.DRIP_WATER.getParticleID(), new ParticleFactoryColorize(new ParticleDrip.WaterFactory(), isInspirationsInstalled ? InspirationsHandler::getCauldronColor : BiomeColorHelper::getWaterColorAtPos));
        manager.registerParticle(EnumParticleTypes.SUSPENDED.getParticleID(), new ParticleFactoryColorize(new ParticleSuspend.Factory(), isInspirationsInstalled ? InspirationsHandler::getCauldronColor : BiomeColorHelper::getWaterColorAtPos));
        manager.registerParticle(EnumParticleTypes.WATER_DROP.getParticleID(), new ParticleFactoryColorize(new ParticleRain.Factory(), isInspirationsInstalled ? InspirationsHandler::getCauldronColor : BiomeColorHelper::getWaterColorAtPos));
        manager.registerParticle(EnumParticleTypes.WATER_SPLASH.getParticleID(), new ParticleFactoryColorize(new ParticleSplash.Factory(), isInspirationsInstalled ? InspirationsHandler::getCauldronColor : BiomeColorHelper::getWaterColorAtPos));
        manager.registerParticle(EnumParticleTypes.WATER_WAKE.getParticleID(), new ParticleFactoryColorize(new ParticleWaterWake.Factory(), isInspirationsInstalled ? InspirationsHandler::getCauldronColor : BiomeColorHelper::getWaterColorAtPos));
        //fix bubble particles spawning in illegal blocks (like cauldrons)
        final IParticleFactory bubbleFactory = manager.particleTypes.get(EnumParticleTypes.WATER_BUBBLE.getParticleID());
        manager.registerParticle(EnumParticleTypes.WATER_BUBBLE.getParticleID(), (int particleID, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... args) ->
                FluidloggedUtils.getFluidOrReal(world, new BlockPos(x, y, z)).getMaterial() == Material.WATER ? bubbleFactory.createParticle(particleID, world, x, y, z, xSpeed, ySpeed, zSpeed, args) : null);
    }

    @Mod.EventHandler
    static void postInit(@Nonnull FMLPostInitializationEvent event) throws IOException {
        //config stuff
        SubaquaticConfigHandler.init();
        SubaquaticTropicalFishConfig.buildFishTypes();
        SubaquaticWaterColorConfig.buildWaterColors();
    }
}
