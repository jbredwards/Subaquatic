package git.jbredwards.subaquatic.api.event;

import com.google.common.base.Preconditions;
import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nonnull;

/**
 * Used to determine the correct ocean biome to generate.
 * This is fired through the {@link net.minecraftforge.common.MinecraftForge#TERRAIN_GEN_BUS TERRAIN_GEN_BUS}.
 * See {@link git.jbredwards.subaquatic.mod.common.world.gen.layer.GenLayerOceanBiomes GenLayerOceanBiomes}
 * for this mod's 1.13 ocean biome generation.
 * @author jbred
 *
 */
@Cancelable
public abstract class GetOceanForGenEvent extends Event
{
    @Nonnull
    protected Biome oceanForGen;
    public GetOceanForGenEvent(@Nonnull Biome oceanForGenIn) { oceanForGen = ensureIsOcean(oceanForGenIn); }

    @Nonnull
    protected static Biome ensureIsOcean(@Nonnull Biome oceanForGenIn) {
        Preconditions.checkNotNull(oceanForGenIn);
        Preconditions.checkArgument(BiomeManager.oceanBiomes.contains(oceanForGenIn),
                "Biome for generation \"%s\" is not an ocean biome!", oceanForGenIn.getRegistryName());

        return oceanForGenIn;
    }

    @Nonnull
    public Biome getOcean() { return oceanForGen; }
    public void setOcean(@Nonnull Biome oceanForGenIn) {
        oceanForGen = ensureIsOcean(oceanForGenIn);
        setCanceled(true);
    }

    /**
     * Fired when fetching shallow ocean biomes.
     */
    @Cancelable
    public static class Shallow extends GetOceanForGenEvent
    {
        public final double temperatureNoise;
        public Shallow(double temperatureNoiseIn) {
            super(Biomes.OCEAN);
            temperatureNoise = temperatureNoiseIn;
        }
    }

    /**
     * Fired when fetching deep ocean biomes.
     */
    @Cancelable
    public static class Deep extends GetOceanForGenEvent
    {
        public final double temperatureNoise;
        public Deep(double temperatureNoiseIn) {
            super(Biomes.DEEP_OCEAN);
            temperatureNoise = temperatureNoiseIn;
        }
    }

    /**
     * TODO
     * Fired when fetching the result of mixing neighboring ocean biomes.
     */
    @Cancelable
    public static class Mixed extends GetOceanForGenEvent
    {
        public Mixed(@Nonnull Biome oceanForGenIn) {
            super(oceanForGenIn);
        }
    }
}
