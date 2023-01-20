package git.jbredwards.subaquatic.mod.common.entity.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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
    @Nonnull
    private static final DataParameter<Integer> NUM_PLAYERS_USING = EntityDataManager.createKey(MultiPartAbstractChestPart.class, DataSerializers.VARINT);
    public float lidAngle, prevLidAngle;

    @SideOnly(Side.CLIENT)
    protected static final ModelChest CHEST_MODEL = new ModelChest();
    public MultiPartAbstractChestPart(@Nonnull IEntityMultiPart parent, @Nonnull String partName, float width, float height) {
        super(parent, partName, width, height);
    }

    @Override
    protected void entityInit() { dataManager.register(NUM_PLAYERS_USING, 0); }
    public int getNumPlayersUsing() { return dataManager.get(NUM_PLAYERS_USING); }
    public void setNumPlayersUsing(int numPlayersUsing) { dataManager.set(NUM_PLAYERS_USING, numPlayersUsing); }

    @Override
    public void onUpdate() {
        super.onUpdate();
        prevLidAngle = lidAngle;

        final int numPlayersUsing = getNumPlayersUsing();
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
        GlStateManager.scale(-0.875, -0.875, -0.875);
        Minecraft.getMinecraft().renderEngine.bindTexture(getChestTexture());

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
