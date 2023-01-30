package git.jbredwards.subaquatic.mod.client.entity.renderer;

import git.jbredwards.subaquatic.mod.common.config.SubaquaticConfigHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderXPOrb;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public class RenderTranslucentXPOrb extends RenderXPOrb
{
    public RenderTranslucentXPOrb(@Nonnull RenderManager renderManagerIn) {
        super(renderManagerIn);
        shadowOpaque = 0.15f;
    }

    @Override
    public void doRender(@Nonnull EntityXPOrb entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if(SubaquaticConfigHandler.translucentXPOrbs) GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if(SubaquaticConfigHandler.translucentXPOrbs) GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
    }
}
