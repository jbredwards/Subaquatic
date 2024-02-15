/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.world.gen.feature;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.mod.common.block.AbstractBlockCoral;
import git.jbredwards.subaquatic.mod.common.block.BlockCoralFan;
import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler.Server.World.Coral;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import git.jbredwards.subaquatic.mod.common.world.gen.IConfigurableWorldGenerator;
import git.jbredwards.subaquatic.mod.common.world.gen.feature.coral.*;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 *
 * @author jbred
 *
 */
public enum GeneratorCoral implements IConfigurableWorldGenerator
{
    INSTANCE;

    @Nonnull public static final List<ICoralStructure> CORAL_GENERATORS = new ArrayList<>();
    @Nonnull public static final List<ICoralBlockSupplier> CORAL_BLOCKS = new ArrayList<>();
    @Nonnull public static final List<ICoralBlockSupplier> CORAL_FANS = new ArrayList<>();
    @Nonnull public static final List<ICoralBlockSupplier> CORAL_FINS = new ArrayList<>();

    @Override
    public void generate(@Nonnull Random random, int chunkX, int chunkZ, @Nonnull World world, @Nonnull IChunkGenerator chunkGenerator, @Nonnull IChunkProvider chunkProvider) {
        if(Coral.enabled && isDimensionValid(world, Coral.dimensions)) {
            final int originX = chunkX << 4 | 8;
            final int originZ = chunkZ << 4 | 8;

            final double noiseVal = Biome.GRASS_COLOR_NOISE.getValue(originX / 400.0, originZ / 400.0);
            final int count = (int)Math.ceil(noiseVal * getMaxForBiome(world, originX, originZ, Coral.PER_BIOME_RARITY, Coral.defaultAmount));

            for(int i = 0; i < count; i++) {
                final BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(originX + random.nextInt(16), 0, originZ + random.nextInt(16)));
                final ICoralBlockSupplier coralBlock = CORAL_BLOCKS.get(random.nextInt(CORAL_BLOCKS.size()));

                if(FluidloggedUtils.isCompatibleFluid(coralBlock.getNeededFluid(), FluidloggedUtils.getFluidFromState(world.getBlockState(pos)))) {
                    final IBlockState down = world.getBlockState(pos.down());
                    if(down.isTopSolid() && !CORAL_BLOCKS.contains(down.getBlock()))
                        CORAL_GENERATORS.get(random.nextInt(CORAL_GENERATORS.size())).generate(world, random, pos, coralBlock);
                }
            }
        }
    }

    //for mod devs to use
    public static void registerCoral(@Nonnull List<ICoralBlockSupplier> registeredCorals, @Nonnull AbstractBlockCoral coral) {
        registerCoral(registeredCorals, coral, coral.neededFluid, coral instanceof BlockCoralFan
                ? side -> coral.getDefaultState().withProperty(BlockCoralFan.SIDE, side)
                : side -> coral.getDefaultState());
    }

    //for modpack devs to use alongside groovyscript or the like
    @SuppressWarnings("unchecked")
    public static void registerCoral(@Nonnull List<ICoralBlockSupplier> registeredCorals, @Nonnull Block coral, @Nonnull Fluid neededFluid) {
        final IProperty<EnumFacing> facingProp = (IProperty<EnumFacing>)coral.getBlockState().getProperties().stream()
                .filter(prop -> prop.getValueClass() == EnumFacing.class && (prop.getName().equals("facing") || prop.getName().equals("side") || prop.getName().equals("rotation")))
                .findFirst()
                .orElse(null);

        registerCoral(registeredCorals, coral, neededFluid, facingProp != null ? side -> coral.getDefaultState().withProperty(facingProp, side) : side -> coral.getDefaultState());
    }

    //for modpack devs to use alongside groovyscript or the like
    public static void registerCoral(@Nonnull List<ICoralBlockSupplier> registeredCorals, @Nonnull Block coral, @Nonnull Fluid neededFluid, @Nonnull Function<EnumFacing, IBlockState> directionHandler) {
        registeredCorals.add(new ICoralBlockSupplier() {
            @Nonnull
            @Override
            public Block getBlock() { return coral; }

            @Nonnull
            @Override
            public Fluid getNeededFluid() { return neededFluid; }

            @Nonnull
            @Override
            public IBlockState withDirection(@Nonnull EnumFacing direction) { return directionHandler.apply(direction); }
        });
    }

    //internal
    public static void registerDefaults() {
        CORAL_GENERATORS.add(CoralStructureClaw.INSTANCE);
        CORAL_GENERATORS.add(CoralStructureMushroom.INSTANCE);
        CORAL_GENERATORS.add(CoralStructureTree.INSTANCE);

        registerCoral(CORAL_BLOCKS, SubaquaticBlocks.BRAIN_CORAL_BLOCK);
        registerCoral(CORAL_BLOCKS, SubaquaticBlocks.BUBBLE_CORAL_BLOCK);
        registerCoral(CORAL_BLOCKS, SubaquaticBlocks.FIRE_CORAL_BLOCK);
        registerCoral(CORAL_BLOCKS, SubaquaticBlocks.HORN_CORAL_BLOCK);
        registerCoral(CORAL_BLOCKS, SubaquaticBlocks.TUBE_CORAL_BLOCK);

        registerCoral(CORAL_FANS, SubaquaticBlocks.BRAIN_CORAL_FAN);
        registerCoral(CORAL_FANS, SubaquaticBlocks.BUBBLE_CORAL_FAN);
        registerCoral(CORAL_FANS, SubaquaticBlocks.FIRE_CORAL_FAN);
        registerCoral(CORAL_FANS, SubaquaticBlocks.HORN_CORAL_FAN);
        registerCoral(CORAL_FANS, SubaquaticBlocks.TUBE_CORAL_FAN);

        registerCoral(CORAL_FINS, SubaquaticBlocks.BRAIN_CORAL_FIN);
        registerCoral(CORAL_FINS, SubaquaticBlocks.BUBBLE_CORAL_FIN);
        registerCoral(CORAL_FINS, SubaquaticBlocks.FIRE_CORAL_FIN);
        registerCoral(CORAL_FINS, SubaquaticBlocks.HORN_CORAL_FIN);
        registerCoral(CORAL_FINS, SubaquaticBlocks.TUBE_CORAL_FIN);
    }
}
