/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client.entity.renderer;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.client.entity.model.ModelSalmon;
import git.jbredwards.subaquatic.mod.common.entity.living.AbstractFish;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
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
@SideOnly(Side.CLIENT)
public class RenderSalmon extends RenderLiving<AbstractFish>
{
    @Nonnull
    private static final ResourceLocation TEXTURE = new ResourceLocation(Subaquatic.MODID, "textures/entity/fish/salmon.png");
    public RenderSalmon(@Nonnull RenderManager renderManagerIn) { super(renderManagerIn, new ModelSalmon(), 0.2f); }

    @Nonnull
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull AbstractFish entity) { return TEXTURE; }

    @Override
    protected void applyRotations(@Nonnull AbstractFish entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
        float magnitude = 1;
        float frequency = 1;

        if(!entityLiving.isInWater()) {
            magnitude = 1.3f;
            frequency = 1.7f;
        }

        GlStateManager.rotate(magnitude * 4.3f * MathHelper.sin(frequency * 0.6f * ageInTicks), 0, 1, 0);
        GlStateManager.translate(0, 0, -0.4);

        if(!entityLiving.isInWater()) {
            GlStateManager.translate(0.2, 0.1, 0);
            GlStateManager.rotate(90, 0, 0, 1);
        }
    }
}
