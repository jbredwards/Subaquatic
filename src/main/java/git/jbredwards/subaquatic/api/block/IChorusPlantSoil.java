package git.jbredwards.subaquatic.api.block;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import javax.annotation.Nonnull;

/**
 * Blocks that can support chorus plants implement this.
 * @author jbred
 *
 */
public interface IChorusPlantSoil
{
    static boolean isSoil(@Nonnull Block block) {
        return block == Blocks.END_STONE || block instanceof IChorusPlantSoil;
    }
}
