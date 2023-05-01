package git.jbredwards.subaquatic.mod.common.entity.util.villager;

import git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket.*;
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
public enum TradeForEntityBucket implements ITradeList
{
    FISH_BUCKET((merchant, recipeList, random) -> recipeList.add(new MerchantRecipe(
            new ItemStack(Items.EMERALD, MathHelper.getInt(random, 6, 8)),
            new EntityBucketHandlerFish().createNewStack(new ItemStack(Items.WATER_BUCKET))
    ))),
    FISH_BUCKET_SPAWNABLE((merchant, recipeList, random) -> {
        final AbstractEntityBucketHandler handler = new AbstractEntityBucketHandler[] {
                new EntityBucketHandlerCod(),
                new EntityBucketHandlerSalmon(),
                new EntityBucketHandlerTropicalFish(),
                new EntityBucketHandlerPufferfish()}[random.nextInt(4)];

        recipeList.add(new MerchantRecipe(
                new ItemStack(Items.EMERALD, MathHelper.getInt(random, 6, 8)),
                handler.createNewStack(new ItemStack(Items.WATER_BUCKET))));
    }),
    TROPICAL_FISH_BUCKET((merchant, recipeList, random) -> recipeList.add(new MerchantRecipe(
            new ItemStack(Items.EMERALD, MathHelper.getInt(random, 10, 14)),
            new EntityBucketHandlerTropicalFish().createNewStackRandom(new ItemStack(Items.WATER_BUCKET), random)
    ))),
    TROPICAL_FISH_BUCKET_ANY((merchant, recipeList, random) -> recipeList.add(new MerchantRecipe(
            new ItemStack(Items.EMERALD, MathHelper.getInt(random, 15, 20)),
            new EntityBucketHandlerTropicalFish().createNewStackTrueRandom(new ItemStack(Items.WATER_BUCKET), random)
    )));

    @Nonnull
    final ITradeList tradeHandler;
    TradeForEntityBucket(@Nonnull ITradeList tradeHandlerIn) { tradeHandler = tradeHandlerIn; }

    @Override
    public void addMerchantRecipe(@Nonnull IMerchant merchant, @Nonnull MerchantRecipeList recipeList, @Nonnull Random random) {
        tradeHandler.addMerchantRecipe(merchant, recipeList, random);
    }
}
