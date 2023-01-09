package git.jbredwards.subaquatic.mod;

import com.cleanroommc.assetmover.AssetMoverAPI;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.api.biome.IOceanBiome;
import git.jbredwards.subaquatic.mod.client.particle.factory.ParticleFactoryColorize;
import git.jbredwards.subaquatic.mod.common.capability.IBubbleColumn;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticWaterColorConfig;
import git.jbredwards.subaquatic.mod.common.world.gen.feature.GeneratorSeaPickle;
import git.jbredwards.subaquatic.mod.common.world.gen.feature.GeneratorSeagrass;
import git.jbredwards.subaquatic.mod.common.world.gen.layer.GenLayerOceanBiomes;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 *
 * @author jbred
 *
 */
@Mod(modid = Subaquatic.MODID, name = Subaquatic.NAME, version = "1.0.0", dependencies = "required-after:fluidlogged_api@[1.9.0.5,);")
public final class Subaquatic
{
    @Nonnull public static final String MODID = "subaquatic", NAME = "Subaquatic";
    @Nonnull public static final Logger LOGGER = LogManager.getFormatterLogger(NAME);

    @SuppressWarnings("ConstantConditions")
    @Mod.EventHandler
    static void construct(@Nonnull FMLConstructionEvent event) throws IOException {
        if(event.getSide() == Side.CLIENT) {
            LOGGER.info("Attempting to gather the vanilla assets required by this mod, this may take a while if it's your first load...");
            final String[][] assets = new Gson().fromJson(IOUtils.toString(
                    Loader.class.getResourceAsStream(String.format("/assets/%s/assetmover.jsonc", MODID)),
                    Charset.defaultCharset()), String[][].class);

            final ProgressManager.ProgressBar progressBar = ProgressManager.push("AssetMover", assets.length);
            for(String[] asset : assets) { //display progress, otherwise it looks like the game froze
                progressBar.step(asset[2].replaceFirst(String.format("assets/%s/", MODID), ""));
                AssetMoverAPI.fromMinecraft(asset[0], ImmutableMap.of(asset[1], asset[2]));
            }

            ProgressManager.pop(progressBar);
            LOGGER.info("Success!");
        }
    }

    @Mod.EventHandler
    static void preInit(@Nonnull FMLPreInitializationEvent event) {
        CapabilityManager.INSTANCE.register(IBubbleColumn.class, IBubbleColumn.Storage.INSTANCE, IBubbleColumn.Impl::new);
        MinecraftForge.EVENT_BUS.register(IBubbleColumn.class);
        //world generators
        GameRegistry.registerWorldGenerator(GeneratorSeagrass.INSTANCE, 5);
        GameRegistry.registerWorldGenerator(GeneratorSeaPickle.INSTANCE, 6);
    }

    @Mod.EventHandler
    static void init(@Nonnull FMLInitializationEvent event) throws IOException {
        if(event.getSide() == Side.CLIENT) overrideParticles();
        MinecraftForge.TERRAIN_GEN_BUS.register(GenLayerOceanBiomes.class);
        //config stuff
        SubaquaticConfigHandler.init();
        SubaquaticWaterColorConfig.buildWaterColors();
        //automatically add all IOceanBiome instances to the Forge ocean biomes list
        ForgeRegistries.BIOMES.forEach(biome -> { if(biome instanceof IOceanBiome) BiomeManager.oceanBiomes.add(biome); });
        //generate ocean biome id sets
        BiomeManager.oceanBiomes.forEach(biome -> {
            final int biomeId = Biome.getIdForBiome(biome);
            IOceanBiome.OCEAN_IDS.add(biomeId);

            if(biome instanceof IOceanBiome && ((IOceanBiome)biome).getDeepOceanBiomeId() != -1) IOceanBiome.SHALLOW_OCEAN_IDS.add(biomeId);
        });
    }

    @SideOnly(Side.CLIENT)
    static void overrideParticles() {
        //override some vanilla particle factories to allow for custom coloring
        final ParticleManager manager = Minecraft.getMinecraft().effectRenderer;
        manager.registerParticle(EnumParticleTypes.DRIP_WATER.getParticleID(), new ParticleFactoryColorize(BiomeColorHelper::getWaterColorAtPos, new ParticleDrip.WaterFactory()));
        manager.registerParticle(EnumParticleTypes.WATER_DROP.getParticleID(), new ParticleFactoryColorize(BiomeColorHelper::getWaterColorAtPos, new ParticleRain.Factory()));
        manager.registerParticle(EnumParticleTypes.WATER_SPLASH.getParticleID(), new ParticleFactoryColorize(BiomeColorHelper::getWaterColorAtPos, new ParticleSplash.Factory()));
        manager.registerParticle(EnumParticleTypes.WATER_WAKE.getParticleID(), new ParticleFactoryColorize(BiomeColorHelper::getWaterColorAtPos, new ParticleWaterWake.Factory()));
        //fix bubble particles spawning in illegal blocks (like cauldrons)
        final IParticleFactory bubbleFactory = manager.particleTypes.get(EnumParticleTypes.WATER_BUBBLE.getParticleID());
        manager.registerParticle(EnumParticleTypes.WATER_BUBBLE.getParticleID(), (int particleID, @Nonnull World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... args) ->
                FluidloggedUtils.getFluidOrReal(world, new BlockPos(x, y, z)).getMaterial() == Material.WATER ? bubbleFactory.createParticle(particleID, world, x, y, z, xSpeed, ySpeed, zSpeed, args) : null);
    }
}
