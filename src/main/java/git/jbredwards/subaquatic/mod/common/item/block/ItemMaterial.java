/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.item.block;

import git.jbredwards.subaquatic.mod.client.item.ICustomModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class ItemMaterial extends Item implements ICustomModel
{
    @Nonnull
    protected final String[] variants;
    public ItemMaterial(@Nonnull final String... variantsIn) {
        setHasSubtypes(true);
        variants = variantsIn;
    }

    @Nonnull
    @Override
    public EnumRarity getRarity(@Nonnull final ItemStack stack) {
        return stack.isItemEnchanted() ? EnumRarity.RARE : stack.getMetadata() == 0 ? EnumRarity.UNCOMMON : EnumRarity.COMMON;
    }

    @Nonnull
    @Override
    public String getTranslationKey(@Nonnull final ItemStack stack) {
        return super.getTranslationKey(stack) + '.' + variants[Math.min(stack.getMetadata(), variants.length - 1)];
    }

    @Override
    public void getSubItems(@Nonnull final CreativeTabs tab, @Nonnull final NonNullList<ItemStack> items) {
        if(isInCreativeTab(tab)) for(int meta = 0; meta < variants.length; meta++) items.add(new ItemStack(this, 1, meta));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        for(int meta = 0; meta < variants.length; meta++) {
            ModelLoader.setCustomModelResourceLocation(this, meta, new ModelResourceLocation(delegate.name(), variants[meta]));
        }
    }
}
