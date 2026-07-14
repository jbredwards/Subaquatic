/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.config;

import git.jbredwards.fluidlogged_api.api.util.FluidState;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.ocean_api.mod.core.ASMHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 *
 * @author jbred
 *
 */
public final class SubaquaticWaterColorConfig
{
    @Nonnull
    public static final Map<Fluid, Color> FLUID_PIXEL_BASE_COLORS = new HashMap<>();
    public static void generateLegacyConfigFile() throws IOException {
        @Nonnull final String fileName = "subaquatic/water_colors.jsonc";
        if(!new File("config", fileName).exists()) { // Check old location.
            @Nonnull final Path config = Loader.instance().getConfigDir().toPath().resolve(fileName);
            if(!Files.exists(config)) {
                Files.createDirectories(config.getParent());
                Files.write(config, Collections.singleton(defaultConfigValues), StandardOpenOption.CREATE_NEW);
            }
        }
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public static float[] getFogColorAt(@Nonnull IBlockAccess worldIn, @Nonnull BlockPos posIn) {
        return new Color(ASMHooks.getFogColorAtPos(worldIn, posIn)).getColorComponents(new float[3]);
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    public static Color getParticleColorAt(@Nonnull World worldIn, double x, double y, double z) {
        final Pair<BlockPos, FluidState> here = findClosestAround(FluidloggedUtils::getFluidState, fluidState -> !fluidState.isEmpty(), worldIn, x, y, z);
        if(here == null) return new Color(BiomeColorHelper.getWaterColorAtPos(worldIn, new BlockPos(x, y, z)));

        final BlockPos pos = here.getLeft();
        final FluidState fluidState = here.getRight();

        //use block color if applicable
        final int blockColor = Minecraft.getMinecraft().getBlockColors().colorMultiplier(fluidState.getState(), worldIn, pos, 0);
        if(blockColor != -1) return new Color(blockColor);

        //use fluid color if applicable
        final int fluidColor = fluidState.getFluid().getColor(worldIn, pos);
        return fluidColor != 0xFFFFFFFF ? new Color(fluidColor) : FLUID_PIXEL_BASE_COLORS.get(fluidState.getFluid());
    }

    @Nullable
    public static <T> Pair<BlockPos, T> findClosestAround(@Nonnull BiFunction<World, BlockPos, T> getter, @Nonnull Predicate<T> checker, @Nonnull World world, double x, double y, double z) {
        final BlockPos origin = new BlockPos(x, y, z);
        T instance = getter.apply(world, origin);

        if(checker.test(instance)) return Pair.of(origin, instance);
        final List<Pair<BlockPos, T>> instances = new ArrayList<>();
        for(BlockPos pos : BlockPos.getAllInBoxMutable(new BlockPos(x - 0.25, y - 0.25, z - 0.25), new BlockPos(x + 0.25, y + 0.25, z + 0.25))) {
            if(!pos.equals(origin)) {
                instance = getter.apply(world, pos);
                if(checker.test(instance)) instances.add(Pair.of(pos, instance));
            }
        }

        double closestDist = -1;
        Pair<BlockPos, T> closest = null;
        for(Pair<BlockPos, T> entry : instances) {
            final double distance = entry.getLeft().distanceSqToCenter(x, y, z);
            if(closestDist > distance) {
                closestDist = distance;
                closest = entry;
            }
        }

        return closest;
    }

    @Nonnull
    private static final String defaultConfigValues =
            "{\n" +
            "    //Frozen River\n" +
            "    \"minecraft:frozen_river\":{\n" +
            "        \"Surface\":\"0x185390\",\n" +
            "        \"Fog\":\"0x185390\"\n" +
            "    },\n" +
            "    //Warm Ocean\n" +
            "    \"subaquatic:warm_ocean\":{\n" +
            "        \"Surface\":\"0x43D5EE\",\n" +
            "        \"Fog\":\"0x43D5EE\"\n" +
            "    },\n" +
            "    //Deep Warm Ocean\n" +
            "    \"subaquatic:deep_warm_ocean\":{\n" +
            "        \"Surface\":\"0x43D5EE\",\n" +
            "        \"Fog\":\"0x43D5EE\"\n" +
            "    },\n" +
            "    //Lukewarm Ocean\n" +
            "    \"subaquatic:lukewarm_ocean\":{\n" +
            "        \"Surface\":\"0x45ADF2\",\n" +
            "        \"Fog\":\"0x45ADF2\"\n" +
            "    },\n" +
            "    //Deep Lukewarm Ocean\n" +
            "    \"subaquatic:deep_lukewarm_ocean\":{\n" +
            "        \"Surface\":\"0x45ADF2\",\n" +
            "        \"Fog\":\"0x45ADF2\"\n" +
            "    },\n" +
            "    //Cold Ocean\n" +
            "    \"subaquatic:cold_ocean\":{\n" +
            "        \"Surface\":\"0x6092f2\",\n" +
            "        \"Fog\":\"0x6092f2\"\n" +
            "    },\n" +
            "    //Deep Cold Ocean\n" +
            "    \"subaquatic:deep_cold_ocean\":{\n" +
            "        \"Surface\":\"0x6092f2\",\n" +
            "        \"Fog\":\"0x6092f2\"\n" +
            "    },\n" +
            "    //Frozen Ocean\n" +
            "    \"minecraft:frozen_ocean\":{\n" +
            "        \"Surface\":\"0x77a9ff\",\n" +
            "        \"Fog\":\"0x77a9ff\"\n" +
            "    },\n" +
            "    //Deep Frozen Ocean\n" +
            "    \"subaquatic:deep_frozen_ocean\":{\n" +
            "        \"Surface\":\"0x77a9ff\",\n" +
            "        \"Fog\":\"0x77a9ff\"\n" +
            "    }\n" +
            "}";
}
