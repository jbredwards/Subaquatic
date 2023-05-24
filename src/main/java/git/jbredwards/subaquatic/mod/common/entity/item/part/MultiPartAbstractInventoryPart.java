package git.jbredwards.subaquatic.mod.common.entity.item.part;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.entity.item.AbstractBoatContainer;
import git.jbredwards.subaquatic.mod.common.message.CMessageOpenBoatInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;
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
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@Mod.EventBusSubscriber(modid = Subaquatic.MODID, value = Side.CLIENT)
public abstract class MultiPartAbstractInventoryPart extends MultiPartContainerPart implements ILockableContainer, UIProviderPart
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
        if(!world.isRemote && !parentBoat.isPassenger(player)) openUI(player);
        return true;
    }

    @Override
    public void setDead() {
        if(dropContentsWhenDead && !world.isRemote) InventoryHelper.dropInventoryItems(world, this, this);
        super.setDead();
    }

    //don't display player inventory if the player is riding this
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    static void cancelGui(@Nonnull GuiOpenEvent event) {
        if(event.getGui() instanceof GuiInventory && Minecraft.getMinecraft().player != null) {
            final Entity riding = Minecraft.getMinecraft().player.getRidingEntity();
            if(riding instanceof AbstractBoatContainer && ((AbstractBoatContainer)riding).containerPart instanceof UIProviderPart) {
                final CMessageOpenBoatInventory message = new CMessageOpenBoatInventory();
                message.isValid = true;

                Subaquatic.WRAPPER.sendToServer(message);
                event.setCanceled(true);
            }
        }
    }

    //=======================
    //GENERIC INVENTORY STUFF
    //=======================

    @Override
    public void openUI(@Nonnull EntityPlayer player) { player.displayGUIChest(getInventory(player)); }

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
