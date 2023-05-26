package git.jbredwards.subaquatic.mod.common.enchantment;

import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentMending;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class EnchantmentCompactFishing extends Enchantment
{
    public EnchantmentCompactFishing(@Nonnull Rarity rarityIn, @Nonnull EnumEnchantmentType typeIn, @Nonnull EntityEquipmentSlot... slots) {
        super(rarityIn, typeIn, slots);
    }

    @Override
    public int getMaxLevel() { return SubaquaticConfigHandler.Common.Item.compactFishingMaxLvl; }

    @Override
    public int getMinEnchantability(int enchantmentLevel) { return super.getMinEnchantability(enchantmentLevel) + 20; }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) { return super.getMaxEnchantability(enchantmentLevel) + 50; }

    @Override
    protected boolean canApplyTogether(@Nonnull Enchantment ench) {
        return (SubaquaticConfigHandler.Common.Item.compactFishingMending || !(ench instanceof EnchantmentMending)) && super.canApplyTogether(ench);
    }
}
