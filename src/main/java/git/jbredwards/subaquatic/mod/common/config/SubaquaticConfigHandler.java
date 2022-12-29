package git.jbredwards.subaquatic.mod.common.config;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * @author jbred
 *
 */
public final class SubaquaticConfigHandler
{
    @Nonnull
    static final Map<Block, BubbleColumnPredicate>
        BUBBLE_COLUMN_PULL = new HashMap<>(), BUBBLE_COLUMN_PUSH = new HashMap<>();

    @Nullable
    public static BubbleColumnPredicate getBubbleColumnConditions(@Nonnull Block soil, boolean isPull) {
        return (isPull ? BUBBLE_COLUMN_PULL : BUBBLE_COLUMN_PUSH).get(soil);
    }

    //THIS IS A TEST, IT WILL BE REMOVED
    static {
        BUBBLE_COLUMN_PULL.put(Blocks.MAGMA, new BubbleColumnPredicate(Blocks.MAGMA, new int[0], new HashSet<>()));
        BUBBLE_COLUMN_PUSH.put(Blocks.SOUL_SAND, new BubbleColumnPredicate(Blocks.SOUL_SAND, new int[0], new HashSet<>()));
    }
}
