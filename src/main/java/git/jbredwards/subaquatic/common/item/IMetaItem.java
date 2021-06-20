package git.jbredwards.subaquatic.common.item;

import git.jbredwards.subaquatic.client.util.IModelProperties;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 *
 * @author jbred
 *
 */
public interface IMetaItem extends IModelProperties
{
    //returns the name of the property
    @Nonnull
    String getPropertyName();

    //returns the names of each enum in the property
    @Nonnull
    IStringSerializable[] getProperties();

    //sets up the metadata models
    @SideOnly(Side.CLIENT)
    @Override
    default void setupModel(@Nullable Item item) {
        if(item != null) {
            for(int meta = 0; meta < getProperties().length; meta++) {
                String variant = getPropertyName() + "=" + getProperties()[meta].getName();
                ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), variant));
            }
        }
    }

    //default implementation of the IMetaItem interface, where the block linked to this also implements IMetaItem
    class Impl extends ItemBlock implements IMetaItem
    {
        public Impl(Block block) {
            super(block);
        }

        @Nonnull
        @Override
        public String getPropertyName() {
            return ((IMetaItem)block).getPropertyName();
        }

        @Nonnull
        @Override
        public IStringSerializable[] getProperties() {
            return ((IMetaItem)block).getProperties();
        }
    }
}
