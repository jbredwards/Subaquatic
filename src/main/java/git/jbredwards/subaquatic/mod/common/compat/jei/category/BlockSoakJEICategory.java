package git.jbredwards.subaquatic.mod.common.compat.jei.category;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.compat.jei.recipe.BlockSoakJEIRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.config.Constants;
import mezz.jei.util.Translator;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class BlockSoakJEICategory implements IRecipeCategory<BlockSoakJEIRecipeWrapper>
{
    @Nonnull
    public static final String ID = Subaquatic.MODID + ":block_soak";

    @Nonnull
    public final IDrawable background, icon;
    public BlockSoakJEICategory(@Nonnull IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(Constants.RECIPE_GUI_VANILLA, 0, 168, 125, 18);
        icon = guiHelper.createDrawableIngredient(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER));
    }

    @Nonnull
    @Override
    public String getUid() { return ID; }

    @Nonnull
    @Override
    public String getTitle() { return Translator.translateToLocal("gui.subaquatic.jei.category.block_soak"); }

    @Nonnull
    @Override
    public String getModName() { return Subaquatic.NAME; }

    @Nonnull
    @Override
    public IDrawable getBackground() { return background; }

    @Nonnull
    @Override
    public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull BlockSoakJEIRecipeWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
        final IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 0, 0);
        guiItemStacks.init(1, true, 49, 0);
        guiItemStacks.init(2, false, 107, 0);
        guiItemStacks.set(ingredients);
    }
}
