package git.jbredwards.subaquatic.mod.common.entity.item.part;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public class MultiPartWorkbenchPart extends MultiPartContainerPart implements IInteractionObject, UIProviderPart
{
    public MultiPartWorkbenchPart(@Nonnull IEntityMultiPart parent, @Nonnull String partName, float width, float height) {
        super(parent, partName, width, height);
    }

    @Override
    public boolean processInitialInteract(@Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        if(!world.isRemote && !parentBoat.isPassenger(player)) openUI(player);
        return true;
    }

    @Override
    public void openUI(@Nonnull EntityPlayer player) {
        player.addStat(StatList.CRAFTING_TABLE_INTERACTION);
        player.displayGui(this);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderContainer(double x, double y, double z, float entityYaw, float partialTicks, boolean isChristmas) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.0625, 1.13, -0.0625);
        GlStateManager.scale(0.875, -0.875, -0.875);

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(Blocks.CRAFTING_TABLE.getDefaultState(), 1);

        GlStateManager.popMatrix();
    }

    @Nonnull
    @Override
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer playerIn) {
        return new ContainerWorkbench(playerInventory, world, getPosition()) {
            @Override
            public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
                return !isDead && playerIn.getDistanceSq(posX, posY, posZ) <= 64;
            }
        };
    }

    @Nonnull
    @Override
    public String getGuiID() { return "minecraft:crafting_table"; }
}
