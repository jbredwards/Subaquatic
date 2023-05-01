package git.jbredwards.subaquatic.mod.common.entity.util.villager;

import git.jbredwards.subaquatic.mod.common.world.gen.feature.GeneratorCoral;
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
public enum TradeForCoral implements ITradeList
{
    CORAL_BLOCK((merchant, recipeList, random) -> recipeList.add(new MerchantRecipe(
            new ItemStack(Items.EMERALD, MathHelper.getInt(random, 4, 5)),
            new ItemStack(GeneratorCoral.CORAL_BLOCKS.get(random.nextInt(GeneratorCoral.CORAL_BLOCKS.size())))
    ))),
    CORAL_FAN((merchant, recipeList, random) -> recipeList.add(new MerchantRecipe(
            new ItemStack(Items.EMERALD, 3),
            new ItemStack(GeneratorCoral.CORAL_FANS.get(random.nextInt(GeneratorCoral.CORAL_FANS.size())))
    ))),
    CORAL_FIN((merchant, recipeList, random) -> recipeList.add(new MerchantRecipe(
            new ItemStack(Items.EMERALD, 3),
            new ItemStack(GeneratorCoral.CORAL_FINS.get(random.nextInt(GeneratorCoral.CORAL_FINS.size())))
    )));

    @Nonnull
    final ITradeList tradeHandler;
    TradeForCoral(@Nonnull ITradeList tradeHandlerIn) { tradeHandler = tradeHandlerIn; }

    @Override
    public void addMerchantRecipe(@Nonnull IMerchant merchant, @Nonnull MerchantRecipeList recipeList, @Nonnull Random random) {
        tradeHandler.addMerchantRecipe(merchant, recipeList, random);
    }
}
