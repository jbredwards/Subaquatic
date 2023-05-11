package git.jbredwards.subaquatic.api.biome;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 *
 *
 * @since 1.1.0
 * @author jbred
 *
 */
public interface ISeaFloraBiome
{
    /**
     *
     */
    void plantSeaFlower(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos);
}
