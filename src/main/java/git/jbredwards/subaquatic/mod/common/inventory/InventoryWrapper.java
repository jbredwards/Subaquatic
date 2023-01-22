package git.jbredwards.subaquatic.mod.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class InventoryWrapper implements IInventory
{
    @Nonnull
    protected final IInventory inventoryHandler, actionHandler;
    public InventoryWrapper(@Nonnull IInventory inventoryHandlerIn, @Nonnull IInventory actionHandlerIn) {
        inventoryHandler = inventoryHandlerIn;
        actionHandler = actionHandlerIn;
    }

    @Override
    public int getSizeInventory() { return inventoryHandler.getSizeInventory(); }

    @Override
    public boolean isEmpty() { return inventoryHandler.isEmpty(); }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int index) { return inventoryHandler.getStackInSlot(index); }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int index, int count) { return inventoryHandler.decrStackSize(index, count); }

    @Nonnull
    @Override
    public ItemStack removeStackFromSlot(int index) { return inventoryHandler.removeStackFromSlot(index); }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) { inventoryHandler.setInventorySlotContents(index, stack); }

    @Override
    public int getInventoryStackLimit() { return inventoryHandler.getInventoryStackLimit(); }

    @Override
    public void markDirty() { inventoryHandler.markDirty(); }

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) { return actionHandler.isUsableByPlayer(player); }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) { actionHandler.openInventory(player); }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) { actionHandler.closeInventory(player); }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) { return inventoryHandler.isItemValidForSlot(index, stack); }

    @Override
    public int getField(int id) { return inventoryHandler.getField(id); }

    @Override
    public void setField(int id, int value) { inventoryHandler.setField(id, value); }

    @Override
    public int getFieldCount() { return inventoryHandler.getFieldCount(); }

    @Override
    public void clear() { inventoryHandler.clear(); }

    @Nonnull
    @Override
    public String getName() { return inventoryHandler.getName(); }

    @Override
    public boolean hasCustomName() { return inventoryHandler.hasCustomName(); }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() { return inventoryHandler.getDisplayName(); }
}
