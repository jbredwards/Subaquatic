package git.jbredwards.subaquatic.mod.client.entity.renderer;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.entity.living.AbstractFish;
import net.minecraft.client.renderer.entity.RenderManager;
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
public class RenderFish extends RenderCod
{
    @Nonnull
    private static final ResourceLocation TEXTURE = new ResourceLocation(Subaquatic.MODID, "textures/entity/fish/fish.png");
    public RenderFish(@Nonnull RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Nonnull
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull AbstractFish entity) { return TEXTURE; }
}
