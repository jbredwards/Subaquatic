package git.jbredwards.subaquatic.mod.common.block;

import git.jbredwards.subaquatic.mod.client.item.ICustomModel;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.function.Supplier;

/**
 *
 * @author jbred
 *
 */
public abstract class AbstractBlockSlab extends BlockSlab implements ICustomModel
{
    @Nonnull
    private static final PropertyEnum<SlabType> SLAB_TYPE = PropertyEnum.create("slab_type", SlabType.class);

    @Nonnull
    public final Supplier<Item> itemDropped;
    public AbstractBlockSlab(@Nonnull Material materialIn, @Nonnull Supplier<Item> itemDroppedIn) {
        this(materialIn, materialIn.getMaterialMapColor(), itemDroppedIn);
    }

    public AbstractBlockSlab(@Nonnull Material materialIn, @Nonnull MapColor mapColorIn, @Nonnull Supplier<Item> itemDroppedIn) {
        super(materialIn, mapColorIn);
        itemDropped = itemDroppedIn;
        useNeighborBrightness = true;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return isDouble() ? new BlockStateContainer(this, SLAB_TYPE) : new BlockStateContainer(this, SLAB_TYPE, HALF);
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return isDouble() ? getDefaultState() : getDefaultState().withProperty(HALF, (meta & 8) == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
    }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) {
        return !isDouble() && state.getValue(HALF) == EnumBlockHalf.TOP ? 8 : 0;
    }

    @Nonnull
    @Override
    public Item getItemDropped(@Nonnull IBlockState state, @Nonnull Random rand, int fortune) { return itemDropped.get(); }

    @Nonnull
    @Override
    public String getTranslationKey(int meta) { return getTranslationKey(); }

    @Nonnull
    @Override
    public IProperty<?> getVariantProperty() { return SLAB_TYPE; }

    @Nonnull
    @Override
    public Comparable<?> getTypeForItem(@Nonnull ItemStack stack) { return SlabType.NORMAL; }

    @Override
    public void registerModels() {
        ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(SLAB_TYPE).build());
    }

    private enum SlabType implements IStringSerializable
    {
        NORMAL;

        @Nonnull
        @Override
        public String getName() { return "normal"; }
    }

    public static class Single extends AbstractBlockSlab
    {
        public Single(@Nonnull Material materialIn, @Nonnull Supplier<Item> itemDroppedIn) { super(materialIn, itemDroppedIn); }
        public Single(@Nonnull Material materialIn, @Nonnull MapColor mapColorIn, @Nonnull Supplier<Item> itemDroppedIn) {
            super(materialIn, mapColorIn, itemDroppedIn);
        }

        @Override
        public boolean isDouble() { return false; }
    }

    public static class Double extends AbstractBlockSlab
    {
        public Double(@Nonnull Material materialIn, @Nonnull Supplier<Item> itemDroppedIn) { super(materialIn, itemDroppedIn); }
        public Double(@Nonnull Material materialIn, @Nonnull MapColor mapColorIn, @Nonnull Supplier<Item> itemDroppedIn) {
            super(materialIn, mapColorIn, itemDroppedIn);
        }

        @Override
        public boolean isDouble() { return true; }
    }
}
