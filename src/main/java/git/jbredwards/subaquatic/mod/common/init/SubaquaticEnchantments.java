package git.jbredwards.subaquatic.mod.common.init;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.enchantment.EnchantmentCompactFishing;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

/**
 * stores all of this mod's enchantments
 * @author jbred
 *
 */
public final class SubaquaticEnchantments
{
    // Init
    @Nonnull public static final List<Enchantment> INIT = new LinkedList<>();

    // Enchantments
    @Nonnull public static final EnchantmentCompactFishing COMPACT_FISHING = register("compact_fishing", new EnchantmentCompactFishing(Enchantment.Rarity.VERY_RARE, EnumEnchantmentType.FISHING_ROD, EntityEquipmentSlot.MAINHAND));

    // Registry
    @Nonnull static <T extends Enchantment> T register(@Nonnull String name, @Nonnull T ench) {
        INIT.add(ench.setName(Subaquatic.MODID + '.' + name).setRegistryName(Subaquatic.MODID, name));
        return ench;
    }
}
