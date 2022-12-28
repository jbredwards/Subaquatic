package git.jbredwards.subaquatic.mod.common.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Basic implementation of a potion that can use custom textures
 * @author jbred
 *
 */
public class PotionBase extends Potion
{
    public PotionBase(boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderInventoryEffect(int x, int y, @Nonnull PotionEffect effect, @Nonnull Minecraft mc) {
        mc.getTextureManager().bindTexture(getTexture());
        Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderHUDEffect(int x, int y, @Nonnull PotionEffect effect, @Nonnull Minecraft mc, float alpha) {
        mc.getTextureManager().bindTexture(getTexture());
        Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
    }

    @Nonnull
    protected ResourceLocation getTexture() {
        return new ResourceLocation(delegate.name().getPath(), String.format("textures/potions/%s.png", delegate.name().getNamespace()));
    }
}
