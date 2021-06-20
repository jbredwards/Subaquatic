package git.jbredwards.subaquatic.common.init;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * stores all of this mod's smelting recipes
 * @author jbred
 *
 */
public enum ModSmelting
{
    ;

    //smelting init
    public static void init() {
        GameRegistry.addSmelting(ModItems.KELP, new ItemStack(ModItems.DRIED_KELP), 0.1f);
    }
}
