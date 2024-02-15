/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.common.entity.item;

import git.jbredwards.subaquatic.mod.common.init.SubaquaticItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
public class EntityMinecartWorkbench extends EntityMinecart implements IInteractionObject
{
    public EntityMinecartWorkbench(@Nonnull World worldIn) { super(worldIn); }
    public EntityMinecartWorkbench(@Nonnull World worldIn, double x, double y, double z) { super(worldIn, x, y, z); }

    @Override
    public boolean processInitialInteract(@Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        if(!super.processInitialInteract(player, hand) && !world.isRemote) {
            player.addStat(StatList.CRAFTING_TABLE_INTERACTION);
            player.displayGui(this);
        }

        return true;
    }

    @Override
    public void killMinecart(@Nonnull DamageSource source) {
        super.killMinecart(source);
        if(world.getGameRules().getBoolean("doEntityDrops"))
            dropItemWithOffset(Item.getItemFromBlock(Blocks.CRAFTING_TABLE), 1, 0);
    }

    @Override
    public boolean canBeRidden() { return false; }

    @Nonnull
    @Override
    public IBlockState getDefaultDisplayTile() { return Blocks.CRAFTING_TABLE.getDefaultState(); }

    @Nonnull
    @Override
    public ItemStack getCartItem() { return new ItemStack(SubaquaticItems.CRAFTING_TABLE_MINECART); }

    @Nullable
    @Override
    public Type getType() { return null; }

    @Nonnull
    @Override
    public String getGuiID() { return "minecraft:crafting_table"; }

    @Nonnull
    @Override
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer player) {
        return new ContainerWorkbench(playerInventory, world, getPosition()) {
            @Override
            public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
                return !isDead && playerIn.getDistanceSq(posX, posY, posZ) <= 64;
            }
        };
    }
}
