/*
 * Copyright (c) 2026. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client.entity.model;

import git.jbredwards.subaquatic.mod.common.entity.living.EntityTurtle;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
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
public class ModelTurtle extends ModelQuadruped
{
    @Nonnull
    public final ModelRenderer eggBelly;
    public ModelTurtle(final float scale) {
        super(12, scale);
        textureWidth = 128;
        textureHeight = 64;
        head = new ModelRenderer(this, 3, 0);
        head.addBox(-3, -1, -3, 6, 5, 6, scale);
        head.setRotationPoint(0, 19, -10);

        body = new ModelRenderer(this);
        body.setTextureOffset(7, 37).addBox(-9.5f, 3, -10, 19, 20, 6, scale);
        body.setTextureOffset(31, 1).addBox(-5.5f, 3, -13, 11, 18, 3, scale);
        body.setRotationPoint(0, 11, -10);

        eggBelly = new ModelRenderer(this);
        eggBelly.setTextureOffset(70, 33).addBox(-4.5f, 3, -14, 9, 18, 1, scale);
        eggBelly.setRotationPoint(0, 11, -10);

        leg1 = new ModelRenderer(this, 1, 23);
        leg1.addBox(-2, 0, 0, 4, 1, 10, scale);
        leg1.setRotationPoint(-3.5f, 22, 11);

        leg2 = new ModelRenderer(this, 1, 12);
        leg2.addBox(-2, 0, 0, 4, 1, 10, scale);
        leg2.setRotationPoint(3.5f, 22, 11);

        leg3 = new ModelRenderer(this, 27, 30);
        leg3.addBox(-13, 0, -2, 13, 1, 5, scale);
        leg3.setRotationPoint(-5, 21, -4);

        leg4 = new ModelRenderer(this, 27, 24);
        leg4.addBox(0, 0, -2, 13, 1, 5, scale);
        leg4.setRotationPoint(5, 21, -4);
    }

    @Override
    public void render(@Nonnull final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        @Nonnull final EntityTurtle turtle = (EntityTurtle)entityIn;

        if(isChild) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.16666667f, 0.16666667f, 0.16666667f);
            GlStateManager.translate(0, 120 * scale, 0);
            head.render(scale);
            body.render(scale);
            leg1.render(scale);
            leg2.render(scale);
            leg3.render(scale);
            leg4.render(scale);
            GlStateManager.popMatrix();
        }
        
        else {
            GlStateManager.pushMatrix();
            if(turtle.hasEgg()) GlStateManager.translate(0, -0.08f, 0);

            head.render(scale);
            body.render(scale);
            GlStateManager.pushMatrix();
            leg1.render(scale);
            leg2.render(scale);
            GlStateManager.popMatrix();
            leg3.render(scale);
            leg4.render(scale);

            if(turtle.hasEgg()) eggBelly.render(scale);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, @Nonnull final Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        @Nonnull final EntityTurtle turtle = (EntityTurtle)entityIn;

        leg1.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f * 0.6f) * 0.5f * limbSwingAmount;
        leg2.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f * 0.6f + (float)Math.PI) * 0.5f * limbSwingAmount;
        leg3.rotateAngleZ = MathHelper.cos(limbSwing * 0.6662f * 0.6f + (float)Math.PI) * 0.5f * limbSwingAmount;
        leg4.rotateAngleZ = MathHelper.cos(limbSwing * 0.6662f * 0.6f) * 0.5f * limbSwingAmount;
        leg3.rotateAngleX = 0;
        leg4.rotateAngleX = 0;
        leg3.rotateAngleY = 0;
        leg4.rotateAngleY = 0;
        leg1.rotateAngleY = 0;
        leg2.rotateAngleY = 0;

        eggBelly.rotateAngleX = ((float)Math.PI / 2F);
        if(!turtle.isInWater() && turtle.onGround) {
            float f = turtle.isDiggingSand() ? 4 : 1;
            float f1 = turtle.isDiggingSand() ? 2 : 1;

            leg3.rotateAngleY = MathHelper.cos(f * limbSwing * 5 + (float)Math.PI) * 8 * limbSwingAmount * f1;
            leg3.rotateAngleZ = 0;
            leg4.rotateAngleY = MathHelper.cos(f * limbSwing * 5) * 8 * limbSwingAmount * f1;
            leg4.rotateAngleZ = 0;
            leg1.rotateAngleY = MathHelper.cos(limbSwing * 5 + (float)Math.PI) * 3 * limbSwingAmount;
            leg1.rotateAngleX = 0;
            leg2.rotateAngleY = MathHelper.cos(limbSwing * 5) * 3 * limbSwingAmount;
            leg2.rotateAngleX = 0;
        }
    }
}
