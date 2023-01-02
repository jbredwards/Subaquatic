package git.jbredwards.subaquatic.mod.common.item;

import git.jbredwards.subaquatic.mod.common.block.BlockSeaPickle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class ItemBlockSeaPickle extends ItemBlock
{
    @Nonnull
    protected final BlockSeaPickle seaPickle;
    public ItemBlockSeaPickle(@Nonnull BlockSeaPickle block) {
        super(block);
        seaPickle = block;
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        seaPickle.isPlacing.set(true);
        final EnumActionResult result = super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        seaPickle.isPlacing.set(false);
        return result;
    }
}
