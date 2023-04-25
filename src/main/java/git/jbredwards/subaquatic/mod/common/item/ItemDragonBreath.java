package git.jbredwards.subaquatic.mod.common.item;

import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class ItemDragonBreath extends Item
{
    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return SubaquaticConfigHandler.Client.Item.dragonBreathEnchantGlint || super.hasEffect(stack);
    }
}
