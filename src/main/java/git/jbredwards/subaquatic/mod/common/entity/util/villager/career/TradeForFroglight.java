package git.jbredwards.subaquatic.mod.common.entity.util.villager.career;

import git.jbredwards.subaquatic.mod.common.init.SubaquaticItems;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public enum TradeForFroglight implements ITradeList
{
    FROGLIGHT((merchant, recipeList, random) -> recipeList.add(new MerchantRecipe(
            new ItemStack(Items.EMERALD, MathHelper.getInt(random, 32, 40)),
            new ItemStack(Items.MAGMA_CREAM, 4),
            new ItemStack(SubaquaticItems.FROGLIGHT, 1, random.nextInt(3))
    )));

    @Nonnull
    final ITradeList tradeHandler;
    TradeForFroglight(@Nonnull ITradeList tradeHandlerIn) { tradeHandler = tradeHandlerIn; }

    @Override
    public void addMerchantRecipe(@Nonnull IMerchant merchant, @Nonnull MerchantRecipeList recipeList, @Nonnull Random random) {
        tradeHandler.addMerchantRecipe(merchant, recipeList, random);
    }
}
