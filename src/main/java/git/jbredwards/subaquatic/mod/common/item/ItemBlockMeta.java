package git.jbredwards.subaquatic.mod.common.item;

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

    protected ItemBlockMeta(@Nonnull Block blockIn, @Nonnull String propertyIn, @Nonnull String... variantsIn) {
        super(blockIn);
        setHasSubtypes(true);
        property = propertyIn;
        variants = variantsIn;
    }

    public ItemBlockMeta(@Nonnull Block block, @Nonnull PropertyBool property) {
        this(block, property.getName(), "true", "false");
    }

    public ItemBlockMeta(@Nonnull Block block, @Nonnull PropertyInteger property) {
        this(block, property.getName(), property.getAllowedValues().stream().map(String::valueOf).toArray(String[]::new));
    }

    public <T extends Enum<T> & IStringSerializable> ItemBlockMeta(@Nonnull Block block, @Nonnull PropertyEnum<T> property) {
        this(block, property.getName(), property.getAllowedValues().stream().map(T::getName).toArray(String[]::new));
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
                    delegate.name(), String.format("%s=%s", property, variants[meta])));
        }
    }
}
