package git.jbredwards.subaquatic.mod.common.compat.jei.recipe;

import git.jbredwards.subaquatic.mod.common.recipe.BlockSoakRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author jbred
 *
 */
public class BlockSoakJEIRecipeWrapper implements IRecipeWrapper
{
    @Nonnull public final List<List<ItemStack>> inputs;
    @Nonnull public final ItemStack output;

    public BlockSoakJEIRecipeWrapper(@Nonnull BlockSoakRecipe recipeIn) {
        output = recipeIn.output.getRight();
        inputs = Arrays.asList(
                recipeIn.potionTypes.stream()
                        .map(potion -> PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), potion))
                        .collect(Collectors.toList()),
                recipeIn.inputs.stream()
                        .map(Pair::getRight)
                        .collect(Collectors.toList()));
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, inputs);
        ingredients.setOutput(VanillaTypes.ITEM, output);
    }
}
