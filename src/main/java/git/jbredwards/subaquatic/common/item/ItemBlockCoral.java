package git.jbredwards.subaquatic.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class ItemBlockCoral extends ItemBlock
{
    public ItemBlockCoral(@Nonnull Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) { return damage & 1; }

    @Nonnull
    @Override
    public String getTranslationKey(@Nonnull ItemStack stack) {
        return String.format("%s.%s", super.getTranslationKey(stack), (stack.getMetadata() & 1) == 1 ? "dead" : "alive");
    }
}
