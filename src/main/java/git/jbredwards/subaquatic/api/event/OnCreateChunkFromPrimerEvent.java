package git.jbredwards.subaquatic.api.event;

import git.jbredwards.fluidlogged_api.api.asm.impl.IFluidStatePrimer;
import git.jbredwards.fluidlogged_api.api.util.FluidState;
import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 * Allows mods to utilize {@link ChunkPrimer} for world gen. Remember that this is called before the Chunk is set in the world!
 * This is fired through the {@link net.minecraftforge.common.MinecraftForge#TERRAIN_GEN_BUS TERRAIN_GEN_BUS}.
 * <p>
 * General guide to event priority:
 * HIGH = terrain;
 * NORMAL = structures;
 * LOW = plants
 *
 * @since 1.0.2
 * @author jbred
 *
 */
@Cancelable
public class OnCreateChunkFromPrimerEvent extends Event
{
    @Nonnull public final Chunk chunk;
    @Nonnull public final ChunkPrimer primer;
    @Nonnull public final Random rand;

    @Nullable
    protected Biome[] biomesForGen = null;
    public OnCreateChunkFromPrimerEvent(@Nonnull Chunk chunkIn, @Nonnull ChunkPrimer primerIn) {
        chunk = chunkIn;
        primer = primerIn;

        final long worldSeed = getWorld().getSeed();
        rand = new Random(worldSeed);

        final long xSeed = rand.nextLong() >> 2 | 1;
        final long zSeed = rand.nextLong() >> 2 | 1;
        rand.setSeed((xSeed * getChunkX() + zSeed * getChunkZ()) ^ worldSeed);
    }

    public int getChunkX() { return chunk.x; }
    public int getChunkZ() { return chunk.z; }

    @Nonnull
    public World getWorld() { return chunk.getWorld(); }

    @Nonnull
    public Biome getBiome(@Nonnull BlockPos pos) {
        if(isOutsideChunk(pos)) return getWorld().getBiome(pos);
        return getBiome(pos.getX() & 15, pos.getZ() & 15);
    }

    @Nonnull
    public Biome getBiome(int x, int z) {
        return (biomesForGen != null ? biomesForGen : (biomesForGen = getWorld().getBiomeProvider().getBiomes(null, getChunkX() << 4, getChunkZ() << 4, 16, 16)))[x << 4 | z];
    }

    @Nonnull
    public IBlockState getBlockState(@Nonnull BlockPos pos) {
        if(isOutsideChunk(pos)) return getWorld().getBlockState(pos);
        return getWorld().isOutsideBuildHeight(pos) ? ChunkPrimer.DEFAULT_STATE : primer.getBlockState(pos.getX() & 15, pos.getY() & 255, pos.getZ() & 15);
    }

    public void setBlockState(@Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if(isOutsideChunk(pos)) getWorld().setBlockState(pos, state, Constants.BlockFlags.NO_OBSERVERS);
        else if(!getWorld().isOutsideBuildHeight(pos)) primer.setBlockState(pos.getX() & 15, pos.getY() & 255, pos.getZ() & 15, state);
    }

    @Nonnull
    public FluidState getFluidState(@Nonnull BlockPos pos) {
        if(getWorld().isOutsideBuildHeight(pos)) return FluidState.EMPTY;
        else if(isOutsideChunk(pos)) return FluidloggedUtils.getFluidState(getWorld(), pos);

        final FluidState fluidState = IFluidStatePrimer.of(primer).getFluidState(pos.getX() & 15, pos.getY() & 255, pos.getZ() & 15);
        if(!fluidState.isEmpty()) return fluidState;

        return FluidState.of(primer.getBlockState(pos.getX() & 15, pos.getY() & 255, pos.getZ() & 15));
    }

    public void setFluidState(@Nonnull BlockPos pos, @Nonnull FluidState fluidState) {
        if(isOutsideChunk(pos)) FluidloggedUtils.setFluidState(getWorld(), pos, null, fluidState, false, false, Constants.BlockFlags.NO_OBSERVERS);
        else if(!getWorld().isOutsideBuildHeight(pos)) IFluidStatePrimer.of(primer).setFluidState(pos.getX() & 15, pos.getY() & 255, pos.getZ() & 15, fluidState);
    }

    public int findTopSolidNonLiquidBlock(int x, int z) {
        final int groundIndex = primer.findGroundBlockIdx(x, z);
        for(int y = groundIndex; y >= 0; --y) {
            final IBlockState state = primer.getBlockState(x, y, z);
            if(state.getMaterial().blocksMovement() && !FluidloggedUtils.isFluid(state)) {
                final BlockPos pos = getPos(x, y, z);
                if(!state.getBlock().isLeaves(state, getWorld(), pos) && !state.getBlock().isFoliage(getWorld(), pos))
                    return y + 1;
            }
        }

        return groundIndex;
    }

    @Nonnull
    public BlockPos getPos(int x, int y, int z) { return new BlockPos(getChunkX() << 4 | x, y, getChunkZ() << 4 | z); }
    public boolean isOutsideChunk(@Nonnull BlockPos pos) { return !chunk.isAtLocation(pos.getX() >> 4, pos.getZ() >> 4); }
}
