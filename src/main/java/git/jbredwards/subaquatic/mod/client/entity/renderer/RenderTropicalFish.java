/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client.entity.renderer;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.client.entity.layer.LayerTropicalFishPattern;
import git.jbredwards.subaquatic.mod.client.entity.model.ModelTropicalFish;
import git.jbredwards.subaquatic.mod.common.entity.living.EntityTropicalFish;
import git.jbredwards.subaquatic.mod.common.entity.util.TropicalFishData;
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
public class RenderTropicalFish extends RenderLiving<EntityTropicalFish>
{
    @Nonnull
    protected final ModelTropicalFish modelA = new ModelTropicalFish.A(0), modelB = new ModelTropicalFish.B(0);
    public RenderTropicalFish(@Nonnull RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelTropicalFish.A(0), 0.15f);
        addLayer(new LayerTropicalFishPattern(this));
    }

    @Nonnull
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityTropicalFish entity) {
        return new ResourceLocation(Subaquatic.MODID, "textures/entity/fish/tropical_" + entity.getVariant().primaryShape + ".png");
    }

    @Override
    public void doRender(@Nonnull EntityTropicalFish entity, double x, double y, double z, float entityYaw, float partialTicks) {
        final TropicalFishData variant = entity.getVariant();
        final float[] colors = variant.primaryColor.getColorComponentValues();
        mainModel = (variant.primaryShape & 1) == 0 ? modelA : modelB;

        GlStateManager.color(colors[0], colors[1], colors[2]);
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected void applyRotations(@Nonnull EntityTropicalFish entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
        GlStateManager.rotate(4.3f * MathHelper.sin(0.6f * ageInTicks), 0, 1, 0);
        if(!entityLiving.isInWater()) {
            GlStateManager.translate(0.2, 0.1, 0);
            GlStateManager.rotate(90, 0, 0, 1);
        }
    }
}
