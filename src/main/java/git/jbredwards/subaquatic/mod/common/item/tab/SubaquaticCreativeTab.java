/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.item.tab;

import com.google.common.collect.ImmutableList;
import git.jbredwards.ocean_api.api.BucketableEntityBehavior;
import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticEnchantments;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticEntities;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author jbred
 *
 */
public final class SubaquaticCreativeTab extends CreativeTabs
{
    public static final SubaquaticCreativeTab INSTANCE = new SubaquaticCreativeTab();
    SubaquaticCreativeTab() { super(Subaquatic.MODID + ".tab"); }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack createIcon() { return SubaquaticItems.NAUTILUS_SHELL.getDefaultInstance(); }

    @SideOnly(Side.CLIENT)
    @Override
    public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> items) {
        super.displayAllRelevantItems(items);

        //dynamically add all entity buckets to tab
        items.addAll(generateEntityBuckets());

        //dynamically add all entity eggs to tab
        SubaquaticEntities.INIT.forEach(entry -> {
            if(entry.getEgg() != null) {
                final ItemStack spawnEgg = new ItemStack(Items.SPAWN_EGG);
                ItemMonsterPlacer.applyEntityIdToItemStack(spawnEgg, entry.getEgg().spawnedID);
                items.add(spawnEgg);
            }
        });

        //dynamically add all enchanted books to tab
        SubaquaticEnchantments.INIT.forEach(ench -> {
            for(int lvl = ench.getMinLevel(); lvl <= ench.getMaxLevel(); lvl++)
                items.add(ItemEnchantedBook.getEnchantedItemStack(new EnchantmentData(ench, lvl)));
        });
    }

    @Nonnull
    public static List<ItemStack> generateEntityBuckets() {
        return BucketableEntityBehavior.Util.getBaseItems().stream()
                .flatMap(item -> {
                    @Nonnull final NonNullList<ItemStack> items = NonNullList.create();
                    item.getSubItems(SEARCH, items);
                    return items.stream();
                })
                .flatMap(stack -> {
                    @Nonnull final Stream.Builder<ItemStack> builder = Stream.builder();
                    for(@Nonnull final BucketableEntityBehavior behavior : BucketableEntityBehavior.REGISTRY) {
                        if(Subaquatic.MODID.equals(behavior.getRegistryName().getNamespace())
                        && BucketableEntityBehavior.Util.canHoldBehavior(stack, behavior)) {
                            for(@Nonnull final NBTTagCompound compound : behavior.subtypes()) {
                                @Nonnull final ItemStack copy = stack.copy();
                                copy.setTagInfo(BucketableEntityBehavior.Util.NBT_KEY, compound);
                                builder.accept(copy);
                            }
                        }
                    }

                    return builder.build();
                })
                .collect(ImmutableList.toImmutableList());
    }

    @Override
    public boolean hasSearchBar() { return true; }

    @Override
    public int getLabelColor() { return 0x716d52; }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public ResourceLocation getBackgroundImage() {
        return new ResourceLocation(Subaquatic.MODID, "textures/gui/container/creative_inventory/items.png");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasScrollbar() {
        applyTabsTexture();
        return super.hasScrollbar();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColumn() {
        applyTabsTexture();
        return super.getColumn();
    }

    //this is a very hacky way to apply the custom tabs texture, will probably be improved in the future
    @SideOnly(Side.CLIENT)
    static void applyTabsTexture() {
        final TextureManager manager = Minecraft.getMinecraft().getTextureManager();
        final ITextureObject texture = manager.getTexture(GuiContainerCreative.CREATIVE_INVENTORY_TABS);
        //ensure the currently applied texture is the one that needs to be replaced
        if(texture != null && texture.getGlTextureId() == GlStateManager.textureState[GlStateManager.activeTextureUnit].textureName)
            manager.bindTexture(new ResourceLocation(Subaquatic.MODID, "textures/gui/container/creative_inventory/tabs.png"));
    }
}
