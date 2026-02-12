/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client.entity.renderer;

import git.jbredwards.subaquatic.mod.Subaquatic;
import git.jbredwards.subaquatic.mod.client.entity.model.ModelTurtle;
import git.jbredwards.subaquatic.mod.common.entity.living.EntityTurtle;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
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
public class RenderTurtle extends RenderLiving<EntityTurtle>
{
    @Nonnull
    public static final ResourceLocation TEXTURE = new ResourceLocation(Subaquatic.MODID, "textures/entity/turtle.png");
    public RenderTurtle(@Nonnull final RenderManager manager) {
        super(manager, new ModelTurtle(0), 0.35f);
    }

    @Override
    public void doRender(@Nonnull final EntityTurtle entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        if(entity.isChild()) shadowSize *= 0.5;
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull final EntityTurtle entity) {
        return TEXTURE;
    }
}
