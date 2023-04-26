package git.jbredwards.subaquatic.mod.common.item.block;

import git.jbredwards.subaquatic.mod.client.item.ICustomModel;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class ItemBlockMeta extends ItemBlock implements ICustomModel
{
    @Nonnull protected final String[] variants;
    @Nonnull protected final String property;
    protected final boolean hasUniqueModel;

    public ItemBlockMeta(@Nonnull Block blockIn, boolean hasUniqueModelIn, @Nonnull String propertyIn, @Nonnull String... variantsIn) {
        super(blockIn);
        setHasSubtypes(true);
        property = propertyIn;
        variants = variantsIn;
        hasUniqueModel = hasUniqueModelIn;
    }

    public ItemBlockMeta(@Nonnull Block blockIn, boolean hasUniqueModelIn, @Nonnull IProperty<?> propertyIn) {
        this(blockIn, hasUniqueModelIn, propertyIn.getName(), propertyIn.getAllowedValues().stream().map(String::valueOf).toArray(String[]::new));
    }

    @Override
    public int getMetadata(int damage) { return MathHelper.clamp(damage, 0, variants.length - 1); }

    @Override
    public int getMetadata(@Nonnull ItemStack stack) { return getMetadata(super.getMetadata(stack)); }

    @Nonnull
    @Override
    public String getTranslationKey(@Nonnull ItemStack stack) {
        return String.format("%s.%s.%s", super.getTranslationKey(stack), property, variants[stack.getMetadata()]);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if(isInCreativeTab(tab)) for(int meta = 0; meta < variants.length; meta++) items.add(new ItemStack(this, 1, meta));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        for(int meta = 0; meta < variants.length; meta++) {
            ModelLoader.setCustomModelResourceLocation(this, meta, new ModelResourceLocation(delegate.name(),
                    String.format(hasUniqueModel ? "inventory:%s=%s" : "%s=%s", property, variants[meta])));
        }
    }
}
