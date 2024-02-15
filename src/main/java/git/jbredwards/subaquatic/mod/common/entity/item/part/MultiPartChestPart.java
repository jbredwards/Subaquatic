/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.item.part;

import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author jbred
 *
 */
public class MultiPartChestPart extends MultiPartAbstractChestPart implements ILootContainer
{
    @Nonnull
    protected final NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
    protected ResourceLocation lootTable;
    protected long lootTableSeed;

    public MultiPartChestPart(@Nonnull IEntityMultiPart parent, @Nonnull String partName, float width, float height) {
        super(parent, partName, width, height);
    }

    @Nonnull
    @Override
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer playerIn) {
        tryFillWithLoot(playerIn);
        return new ContainerChest(playerInventory, this, playerIn);
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {
        super.openInventory(player);
        player.addStat(StatList.CHEST_OPENED);
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    protected ResourceLocation getChestTexture(boolean isChristmas) {
        return isChristmas ? TileEntityChestRenderer.TEXTURE_CHRISTMAS : TileEntityChestRenderer.TEXTURE_NORMAL;
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        if(compound.hasKey("LootTable", Constants.NBT.TAG_STRING)) {
            lootTable = new ResourceLocation(compound.getString("LootTable"));
            lootTableSeed = compound.getLong("LootTableSeed");
        }

        else ItemStackHelper.loadAllItems(compound, items);
    }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        if(lootTable != null) {
            compound.setString("LootTable", lootTable.toString());
            compound.setLong("LootTableSeed", lootTableSeed);
        }

        else ItemStackHelper.saveAllItems(compound, items);
    }

    @Override
    public int getSizeInventory() { return items.size(); }

    @Override
    public boolean isEmpty() {
        for(ItemStack stack : items) if(!stack.isEmpty()) return false;
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int index) {
        tryFillWithLoot(null);
        return items.get(index);
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int index, int count) {
        tryFillWithLoot(null);
        return ItemStackHelper.getAndSplit(items, index, count);
    }

    @Nonnull
    @Override
    public ItemStack removeStackFromSlot(int index) {
        tryFillWithLoot(null);
        return ItemStackHelper.getAndRemove(items, index);
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        lootTable = null;
        items.set(index, stack);
        if(stack.getCount() > getInventoryStackLimit()) stack.setCount(getInventoryStackLimit());
    }

    @Override
    public void clear() {
        lootTable = null;
        Collections.fill(items, ItemStack.EMPTY);
    }

    @Nullable
    @Override
    public ResourceLocation getLootTable() { return lootTable; }
    protected void tryFillWithLoot(@Nullable EntityPlayer player) {
        if(lootTable != null) {
            final LootTable table = world.getLootTableManager().getLootTableFromLocation(lootTable);
            lootTable = null;

            final Random seed = lootTableSeed != 0 ? new Random(lootTableSeed) : new Random();
            final LootContext.Builder builder = new LootContext.Builder((WorldServer)world);

            if(player != null) builder.withLuck(player.getLuck()).withPlayer(player); // Forge: add player to LootContext
            table.fillInventory(this, seed, builder.build());
        }
    }
}
