package git.jbredwards.subaquatic.mod.common.world.gen.feature;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import git.jbredwards.subaquatic.api.biome.IOceanBiome;
import git.jbredwards.subaquatic.mod.common.block.BlockCoral;
import git.jbredwards.subaquatic.mod.common.block.BlockCoralFan;
import git.jbredwards.subaquatic.mod.common.block.BlockCoralFin;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticBlocks;
import git.jbredwards.subaquatic.mod.common.world.biome.BiomeWarmOcean;
import git.jbredwards.subaquatic.mod.common.world.gen.feature.coral.CoralStructureClaw;
import git.jbredwards.subaquatic.mod.common.world.gen.feature.coral.CoralStructureMushroom;
import git.jbredwards.subaquatic.mod.common.world.gen.feature.coral.CoralStructureTree;
import git.jbredwards.subaquatic.mod.common.world.gen.feature.coral.ICoralStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public enum GeneratorCoral implements IWorldGenerator
{
    INSTANCE;

    @Nonnull public static final List<ICoralStructure> CORAL_GENERATORS = new ArrayList<>();
    @Nonnull public static final List<BlockCoral> CORAL_BLOCKS = new ArrayList<>();
    @Nonnull public static final List<BlockCoralFan> CORAL_FANS = new ArrayList<>();
    @Nonnull public static final List<BlockCoralFin> CORAL_FINS = new ArrayList<>();

    @Override
    public void generate(@Nonnull Random random, int chunkX, int chunkZ, @Nonnull World world, @Nonnull IChunkGenerator chunkGenerator, @Nonnull IChunkProvider chunkProvider) {
        final int originX = chunkX << 4 | 8;
        final int originZ = chunkZ << 4 | 8;

        final double noiseVal = Biome.GRASS_COLOR_NOISE.getValue(originX / 400.0, originZ / 400.0);
        final int count = (int)Math.ceil(noiseVal * getMaxForBiome(world.getBiome(new BlockPos(originX, 0, originZ))));

        for(int i = 0; i < count; i++) {
            final BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(originX + random.nextInt(16), 0, originZ + random.nextInt(16)));
            if(IOceanBiome.isOcean(world.getBiome(pos))) {
                final BlockCoral coral = CORAL_BLOCKS.get(random.nextInt(CORAL_BLOCKS.size()));
                if(FluidloggedUtils.isCompatibleFluid(coral.neededFluid, FluidloggedUtils.getFluidFromState(world.getBlockState(pos)))) {
                    final IBlockState down = world.getBlockState(pos.down());
                    if(down.isTopSolid() && !CORAL_BLOCKS.contains(down.getBlock()))
                        CORAL_GENERATORS.get(random.nextInt(CORAL_GENERATORS.size())).generate(world, random, pos, coral);
                }
            }
        }
    }

    static int getMaxForBiome(@Nonnull Biome biome) {
        return biome instanceof BiomeWarmOcean && ((BiomeWarmOcean)biome).getDeepOceanBiomeId() != -1 ? 20 : 0;
    }

    public static void initDefaults() {
        CORAL_GENERATORS.clear();
        CORAL_GENERATORS.add(CoralStructureClaw.INSTANCE);
        CORAL_GENERATORS.add(CoralStructureMushroom.INSTANCE);
        CORAL_GENERATORS.add(CoralStructureTree.INSTANCE);

        CORAL_BLOCKS.clear();
        CORAL_BLOCKS.add(SubaquaticBlocks.BRAIN_CORAL_BLOCK);
        CORAL_BLOCKS.add(SubaquaticBlocks.BUBBLE_CORAL_BLOCK);
        CORAL_BLOCKS.add(SubaquaticBlocks.FIRE_CORAL_BLOCK);
        CORAL_BLOCKS.add(SubaquaticBlocks.HORN_CORAL_BLOCK);
        CORAL_BLOCKS.add(SubaquaticBlocks.TUBE_CORAL_BLOCK);

        CORAL_FANS.clear();
        CORAL_FANS.add(SubaquaticBlocks.BRAIN_CORAL_FAN);
        CORAL_FANS.add(SubaquaticBlocks.BUBBLE_CORAL_FAN);
        CORAL_FANS.add(SubaquaticBlocks.FIRE_CORAL_FAN);
        CORAL_FANS.add(SubaquaticBlocks.HORN_CORAL_FAN);
        CORAL_FANS.add(SubaquaticBlocks.TUBE_CORAL_FAN);

        CORAL_FINS.clear();
        CORAL_FINS.add(SubaquaticBlocks.BRAIN_CORAL_FIN);
        CORAL_FINS.add(SubaquaticBlocks.BUBBLE_CORAL_FIN);
        CORAL_FINS.add(SubaquaticBlocks.FIRE_CORAL_FIN);
        CORAL_FINS.add(SubaquaticBlocks.HORN_CORAL_FIN);
        CORAL_FINS.add(SubaquaticBlocks.TUBE_CORAL_FIN);
    }
}
