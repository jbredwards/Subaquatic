/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.item.part;

import git.jbredwards.subaquatic.mod.common.inventory.InventoryWrapper;
import net.minecraft.client.renderer.tileentity.TileEntityEnderChestRenderer;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class MultiPartEnderChestPart extends MultiPartAbstractChestPart
{
    public MultiPartEnderChestPart(@Nonnull IEntityMultiPart parent, @Nonnull String partName, float width, float height) {
        super(parent, partName, width, height);
    }

    @Nonnull
    @Override
    public IInventory getInventory(@Nonnull EntityPlayer player) {
        final InventoryEnderChest enderChest = player.getInventoryEnderChest();
        enderChest.setChestTileEntity(null);
        return new InventoryWrapper(enderChest, this);
    }

    @Nonnull
    @Override
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer playerIn) {
        return new ContainerChest(playerInventory, getInventory(playerIn), playerIn);
    }

    @Override
    public void playChestCloseSound() {
        playSound(SoundEvents.BLOCK_ENDERCHEST_CLOSE, 0.5f, rand.nextFloat() * 0.1f + 0.9f);
    }

    @Override
    public void playChestOpenSound() {
        playSound(SoundEvents.BLOCK_ENDERCHEST_OPEN, 0.5f, rand.nextFloat() * 0.1f + 0.9f);
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {
        super.openInventory(player);
        player.addStat(StatList.ENDERCHEST_OPENED);
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    protected ResourceLocation getChestTexture(boolean isChristmas) {
        return TileEntityEnderChestRenderer.ENDER_CHEST_TEXTURE;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        //spawn particles
        if(world.isRemote && rand.nextFloat() < 0.1) {
            for(int i = 0; i < 3; i++) {
                final int xOffset = rand.nextInt(2) * 2 - 1;
                final int zOffset = rand.nextInt(2) * 2 - 1;

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
}
