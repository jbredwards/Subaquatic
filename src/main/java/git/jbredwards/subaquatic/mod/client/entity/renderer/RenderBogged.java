/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client.entity.renderer;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.client.entity.layer.LayerBoggedClothing;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author jbred
 *
 */
@SideOnly(Side.CLIENT)
public class RenderBogged extends RenderSkeleton
{
    @Nonnull
    protected static final ResourceLocation TEXTURE = new ResourceLocation(Subaquatic.MODID, "textures/entity/bogged/base.png");
    public RenderBogged(@Nonnull final RenderManager renderManagerIn) {
        super(renderManagerIn);
        addLayer(new LayerBoggedClothing(this));
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull final AbstractSkeleton entity) { return TEXTURE; }
}
