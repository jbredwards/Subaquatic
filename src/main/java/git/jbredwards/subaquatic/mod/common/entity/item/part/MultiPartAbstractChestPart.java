package git.jbredwards.subaquatic.mod.common.entity.item.part;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.message.MessageAbstractChestPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public abstract class MultiPartAbstractChestPart extends MultiPartAbstractInventoryPart
{
    public float lidAngle, prevLidAngle;
    protected int numPlayersUsing;

    @SideOnly(Side.CLIENT)
    protected static final ModelChest CHEST_MODEL = new ModelChest();
    public MultiPartAbstractChestPart(@Nonnull IEntityMultiPart parent, @Nonnull String partName, float width, float height) {
        super(parent, partName, width, height);
    }

    public int getNumPlayersUsing() { return numPlayersUsing; }
    public void setNumPlayersUsing(int numPlayersUsingIn) {
        if(!world.isRemote) Subaquatic.WRAPPER.sendToAllTracking(new MessageAbstractChestPart(numPlayersUsingIn, parentBoat.getEntityId()), parentBoat);
        numPlayersUsing = numPlayersUsingIn;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        prevLidAngle = lidAngle;

        if(numPlayersUsing > 0 && lidAngle == 0) playChestOpenSound();
        if(numPlayersUsing == 0 && lidAngle > 0 || numPlayersUsing > 0 && lidAngle < 1) {
            if(numPlayersUsing > 0) lidAngle += 0.1;
            else lidAngle -= 0.1;

            if(lidAngle < 0.5 && prevLidAngle >= 0.5) playChestCloseSound();
            lidAngle = MathHelper.clamp(lidAngle, 0, 1);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderContainer(double x, double y, double z, float entityYaw, float partialTicks) {
        Minecraft.getMinecraft().renderEngine.bindTexture(getChestTexture());
        GlStateManager.translate(-0.1, -0.1, -0.1);

        float animatedLidAngle = 1 - (prevLidAngle + (lidAngle - prevLidAngle) * partialTicks);
        animatedLidAngle = 1 - animatedLidAngle * animatedLidAngle * animatedLidAngle;

        CHEST_MODEL.chestLid.rotateAngleX = -(animatedLidAngle * (float)Math.PI / 2);
        CHEST_MODEL.renderAll();
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    protected abstract ResourceLocation getChestTexture();

    public void playChestCloseSound() { playSound(SoundEvents.BLOCK_CHEST_CLOSE, 0.5f, rand.nextFloat() * 0.1f + 0.9f); }
    public void playChestOpenSound() { playSound(SoundEvents.BLOCK_CHEST_OPEN, 0.5f, rand.nextFloat() * 0.1f + 0.9f); }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {
        final int numPlayersUsing = getNumPlayersUsing();
        if(numPlayersUsing > 0) setNumPlayersUsing(numPlayersUsing - 1);
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) { setNumPlayersUsing(getNumPlayersUsing() + 1);  }

    @Nonnull
    @Override
    public String getGuiID() { return "minecraft:chest"; }
}
