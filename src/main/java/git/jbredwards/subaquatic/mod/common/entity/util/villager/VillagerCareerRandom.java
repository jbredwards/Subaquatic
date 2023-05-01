package git.jbredwards.subaquatic.mod.common.entity.util.villager;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * A villager career that chooses random trades, like vanilla 1.14
 * @author jbred
 *
 */
public class VillagerCareerRandom extends VillagerCareer
{
    protected final int numOfTrades; //the max number of trades that can be unlocked at a time
    public VillagerCareerRandom(@Nonnull VillagerProfession parent, @Nonnull String name, int numOfTradesIn) {
        super(parent, name);
        numOfTrades = numOfTradesIn;
    }

    @Nullable
    @Override
    public List<ITradeList> getTrades(int level) {
        final List<ITradeList> unShuffledTrades = super.getTrades(level);
        if(unShuffledTrades == null || unShuffledTrades.size() <= numOfTrades) return unShuffledTrades;

        final ITradeList[] shuffledTrades = unShuffledTrades.toArray(new ITradeList[0]);
        Collections.shuffle(Arrays.asList(shuffledTrades));

        return ImmutableList.copyOf(Arrays.copyOf(shuffledTrades, numOfTrades));
    }
}
