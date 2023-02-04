package git.jbredwards.subaquatic.mod.common.entity.item;

import git.jbredwards.subaquatic.mod.common.init.SubaquaticItems;
import git.jbredwards.subaquatic.mod.common.inventory.InventoryWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public class EntityMinecartEnderChest extends EntityMinecart implements ILockableContainer
{
    public EntityMinecartEnderChest(@Nonnull World worldIn) { super(worldIn); }
    public EntityMinecartEnderChest(@Nonnull World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public boolean processInitialInteract(@Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        if(!super.processInitialInteract(player, hand) && !world.isRemote) player.displayGUIChest(getInventory(player));
        return true;
    }

    @Override
    public void killMinecart(@Nonnull DamageSource source) {
        if(world.getGameRules().getBoolean("doEntityDrops")) dropItem(Item.getItemFromBlock(Blocks.ENDER_CHEST), 1);
        super.killMinecart(source);
    }

    @Override
    public boolean canBeRidden() { return false; }

    @Override
    public int getDefaultDisplayTileOffset() { return 8; }

    @Nonnull
    @Override
    public IBlockState getDefaultDisplayTile() { return Blocks.ENDER_CHEST.getDefaultState(); }

    @Nonnull
    @Override
    public ItemStack getCartItem() { return new ItemStack(SubaquaticItems.ENDER_CHEST_MINECART); }

    @Nullable
    @Override
    public Type getType() { return null; }

    @Nonnull
    @Override
    public String getGuiID() { return "minecraft:chest"; }

    @Nonnull
    @Override
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer playerIn) {
        return new ContainerChest(playerInventory, getInventory(playerIn), playerIn);
    }

    @Nonnull
    public IInventory getInventory(@Nonnull EntityPlayer player) {
        final InventoryEnderChest enderChest = player.getInventoryEnderChest();
        enderChest.setChestTileEntity(null);
        return new InventoryWrapper(enderChest, this);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        //spawn particles
        if(world.isRemote && rand.nextFloat() < 0.1) {
            for(int i = 0; i < 3; i++) {
                final double xOffset = (rand.nextInt(2) * 2 - 1) * 0.8;
                final double zOffset = (rand.nextInt(2) * 2 - 1) * 0.8;

                final double x = posX + 0.25 * xOffset;
                final double y = posY + rand.nextFloat();
                final double z = posZ + 0.25 * zOffset;
                final double xSpeed = rand.nextFloat() * xOffset;
                final double ySpeed = (rand.nextFloat() - 0.5) * 0.125;
                final double zSpeed = rand.nextFloat() * zOffset;

                world.spawnParticle(EnumParticleTypes.PORTAL, x, y, z, xSpeed, ySpeed, zSpeed);
            }
        }
    }

    //=================================================
    //INVENTORY IS LOCAL TO THE PLAYER, NOT THIS ENTITY
    //=================================================

    @Override
    public int getSizeInventory() { return 0; }

    @Override
    public boolean isEmpty() { return true; }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int index) { return ItemStack.EMPTY; }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int index, int count) { return ItemStack.EMPTY; }

    @Nonnull
    @Override
    public ItemStack removeStackFromSlot(int index) { return ItemStack.EMPTY; }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {}

    @Override
    public void clear() {}

    //=======================
    //GENERIC INVENTORY STUFF
    //=======================

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) { return !isDead && player.getDistanceSq(this) <= 64; }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) { return true; }

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
