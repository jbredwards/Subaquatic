package git.jbredwards.subaquatic.mod.common.item.block;

import git.jbredwards.subaquatic.mod.client.item.ICustomModel;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.ModelLoader;

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

    protected ItemBlockMeta(@Nonnull Block blockIn, @Nonnull String propertyIn, boolean hasUniqueModelIn, @Nonnull String... variantsIn) {
        super(blockIn);
        setHasSubtypes(true);
        property = propertyIn;
        variants = variantsIn;
        hasUniqueModel = hasUniqueModelIn;
    }

    public ItemBlockMeta(@Nonnull Block block, @Nonnull PropertyBool property, boolean hasUniqueModelIn) {
        this(block, property.getName(), hasUniqueModelIn, "true", "false");
    }

    public ItemBlockMeta(@Nonnull Block block, @Nonnull PropertyInteger property, boolean hasUniqueModelIn) {
        this(block, property.getName(), hasUniqueModelIn, property.getAllowedValues().stream().map(String::valueOf).toArray(String[]::new));
    }

    public <T extends Enum<T> & IStringSerializable> ItemBlockMeta(@Nonnull Block block, @Nonnull PropertyEnum<T> property, boolean hasUniqueModelIn) {
        this(block, property.getName(), hasUniqueModelIn, property.getAllowedValues().stream().map(T::getName).toArray(String[]::new));
    }

    @Override
    public int getMetadata(int damage) { return MathHelper.clamp(damage, 0, variants.length); }

    @Override
    public int getMetadata(@Nonnull ItemStack stack) { return getMetadata(super.getMetadata(stack)); }

    @Nonnull
    @Override
    public String getTranslationKey(@Nonnull ItemStack stack) {
        return String.format("%s.%s.%s", super.getTranslationKey(stack), property, variants[stack.getMetadata()]);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if(isInCreativeTab(tab)) {
            for(int meta = 0; meta < variants.length; meta++) items.add(new ItemStack(this, 1, meta));
        }
    }

    @Override
    public void registerModels() {
        for(int meta = 0; meta < variants.length; meta++) {
            ModelLoader.setCustomModelResourceLocation(this, meta, new ModelResourceLocation(
                    delegate.name(), hasUniqueModel
                    ? String.format("inventory:%s=%s", property, variants[meta])
                    : String.format("%s=%s", property, variants[meta])));
        }
    }
}
