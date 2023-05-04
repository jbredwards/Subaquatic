package git.jbredwards.subaquatic.mod.common.entity.util.villager.career;

import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public final class TradeUtil
{
    @Nonnull
    public static ITradeList newTrade(@Nonnull Item input, int minIn, int maxIn, @Nonnull Item output, int minOut, int maxOut) {
        return newTrade(new ItemStack(input), minIn, maxIn, new ItemStack(output), minOut, maxOut);
    }

    @Nonnull
    public static ITradeList newTrade(@Nonnull ItemStack input, int minIn, int maxIn, @Nonnull Item output, int minOut, int maxOut) {
        return newTrade(input, minIn, maxIn, new ItemStack(output), minOut, maxOut);
    }

    @Nonnull
    public static ITradeList newTrade(@Nonnull Item input, int minIn, int maxIn, @Nonnull ItemStack output, int minOut, int maxOut) {
        return newTrade(new ItemStack(input), minIn, maxIn, output, minOut, maxOut);
    }

    @Nonnull
    public static ITradeList newTrade(@Nonnull ItemStack input, int minIn, int maxIn, @Nonnull ItemStack output, int minOut, int maxOut) {
        return (merchant, recipeList, random) -> recipeList.add(new MerchantRecipe(
                ItemHandlerHelper.copyStackWithSize(input, MathHelper.getInt(random, minIn, maxIn)),
                ItemHandlerHelper.copyStackWithSize(output, MathHelper.getInt(random, minOut, maxOut))
        ));
    }

    @Nonnull
    public static ITradeList or(@Nonnull ITradeList trade1, @Nonnull ITradeList trade2) {
        return (merchant, recipeList, random) -> (random.nextBoolean() ? trade1 : trade2).addMerchantRecipe(merchant, recipeList, random);
    }
}
