package git.jbredwards.subaquatic.mod.common.item;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.capability.IFishBucket;
import git.jbredwards.subaquatic.mod.common.capability.util.FishBucketData;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticEntities;
import git.jbredwards.subaquatic.mod.common.init.SubaquaticItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

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
    public ItemStack createIcon() { return new ItemStack(SubaquaticItems.NAUTILUS_SHELL); }

    @SideOnly(Side.CLIENT)
    @Override
    public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> items) {
        super.displayAllRelevantItems(items);

        //add all fish buckets to tab
        addFishBucket(items, new ItemStack(Items.WATER_BUCKET), SubaquaticEntities.COD);
        addFishBucket(items, new ItemStack(Items.WATER_BUCKET), SubaquaticEntities.SALMON);

        //dynamically add all entity eggs to tab
        SubaquaticEntities.INIT.forEach(entry -> {
            if(entry.getEgg() != null) {
                final ItemStack spawnEgg = new ItemStack(Items.SPAWN_EGG);
                ItemMonsterPlacer.applyEntityIdToItemStack(spawnEgg, entry.getEgg().spawnedID);
                items.add(spawnEgg);
            }
        });
    }

    static void addFishBucket(@Nonnull NonNullList<ItemStack> items, @Nonnull ItemStack stack, @Nonnull EntityEntry entity) {
        final IFishBucket cap = IFishBucket.get(stack);
        if(cap != null) {
            final FishBucketData data = new FishBucketData();
            data.entity = entity;

            final NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("id", entity.delegate.name().toString());
            data.fishNbt = nbt;

            cap.setData(data);
            items.add(stack);
        }
    }
}
