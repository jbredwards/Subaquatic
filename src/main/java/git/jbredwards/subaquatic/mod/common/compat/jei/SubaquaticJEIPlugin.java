package git.jbredwards.subaquatic.mod.common.compat.jei;

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
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@JEIPlugin
public final class SubaquaticJEIPlugin implements IModPlugin
{
    @Nonnull
    final ISubtypeRegistry.ISubtypeInterpreter boatContainerInterpreter = stack -> {
        final IBoatType cap = IBoatType.get(stack);
        return cap != null ? cap.getType().serializeNBT().toString() : "";
    };

    @Override
    public void registerItemSubtypes(@Nonnull ISubtypeRegistry subtypeRegistry) {
        subtypeRegistry.registerSubtypeInterpreter(SubaquaticItems.CHEST_BOAT, boatContainerInterpreter);
        subtypeRegistry.registerSubtypeInterpreter(SubaquaticItems.ENDER_CHEST_BOAT, boatContainerInterpreter);
        subtypeRegistry.registerSubtypeInterpreter(SubaquaticItems.CRAFTING_TABLE_BOAT, boatContainerInterpreter);
        subtypeRegistry.registerSubtypeInterpreter(SubaquaticItems.FURNACE_BOAT, boatContainerInterpreter);
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
