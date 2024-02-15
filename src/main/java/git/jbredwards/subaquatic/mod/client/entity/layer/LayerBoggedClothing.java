/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client.entity.layer;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.common.entity.living.EntityBogged;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
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
public class LayerBoggedClothing implements LayerRenderer<EntityBogged>
{
    @Nonnull protected static final ResourceLocation TEXTURE = new ResourceLocation(Subaquatic.MODID, "textures/entity/bogged/overlay.png");
    @Nonnull protected final ModelSkeleton model = new ModelSkeleton(0.25f, true);

    @Nonnull protected final RenderLivingBase<?> renderer;
    public LayerBoggedClothing(@Nonnull final RenderLivingBase<?> rendererIn) { renderer = rendererIn; }

    @Override
    public void doRenderLayer(@Nonnull final EntityBogged entity, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        model.setModelAttributes(renderer.getMainModel());
        model.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
        GlStateManager.color(1, 1, 1);

        renderer.bindTexture(TEXTURE);
        model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public boolean shouldCombineTextures() { return true; }
}
