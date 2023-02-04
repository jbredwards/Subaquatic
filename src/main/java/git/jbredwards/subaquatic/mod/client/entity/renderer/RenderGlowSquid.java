package git.jbredwards.subaquatic.mod.client.entity.renderer;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.client.entity.renderer.layers.LayerGlowing;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSquid;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public class RenderGlowSquid extends RenderSquid
{
    @Nonnull
    protected static final ResourceLocation
            TEXTURE = new ResourceLocation(Subaquatic.MODID, "textures/entity/glow_squid/squid.png"),
            OVERLAY = new ResourceLocation(Subaquatic.MODID, "textures/entity/glow_squid/overlay.png");

    public RenderGlowSquid(@Nonnull RenderManager renderManagerIn) {
        super(renderManagerIn);
        addLayer(new LayerGlowing(OVERLAY, this, true));
    }

    @Nonnull
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntitySquid entity) { return TEXTURE; }
}
