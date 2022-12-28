package git.jbredwards.subaquatic.mod.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * better version of ItemFood
 * @author jbred
 *
 */
public class ItemDurationFood extends ItemFood
{
    @Nonnull
    public final List<Pair<PotionEffect, Float>> effects = new ArrayList<>();
    public int maxUseDuration = 32;

    public ItemDurationFood(int amount, boolean isWolfFood) { super(amount, isWolfFood); }
    public ItemDurationFood(int amount, float saturation, boolean isWolfFood) { super(amount, saturation, isWolfFood); }

    @Override
    protected void onFoodEaten(@Nonnull ItemStack stack, @Nonnull World worldIn, @Nonnull EntityPlayer player) {
        if(!worldIn.isRemote) for(Pair<PotionEffect, Float> pair : effects)
            if(pair.getRight() > 0 && worldIn.rand.nextFloat() < pair.getRight())
                player.addPotionEffect(pair.getLeft());
    }

    @Override
    public int getMaxItemUseDuration(@Nonnull ItemStack stack) { return maxUseDuration; }
}
