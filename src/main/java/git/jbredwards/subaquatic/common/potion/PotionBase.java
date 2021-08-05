package git.jbredwards.subaquatic.common.potion;

import git.jbredwards.subaquatic.util.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author jbred
 *
 */
public class PotionBase extends Potion
{
    public PotionBase(boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
    }

    @Override
    public Potion setIconIndex(int x, int y) { return super.setIconIndex(x, y); }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasStatusIcon() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(Constants.MODID, "textures/gui/effects.png"));
        return true;
    }
}
