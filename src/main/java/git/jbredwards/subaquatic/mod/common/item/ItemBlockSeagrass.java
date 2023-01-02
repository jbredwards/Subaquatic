package git.jbredwards.subaquatic.mod.common.item;

import git.jbredwards.subaquatic.mod.common.block.BlockSeagrass;
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
public class ItemBlockSeagrass extends ItemBlock
{
    @Nonnull
    protected final BlockSeagrass seagrass;
    public ItemBlockSeagrass(@Nonnull BlockSeagrass block) {
        super(block);
        seagrass = block;
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        seagrass.isPlacing.set(true);
        final EnumActionResult result = super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        seagrass.isPlacing.set(false);
        return result;
    }
}
