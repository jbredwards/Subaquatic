package git.jbredwards.subaquatic.common.init;

import git.jbredwards.subaquatic.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.List;

/**
 * stores all of this mod's crafting recipes
 * @author jbred
 *
 */
@SuppressWarnings("unused")
public enum ModCrafting
{
    ;

    //recipe init
    public static final List<IRecipe> INIT = new ArrayList<>();

    //shaped
    public static final IRecipe DRIED_KELP_BLOCK = register(true, "dried_kelp_block", ModItems.DRIED_KELP_BLOCK, "III", "III", "III", 'I', ModItems.DRIED_KELP);
    //shapeless
    public static final IRecipe DRIED_KELP = register(false, "dried_kelp", new ItemStack(ModItems.DRIED_KELP, 9), ModItems.DRIED_KELP_BLOCK);

    //formats the inputs into a recipe
    private static IRecipe register(boolean shaped, String name, Item out, Object... in) { return register(shaped, name, new ItemStack(out), in); }
    private static IRecipe register(boolean shaped, String name, Block out, Object... in) { return register(shaped, name, new ItemStack(out), in); }
    private static IRecipe register(boolean shaped, String name, ItemStack out, Object... in) {
        if(shaped) return register(name + "_shaped",    new ShapedOreRecipe(new ResourceLocation(Constants.MODID, Constants.NAME), out, in));
        else       return register(name + "_shapeless", new ShapelessOreRecipe(new ResourceLocation(Constants.MODID, Constants.NAME), out, in));
    }

    //prepares the recipe for registration
    private static IRecipe register(String name, IRecipe recipe) {
        recipe.setRegistryName(new ResourceLocation(Constants.MODID, name));
        INIT.add(recipe);
        return recipe;
    }
}
