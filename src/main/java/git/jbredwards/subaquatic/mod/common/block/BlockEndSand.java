package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.subaquatic.api.block.IChorusPlantSoil;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class BlockEndSand extends BlockFalling implements IChorusPlantSoil
{
    @SideOnly(Side.CLIENT)
    @Override
    public int getDustColor(@Nonnull IBlockState state) { return 0xc3bd89; }
}
