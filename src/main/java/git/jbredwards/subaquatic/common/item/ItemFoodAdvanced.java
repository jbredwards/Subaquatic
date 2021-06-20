package git.jbredwards.subaquatic.common.item;

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
@SuppressWarnings("NullableProblems")
public class ItemFoodAdvanced extends ItemFood
{
    @Nonnull public List<Pair<PotionEffect, Float>> effects = new ArrayList<>();
    public int maxUseDuration;

    public ItemFoodAdvanced(int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
    }

    public ItemFoodAdvanced(int amount, boolean isWolfFood) {
        super(amount, isWolfFood);
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        if(!worldIn.isRemote && !effects.isEmpty()) {
            for(Pair<PotionEffect, Float> pair : effects) {
                if(worldIn.rand.nextFloat() < pair.getRight()) player.addPotionEffect(pair.getLeft());
            }
        }
    }

    @Override
    public ItemFood setPotionEffect(PotionEffect effect, float probability) {
        effects.add(Pair.of(effect, probability));
        return this;
    }

    public ItemFood setMaxItemUseDuration(int duration) {
        maxUseDuration = duration;
        return this;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return maxUseDuration;
    }
}
