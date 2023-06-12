package git.jbredwards.subaquatic.mod.common.entity.item.part;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.entity.item.EntityBoatFurnace;
import git.jbredwards.subaquatic.mod.common.message.SMessageFurnacePart;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;

/**
 *
 * @author jbred
 *
 */
@Mod.EventBusSubscriber(modid = Subaquatic.MODID)
public class MultiPartFurnacePart extends MultiPartAbstractInventoryPart implements ISidedInventory
{
    @Nonnull
    protected final NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    protected int burnTime, totalFuelBurnTime, cookTime, totalCookTime;

    @Nullable
    @SideOnly(Side.CLIENT)
    protected IBlockState stateForRender;
    public MultiPartFurnacePart(@Nonnull IEntityMultiPart parent, @Nonnull String partName, float width, float height) {
        super(parent, partName, width, height);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        final boolean prevIsBurning = burnTime > 0;
        if(prevIsBurning) --burnTime;

        //update furnace logic
        if(!world.isRemote) {
            final ItemStack fuel = getStackInSlot(1);
            if(burnTime > 0 || !fuel.isEmpty() && !getStackInSlot(0).isEmpty()) {
                //consume fuel
                if(burnTime == 0 && canSmeltItem()) {
                    burnTime = TileEntityFurnace.getItemBurnTime(fuel);
                    totalFuelBurnTime = burnTime;

                    if(burnTime > 0 && !fuel.isEmpty()) {
                        fuel.shrink(1);
                        if(fuel.isEmpty()) setInventorySlotContents(1, fuel.getItem().getContainerItem(fuel));
                    }

                    //sync new burnTime with client
                    Subaquatic.WRAPPER.sendToAllTracking(new SMessageFurnacePart(parentBoat, burnTime), parentBoat);
                }

                //smelt item
                if(burnTime > 0 && canSmeltItem()) {
                    if(++cookTime == totalCookTime) {
                        cookTime = 0;
                        totalCookTime = getTotalCookTime(getStackInSlot(0));

                        smeltItem();
                    }
                }

                //reset progress
                else cookTime = 0;
            }

            //IDK, but it was in vanilla's code... so... xd
            else if(burnTime == 0 && cookTime > 0) cookTime = MathHelper.clamp(cookTime - 2, 0, totalCookTime);

            //sync burnTime with client
            if(burnTime > 0 != prevIsBurning) Subaquatic.WRAPPER.sendToAllTracking(new SMessageFurnacePart(parentBoat, burnTime), parentBoat);
        }

        //furnace particles
        else if(burnTime > 0 && rand.nextFloat() < 0.1) spawnBurningParticles();
    }

    @SideOnly(Side.CLIENT)
    public void spawnBurningParticles() {
        if(rand.nextFloat() < 0.1) world.playSound(posX, posY, posZ, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, getSoundCategory(), 1, 1, false);
        //currently don't spawn these due to boat noWater mask rendering issues
        /*if(parentBoat.getPassengers().isEmpty()) {
            final Vec3d offset = new Vec3d(-0.5, rand.nextFloat() * 6 / 16, rand.nextFloat() * 0.6 - 0.3).rotateYaw(-parentBoat.rotationYaw * 0.0175f + ((float)Math.PI / 2));

            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX + offset.x, posY + offset.y, posZ + offset.z, 0, 0, 0);
            world.spawnParticle(EnumParticleTypes.FLAME, posX + offset.x, posY + offset.y, posZ + offset.z, 0, 0, 0);
        }*/

        //use these placeholder effects until I'm able to fix the issues mentioned above
        if(rand.nextInt(4) == 0) world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX, posY + 0.9, posZ, 0, 0, 0);
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX + rand.nextFloat() * 0.5 - 0.25, posY + 0.9, posZ + rand.nextFloat() * 0.5 - 0.25, 0, 0, 0);
    }

    public boolean canSmeltItem() {
        final ItemStack toSmelt = getStackInSlot(0);
        if(toSmelt.isEmpty()) return false;

        final ItemStack result = getSmeltingResult(toSmelt);
        if(result.isEmpty()) return false;

        final ItemStack smelted = getStackInSlot(2);
        return smelted.isEmpty() || smelted.getCount() + result.getCount() <= getInventoryStackLimit() && ItemHandlerHelper.canItemStacksStack(result, smelted);
    }

    public void smeltItem() {
        final ItemStack toSmelt = getStackInSlot(0);
        final ItemStack result = getSmeltingResult(toSmelt);
        final ItemStack smelted = getStackInSlot(2);

        if(smelted.isEmpty()) setInventorySlotContents(2, result.copy());
        else smelted.grow(result.getCount());

        //vanilla special case for wet sponges
        final ItemStack fuel = getStackInSlot(1);
        if(fuel.getCount() == 1 && fuel.getItem() == Items.BUCKET && toSmelt.getMetadata() == 1 && toSmelt.getItem() == Item.getItemFromBlock(Blocks.SPONGE))
            setInventorySlotContents(1, new ItemStack(Items.WATER_BUCKET));

        //consume items as they smelt
        toSmelt.shrink(1);
    }

    @Nonnull
    public ItemStack getSmeltingResult(@Nonnull ItemStack stack) { return FurnaceRecipes.instance().getSmeltingResult(stack); }
    public int getTotalCookTime(@Nonnull ItemStack stack) { return 200; }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderContainer(double x, double y, double z, float entityYaw, float partialTicks, boolean isChristmas) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.0625, 1.13, -0.0625);
        GlStateManager.scale(0.875, -0.875, -0.875);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        final int brightness = getBrightnessForRender();
        final float prevBrightnessX = OpenGlHelper.lastBrightnessX;
        final float prevBrightnessY = OpenGlHelper.lastBrightnessY;

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightness % 65536, brightness / 65536f);
        Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(getFurnaceToRender(isChristmas), brightness / 65536f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevBrightnessX, prevBrightnessY);

        GlStateManager.popMatrix();
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    protected IBlockState getFurnaceToRender(boolean isChristmas) {
        final Block blockForRender = (burnTime > 0 ? Blocks.LIT_FURNACE : Blocks.FURNACE);
        if(stateForRender == null || stateForRender.getBlock() != blockForRender)
            stateForRender = blockForRender.getDefaultState().withProperty(BlockFurnace.FACING, EnumFacing.WEST);

        return stateForRender;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getBrightnessForRender() {
        return burnTime > 0 ? Math.max(15728880, super.getBrightnessForRender()) : super.getBrightnessForRender();
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        ItemStackHelper.loadAllItems(compound, items);

        burnTime = compound.getInteger("BurnTime");
        cookTime = compound.getInteger("CookTime");
        totalCookTime = compound.getInteger("CookTimeTotal");
        totalFuelBurnTime = TileEntityFurnace.getItemBurnTime(getStackInSlot(1));
    }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        ItemStackHelper.saveAllItems(compound, items);

        compound.setInteger("BurnTime", burnTime);
        compound.setInteger("CookTime", cookTime);
        compound.setInteger("CookTimeTotal", totalCookTime);
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
    public ItemStack getStackInSlot(int index) { return items.get(index); }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int index, int count) { return ItemStackHelper.getAndSplit(items, index, count); }

    @Nonnull
    @Override
    public ItemStack removeStackFromSlot(int index) { return ItemStackHelper.getAndRemove(items, index); }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        final ItemStack oldStack = getStackInSlot(index);
        items.set(index, stack);

        if(stack.getCount() > getInventoryStackLimit()) stack.setCount(getInventoryStackLimit());
        if(index == 0 && (stack.isEmpty() || !stack.isItemEqual(oldStack) || !ItemStack.areItemStackTagsEqual(stack, oldStack))) {
            totalCookTime = getTotalCookTime(stack);
            cookTime = 0;
        }
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        if(index == 2) return false; //don't insert items in the output slot
        else if(index == 0) return true; //allow any items in the top input slot

        final ItemStack fuel = items.get(1);
        return TileEntityFurnace.isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack) && fuel.getItem() != Items.BUCKET;
    }

    @Nonnull
    @Override
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        switch(side) {
            case UP: return TileEntityFurnace.SLOTS_TOP;
            case DOWN: return TileEntityFurnace.SLOTS_BOTTOM;
            default: return TileEntityFurnace.SLOTS_SIDES;
        }
    }

    @Override
    public boolean canInsertItem(int index, @Nonnull ItemStack itemStackIn, @Nonnull EnumFacing direction) {
        return isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, @Nonnull ItemStack stack, @Nonnull EnumFacing direction) {
        //don't extract fuel from the fuel slot
        return direction != EnumFacing.DOWN || index != 1 || stack.getItem() == Items.WATER_BUCKET || stack.getItem() == Items.BUCKET;
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) { player.addStat(StatList.FURNACE_INTERACTION); }

    @Nonnull
    @Override
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer playerIn) {
        return new ContainerFurnace(playerInventory, this);
    }

    @Nonnull
    @Override
    public String getGuiID() { return "minecraft:furnace"; }

    @Override
    public int getField(int id) {
        switch(id) {
            case 0: return burnTime;
            case 1: return totalFuelBurnTime;
            case 2: return cookTime;
            case 3: return totalCookTime;
            default: return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch(id) {
            case 0:
                burnTime = value;
                break;
            case 1:
                totalFuelBurnTime = value;
                break;
            case 2:
                cookTime = value;
                break;
            case 3:
                totalCookTime = value;
        }
    }

    @Override
    public int getFieldCount() { return 4; }

    @Override
    public void clear() { Collections.fill(items, ItemStack.EMPTY); }

    @SubscribeEvent
    static void syncBurnTimeOnJoin(@Nonnull PlayerEvent.StartTracking event) {
        if(event.getTarget() instanceof EntityBoatFurnace) {
            Subaquatic.WRAPPER.sendTo(new SMessageFurnacePart(event.getTarget(),
                    ((MultiPartFurnacePart)((EntityBoatFurnace)event.getTarget()).containerPart).burnTime),
                    (EntityPlayerMP)event.getEntityPlayer());
        }
    }
}
