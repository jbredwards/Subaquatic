/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.util.villager.career;

import git.jbredwards.ocean_api.api.BucketableEntityBehavior;
import git.jbredwards.subaquatic.mod.common.entity.util.TropicalFishData;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticEntities;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraftforge.fml.common.registry.EntityEntry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 *
 * @author jbred
 *
 */
public enum TradeForEntityBucket implements ITradeList
{
    FISH_BUCKET(new RandomValueRange(6, 8)),
    FISH_BUCKET_SPAWNABLE(new RandomValueRange(6, 8)),
    TROPICAL_FISH_BUCKET(new RandomValueRange(10, 14)),
    TROPICAL_FISH_BUCKET_ANY(new RandomValueRange(15, 20));

    @Nonnull public final List<Function<Random, ItemStack>> trades = new ArrayList<>();
    @Nonnull public final RandomValueRange cost;

    TradeForEntityBucket(@Nonnull final RandomValueRange costIn) { cost = costIn; }
    public static void populate() {
        FISH_BUCKET.trades.add(random -> createFilledBucket(SubaquaticEntities.FISH));
        FISH_BUCKET_SPAWNABLE.trades.add(random -> createFilledBucket(SubaquaticEntities.COD));
        FISH_BUCKET_SPAWNABLE.trades.add(random -> createFilledBucket(SubaquaticEntities.SALMON));
        FISH_BUCKET_SPAWNABLE.trades.add(random -> createTropicalBucket(TropicalFishData.DEFAULT));
        FISH_BUCKET_SPAWNABLE.trades.add(random -> createFilledBucket(SubaquaticEntities.PUFFERFISH));
        TROPICAL_FISH_BUCKET.trades.add(random -> createTropicalBucket(TropicalFishData.random(random, false)));
        TROPICAL_FISH_BUCKET_ANY.trades.add(random -> createTropicalBucket(TropicalFishData.random(random, true)));
    }

    @Nonnull
    public static ItemStack createFilledBucket(@Nonnull final EntityEntry entity) {
        @Nonnull final ItemStack bucket = new ItemStack(Items.WATER_BUCKET);
        bucket.setTagInfo(BucketableEntityBehavior.Util.NBT_KEY, createRootTag(entity.delegate.name()));
        return bucket;
    }

    @Nonnull
    public static ItemStack createTropicalBucket(@Nonnull final TropicalFishData fishData) {
        @Nonnull final ItemStack bucket = new ItemStack(Items.WATER_BUCKET);
        @Nonnull final NBTTagCompound root = createRootTag(SubaquaticEntities.TROPICAL_FISH.delegate.name());

        root.setInteger("Variant", fishData.serialize());
        bucket.setTagInfo(BucketableEntityBehavior.Util.NBT_KEY, root);
        return bucket;
    }

    @Override
    public void addMerchantRecipe(@Nonnull final IMerchant merchant, @Nonnull final MerchantRecipeList recipeList, @Nonnull final Random random) {
        if(trades.isEmpty()) throw new IllegalStateException("Could not find any bucketable entities for trade type: " + name());
        else recipeList.add(new MerchantRecipe(new ItemStack(Items.EMERALD, cost.generateInt(random)), trades.get(random.nextInt(trades.size())).apply(random)));
    }

    @Nonnull
    private static NBTTagCompound createRootTag(@Nonnull final ResourceLocation id) {
        @Nonnull final NBTTagCompound compound = new NBTTagCompound();
        compound.setString("id", id.toString());
        return compound;
    }
}
