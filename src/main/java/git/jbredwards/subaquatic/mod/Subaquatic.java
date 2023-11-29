package git.jbredwards.subaquatic.mod;

import com.cleanroommc.assetmover.AssetMoverAPI;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.api.biome.IOceanBiome;
import git.jbredwards.subaquatic.mod.client.entity.renderer.*;
import git.jbredwards.subaquatic.mod.client.particle.factory.ParticleFactoryColorize;
import git.jbredwards.subaquatic.mod.common.capability.IBoatType;
import git.jbredwards.subaquatic.mod.common.capability.IBubbleColumn;
import git.jbredwards.subaquatic.mod.common.capability.ICompactFishing;
import git.jbredwards.subaquatic.mod.common.capability.IEntityBucket;
import git.jbredwards.subaquatic.mod.common.compat.inspirations.InspirationsHandler;
import git.jbredwards.subaquatic.mod.common.compat.jer.SubaquaticJERPlugin;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticBlockSoakRecipesConfig;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticTropicalFishConfig;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticWaterColorConfig;
import git.jbredwards.subaquatic.mod.common.entity.item.AbstractBoatContainer;
import git.jbredwards.subaquatic.mod.common.entity.item.part.MultiPartAbstractInventoryPart;
import git.jbredwards.subaquatic.mod.common.entity.living.*;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticSounds;
import git.jbredwards.subaquatic.mod.common.message.*;
import git.jbredwards.subaquatic.mod.common.recipe.BlockSoakRecipe;
import git.jbredwards.subaquatic.mod.common.world.biome.BiomeFrozenOcean;
import git.jbredwards.subaquatic.mod.common.world.gen.feature.*;
import git.jbredwards.subaquatic.mod.common.world.gen.layer.GenLayerOceanBiomes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
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
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author jbred
 *
 */
@Mod(modid = Subaquatic.MODID, name = Subaquatic.NAME, version = "1.2.1", dependencies = "required-after:fluidlogged_api@[2.2.5,);required-client:assetmover@[2.5,);")
public final class Subaquatic
{
    @Nonnull public static final String MODID = "subaquatic", NAME = "Subaquatic";
    @Nonnull public static final Logger LOGGER = LogManager.getFormatterLogger(NAME);

    @SuppressWarnings("NotNullFieldNotInitialized")
    @Nonnull public static SimpleNetworkWrapper WRAPPER;

    public static final boolean isInspirationsInstalled = Loader.isModLoaded("inspirations");
    public static final boolean isJERInstalled = Loader.isModLoaded("jeresources");

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    static void constructClient(@Nonnull FMLConstructionEvent event) {
        LOGGER.info("Reading vanilla assets required by this mod...");
        final String[][] assets = new Gson().fromJson(new InputStreamReader(Objects.requireNonNull(
                Loader.class.getResourceAsStream(String.format("/assets/%s/assetmover.jsonc", MODID)))),
                String[][].class);

        for(String[] asset : assets) AssetMoverAPI.fromMinecraft(asset[0], Collections.singletonMap(asset[1], asset[2]));
        LOGGER.info("Success!");
    }

    @Mod.EventHandler
    static void preInit(@Nonnull FMLPreInitializationEvent event) throws IOException {
        //handle tropical fish types
        SubaquaticTropicalFishConfig.buildFishTypes();

        //capabilities
        CapabilityManager.INSTANCE.register(IBubbleColumn.class, IBubbleColumn.Storage.INSTANCE, IBubbleColumn.Impl::new);
        CapabilityManager.INSTANCE.register(IBoatType.class, IBoatType.Storage.INSTANCE, IBoatType.Impl::new);
        CapabilityManager.INSTANCE.register(ICompactFishing.class, ICompactFishing.Storage.INSTANCE, ICompactFishing.Impl::new);
        CapabilityManager.INSTANCE.register(IEntityBucket.class, IEntityBucket.Storage.INSTANCE, IEntityBucket.Impl::new);
        MinecraftForge.EVENT_BUS.register(IBubbleColumn.class);
        MinecraftForge.EVENT_BUS.register(IBoatType.class);
        MinecraftForge.EVENT_BUS.register(ICompactFishing.class);
        MinecraftForge.EVENT_BUS.register(IEntityBucket.class);

        //message registries
        WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        WRAPPER.registerMessage(SMessageBoatType.Handler.INSTANCE, SMessageBoatType.class, 0, Side.CLIENT);
        WRAPPER.registerMessage(SMessageAbstractChestPart.Handler.INSTANCE, SMessageAbstractChestPart.class, 1, Side.CLIENT);
        WRAPPER.registerMessage(CMessageOpenBoatInventory.Handler.INSTANCE, CMessageOpenBoatInventory.class, 2, Side.SERVER);
        WRAPPER.registerMessage(SMessageFurnacePart.Handler.INSTANCE, SMessageFurnacePart.class, 3, Side.CLIENT);
        WRAPPER.registerMessage(SMessageBottleParticles.Handler.INSTANCE, SMessageBottleParticles.class, 4, Side.CLIENT);

        //world generators
        GeneratorCoral.registerDefaults();
        GameRegistry.registerWorldGenerator(GeneratorCoral.INSTANCE, 3);
        GameRegistry.registerWorldGenerator(GeneratorKelp.INSTANCE, 4);
        GameRegistry.registerWorldGenerator(GeneratorSeagrass.INSTANCE, 5);
        GameRegistry.registerWorldGenerator(GeneratorSeaPickle.INSTANCE, 6);
        GameRegistry.registerWorldGenerator(GeneratorGlowLichen.INSTANCE, 6);

        //new water bottle property
        final IItemPropertyGetter waterProperty = new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            @Override
            public float apply(@Nonnull ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                return SubaquaticConfigHandler.Client.Item.translucentWaterBottles
                        && PotionUtils.getPotionFromItem(stack) != PotionTypes.EMPTY
                        && PotionUtils.getEffectsFromStack(stack).isEmpty() ? 1 : 0;
            }
        };

        //items to apply the override
        Items.POTIONITEM.addPropertyOverride(new ResourceLocation(MODID, "isWater"), waterProperty);
        Items.SPLASH_POTION.addPropertyOverride(new ResourceLocation(MODID, "isWater"), waterProperty);
        Items.LINGERING_POTION.addPropertyOverride(new ResourceLocation(MODID, "isWater"), waterProperty);
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    static void preInitClient(@Nonnull FMLPreInitializationEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(AbstractBoatContainer.class, RenderBoatContainer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityCod.class, RenderCod::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityFish.class, RenderFish::new);
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
        StructureOceanMonument.SPAWN_BIOMES.removeIf(biome -> biome instanceof BiomeFrozenOcean);

        //add block soak recipe functionality to dispensers
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.POTIONITEM, BlockSoakRecipe.getDispenserHandler());
    }

    @Mod.EventHandler
    @SideOnly(Side.SERVER)
    static void initServer(@Nonnull FMLInitializationEvent event) {
        //ensure that the server has the water color fix as well, for correct particle color sync
        //client handles this differently via ClientEventHandler, as to fix an F3+T bug
        FluidRegistry.WATER.setColor(0xFF3f97e4);
    }

    @Mod.EventHandler
    static void postInit(@Nonnull FMLPostInitializationEvent event) throws IOException {
        //config stuff
        SubaquaticConfigHandler.init();
        SubaquaticWaterColorConfig.buildWaterColors();
        SubaquaticBlockSoakRecipesConfig.buildRecipes();
        //improve certain modded block sounds
        Optional.ofNullable(Block.getBlockFromName("biomesoplenty:waterlily")).ifPresent(block -> block.setSoundType(SubaquaticSounds.WET_GRASS));
        Optional.ofNullable(Block.getBlockFromName("twilightforest:huge_lilypad")).ifPresent(block -> block.setSoundType(SubaquaticSounds.WET_GRASS));
        Optional.ofNullable(Block.getBlockFromName("twilightforest:huge_waterlily")).ifPresent(block -> block.setSoundType(SubaquaticSounds.WET_GRASS));
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    static void postInitClient(@Nonnull FMLPostInitializationEvent event) {
        //override some vanilla particle factories to allow for custom coloring
        final ParticleManager manager = Minecraft.getMinecraft().effectRenderer;
        manager.registerParticle(EnumParticleTypes.DRIP_WATER.getParticleID(), new ParticleFactoryColorize(manager.particleTypes.get(EnumParticleTypes.DRIP_WATER.getParticleID()), isInspirationsInstalled ? InspirationsHandler::getCauldronColor : BiomeColorHelper::getWaterColorAtPos));
        manager.registerParticle(EnumParticleTypes.SUSPENDED.getParticleID(), new ParticleFactoryColorize(manager.particleTypes.get(EnumParticleTypes.SUSPENDED.getParticleID()), isInspirationsInstalled ? InspirationsHandler::getCauldronColor : BiomeColorHelper::getWaterColorAtPos));
        manager.registerParticle(EnumParticleTypes.WATER_BUBBLE.getParticleID(), new ParticleFactoryColorize(manager.particleTypes.get(EnumParticleTypes.WATER_BUBBLE.getParticleID()), isInspirationsInstalled ? InspirationsHandler::getParticleColorAt : SubaquaticWaterColorConfig::getParticleColorAt));
        manager.registerParticle(EnumParticleTypes.WATER_DROP.getParticleID(), new ParticleFactoryColorize(manager.particleTypes.get(EnumParticleTypes.WATER_DROP.getParticleID()), isInspirationsInstalled ? InspirationsHandler::getParticleColorAt : SubaquaticWaterColorConfig::getParticleColorAt));
        manager.registerParticle(EnumParticleTypes.WATER_SPLASH.getParticleID(), new ParticleFactoryColorize(manager.particleTypes.get(EnumParticleTypes.WATER_SPLASH.getParticleID()), isInspirationsInstalled ? InspirationsHandler::getParticleColorAt : SubaquaticWaterColorConfig::getParticleColorAt));
        manager.registerParticle(EnumParticleTypes.WATER_WAKE.getParticleID(), new ParticleFactoryColorize(manager.particleTypes.get(EnumParticleTypes.WATER_WAKE.getParticleID()), isInspirationsInstalled ? InspirationsHandler::getParticleColorAt : SubaquaticWaterColorConfig::getParticleColorAt));
        //fix bubble particles spawning in illegal blocks (like cauldrons)
        final IParticleFactory bubbleFactory = manager.particleTypes.get(EnumParticleTypes.WATER_BUBBLE.getParticleID());
        manager.registerParticle(EnumParticleTypes.WATER_BUBBLE.getParticleID(), (particleID, world, x, y, z, xSpeed, ySpeed, zSpeed, args) -> {
            final Particle particle = bubbleFactory.createParticle(particleID, world, x, y, z, xSpeed, ySpeed, zSpeed, args);
            if(particle == null) return null;

            final BlockPos pos = new BlockPos(x, y, z);
            final AxisAlignedBB box = particle.getBoundingBox();

            final AxisAlignedBB boxToCheck = new AxisAlignedBB(box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ);
            if(!Boolean.TRUE.equals(FluidloggedUtils.getFluidOrReal(world, pos).getBlock().isAABBInsideMaterial(world, pos, boxToCheck, Material.WATER))) return null;

            //longer lasting bubble particles, if enabled in the config
            else if(SubaquaticConfigHandler.Client.Particle.longerLastingBubbles) particle.setMaxAge(particle.particleMaxAge * 5);
            return particle;
        });

        //Initializing JER integration if loaded
        if(isJERInstalled) SubaquaticJERPlugin.init();
    }
}
