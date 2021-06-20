package git.jbredwards.subaquatic.common.event;

import com.google.common.collect.ImmutableMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * handles fuels
 * @author jbred
 *
 */
public class FuelHandler
{
    public static final Map<Pair<Item, Integer>, Integer> BURNTIME = new ImmutableMap.Builder()
            .build();

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void handleFuels(FurnaceFuelBurnTimeEvent event) {
        final ItemStack stack = event.getItemStack();
        @Nullable Integer burntime = BURNTIME.get(Pair.of(stack.getItem(), stack.getMetadata()));

        if(burntime != null) event.setBurnTime(burntime);
    }
}
