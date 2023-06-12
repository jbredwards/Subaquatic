package git.jbredwards.subaquatic.mod.common.item.tab;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.capability.IEntityBucket;
import git.jbredwards.subaquatic.mod.common.entity.util.fish_bucket.AbstractEntityBucketHandler;
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
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
        final List<ItemStack> buckets = new LinkedList<>();
        final List<Fluid> validFluids = FluidRegistry.getBucketFluids().stream().filter(IEntityBucket::isFluidValid).collect(Collectors.toList());

        IEntityBucket.getValidBuckets().forEach(bucket -> {
            //handle vanilla bucket
            if(bucket == Items.WATER_BUCKET) AbstractEntityBucketHandler.BUCKET_HANDLERS.values().forEach(handler -> handler.get().getSubTypes(buckets, new ItemStack(bucket)));

            //handle forge buckets
            else if(FluidRegistry.isUniversalBucketEnabled() && bucket == ForgeModContainer.getInstance().universalBucket) {
                validFluids.forEach(fluid -> AbstractEntityBucketHandler.BUCKET_HANDLERS.values().forEach(handler -> {
                    final IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(new ItemStack(bucket));
                    if(fluidHandler != null) {
                        final FluidStack fluidStack = new FluidStack(fluid, Fluid.BUCKET_VOLUME);
                        if(fluidHandler.fill(fluidStack, false) >= Fluid.BUCKET_VOLUME) handler.get().getSubTypes(buckets, FluidUtil.getFilledBucket(fluidStack));
                    }
                }));
            }

            //handle modded buckets
            else {
                validFluids.add(0, FluidRegistry.WATER);
                validFluids.forEach(fluid -> AbstractEntityBucketHandler.BUCKET_HANDLERS.values().forEach(handler -> {
                    final ItemStack stack = new ItemStack(bucket);
                    final IFluidHandlerItem fluidHandler = FluidUtil.getFluidHandler(stack);

                    if(fluidHandler != null && fluidHandler.fill(new FluidStack(fluid, Fluid.BUCKET_VOLUME), true) >= Fluid.BUCKET_VOLUME) handler.get().getSubTypes(buckets, stack);
                }));
            }
        });

        return buckets;
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
        return ((getIndex() - 12) % 10) % 5;
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
