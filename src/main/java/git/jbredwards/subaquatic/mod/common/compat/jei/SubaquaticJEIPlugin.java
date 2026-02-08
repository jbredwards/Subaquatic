/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.compat.jei;

import git.jbredwards.subaquatic.api.entity.bucketable.BucketableEntityRegistry;
import git.jbredwards.subaquatic.mod.common.capability.IBoatType;
import git.jbredwards.subaquatic.mod.common.compat.jei.category.BlockSoakJEICategory;
import git.jbredwards.subaquatic.mod.common.compat.jei.recipe.BlockSoakJEIRecipeWrapper;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticItems;
import git.jbredwards.subaquatic.mod.common.recipe.BlockSoakRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
@JEIPlugin
public final class SubaquaticJEIPlugin implements IModPlugin
{
    public enum Interpreters implements ISubtypeRegistry.ISubtypeInterpreter
    {
        BOAT_CONTAINER {
            @Nonnull
            @Override
            public String apply(@Nonnull final ItemStack stack) {
                @Nullable final IBoatType cap = IBoatType.get(stack);
                return cap != null ? cap.getType().serializeNBT().toString() : ISubtypeRegistry.ISubtypeInterpreter.NONE;
            }
        },
        BUCKET_CONTAINER {
            @Nonnull
            @Override
            public String apply(@Nonnull final ItemStack stack) {
                @Nonnull final NBTTagCompound root = stack.getOrCreateSubCompound(BucketableEntityRegistry.NBT_ROOT).copy();
                root.removeTag(BucketableEntityRegistry.NBT_ENTITY_TAGS);
                if(!root.isEmpty()) return root.toString();

                @Nullable final FluidStack fluidStack = FluidUtil.getFluidContained(stack);
                return fluidStack != null ? fluidStack.getFluid().getName() : ISubtypeRegistry.ISubtypeInterpreter.NONE;
            }
        }
    }

    @Override
    public void registerItemSubtypes(@Nonnull ISubtypeRegistry subtypeRegistry) {
        subtypeRegistry.registerSubtypeInterpreter(SubaquaticItems.CHEST_BOAT, Interpreters.BOAT_CONTAINER);
        subtypeRegistry.registerSubtypeInterpreter(SubaquaticItems.ENDER_CHEST_BOAT, Interpreters.BOAT_CONTAINER);
        subtypeRegistry.registerSubtypeInterpreter(SubaquaticItems.CRAFTING_TABLE_BOAT, Interpreters.BOAT_CONTAINER);
        subtypeRegistry.registerSubtypeInterpreter(SubaquaticItems.FURNACE_BOAT, Interpreters.BOAT_CONTAINER);
        for(@Nonnull final Item bucket : BucketableEntityRegistry.BUCKET_REGISTRY.keySet()) subtypeRegistry.registerSubtypeInterpreter(bucket, Interpreters.BUCKET_CONTAINER);
    }

    @Override
    public void register(@Nonnull IModRegistry registry) {
        registry.handleRecipes(BlockSoakRecipe.class, BlockSoakJEIRecipeWrapper::new, BlockSoakJEICategory.ID);
        registry.addRecipes(BlockSoakRecipe.RECIPES, BlockSoakJEICategory.ID);

        registry.addRecipeCatalyst(new ItemStack(SubaquaticItems.CRAFTING_TABLE_MINECART), VanillaRecipeCategoryUid.CRAFTING);
        registry.addRecipeCatalyst(new ItemStack(SubaquaticItems.CRAFTING_TABLE_BOAT, 1, OreDictionary.WILDCARD_VALUE), VanillaRecipeCategoryUid.CRAFTING);
        registry.addRecipeCatalyst(new ItemStack(SubaquaticItems.FURNACE_BOAT, 1, OreDictionary.WILDCARD_VALUE), VanillaRecipeCategoryUid.SMELTING);
        //registry.addRecipeCatalyst(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM, 1, OreDictionary.WILDCARD_VALUE), PotionTypes.WATER), BlockSoakJEICategory.ID);
    }

    @Override
    public void registerCategories(@Nonnull IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(new BlockSoakJEICategory(registry.getJeiHelpers().getGuiHelper()));
    }
}
