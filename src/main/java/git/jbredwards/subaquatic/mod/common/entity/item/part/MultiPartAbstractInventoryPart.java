package git.jbredwards.subaquatic.mod.common.entity.item.part;

import git.jbredwards.subaquatic.mod.Subaquatic;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public abstract class MultiPartAbstractInventoryPart extends MultiPartContainerPart implements ILockableContainer
{
    public boolean dropContentsWhenDead = true;
    public MultiPartAbstractInventoryPart(@Nonnull IEntityMultiPart parent, @Nonnull String partName, float width, float height) {
        super(parent, partName, width, height);
    }

    public static void registerFixer(@Nonnull DataFixer fixer) {
        final ItemStackDataLists walker = new ItemStackDataLists(Object.class, "Items");
        walker.key = new ResourceLocation(Subaquatic.MODID, "multipart_inventory");
        fixer.registerWalker(FixTypes.ENTITY, walker);
    }

    @Nonnull
    @Override
    public final String getFixType() { return "inventory"; }

    @Nonnull
    public IInventory getInventory(@Nonnull EntityPlayer player) { return this; }

    @Override
    public boolean processInitialInteract(@Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        if(!world.isRemote && !(player instanceof FakePlayer) && !parentBoat.isPassenger(player))
            player.displayGUIChest(getInventory(player));

        return true;
    }

    @Override
    public void setDead() {
        if(dropContentsWhenDead && !world.isRemote) InventoryHelper.dropInventoryItems(world, this, this);
        super.setDead();
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) { return !isDead && player.getDistanceSq(this) <= 64; }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) { return true; }

    @Override
    public void setDropItemsWhenDead(boolean dropWhenDead) { dropContentsWhenDead = dropWhenDead; }

    @Override
    public void onDimensionChanged() { dropContentsWhenDead = false; }

    @Override
    public int getInventoryStackLimit() { return 64; }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {}

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {}

    @Override
    public void markDirty() {}

    @Override
    public int getField(int id) { return 0; }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getFieldCount() { return 0; }

    @Override
    public boolean isLocked() { return false; }

    @Override
    public void setLockCode(@Nonnull LockCode code) {}

    @Nonnull
    @Override
    public LockCode getLockCode() { return LockCode.EMPTY_CODE; }
}
