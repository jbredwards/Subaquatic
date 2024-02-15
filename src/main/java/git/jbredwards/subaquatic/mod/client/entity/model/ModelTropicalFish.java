/*
 * Copyright (c) 2024. jbredwards
 * All rights reserved.
 */

package git.jbredwards.subaquatic.mod.client.entity.model;

import net.minecraft.client.model.ModelRenderer;
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
public abstract class ModelTropicalFish extends ModelFishBase
{
    @Nonnull
    protected ModelRenderer tail;

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, @Nonnull Entity entityIn) {
        final float magnitude = entityIn.isInWater() ? 1 : 1.5f;
        tail.rotateAngleY = -magnitude * 0.45f * MathHelper.sin(0.6f * ageInTicks);
    }

    public static class A extends ModelTropicalFish
    {
        public A(float scale) {
            final ModelRenderer body = new ModelRenderer(this, 0, 0);
            body.addBox(-1, -1.5f, -3, 2, 3, 6, scale);
            body.setRotationPoint(0, 22, 0);

            final ModelRenderer rightFin = new ModelRenderer(this, 2, 16);
            rightFin.addBox(-2, -1, 0, 2, 2, 0, scale);
            rightFin.setRotationPoint(-1, 22.5f, 0);
            rightFin.rotateAngleY = ((float)Math.PI / 4);

            final ModelRenderer leftFin = new ModelRenderer(this, 2, 12);
            leftFin.addBox(0, -1, 0, 2, 2, 0, scale);
            leftFin.setRotationPoint(1, 22.5f, 0);
            leftFin.rotateAngleY = (-(float)Math.PI / 4);

            final ModelRenderer topFin = new ModelRenderer(this, 10, -5);
            topFin.addBox(0, -3, 0, 0, 3, 6, scale);
            topFin.setRotationPoint(0, 20.5f, -3);

            tail = new ModelRenderer(this, 22, -6);
            tail.addBox(0, -1.5f, 0, 0, 3, 6, scale);
            tail.setRotationPoint(0, 22, 3);
        }
    }

    public static class B extends ModelTropicalFish
    {
        public B(float scale) {
            final ModelRenderer body = new ModelRenderer(this, 0, 20);
            body.addBox(-1, -3, -3, 2, 6, 6, scale);
            body.setRotationPoint(0, 19, 0);

            final ModelRenderer rightFin = new ModelRenderer(this, 2, 16);
            rightFin.addBox(-2, 0, 0, 2, 2, 0, scale);
            rightFin.setRotationPoint(-1, 20, 0);
            rightFin.rotateAngleY = ((float)Math.PI / 4);

            final ModelRenderer leftFin = new ModelRenderer(this, 2, 12);
            leftFin.addBox(0, 0, 0, 2, 2, 0, scale);
            leftFin.setRotationPoint(1, 20, 0);
            leftFin.rotateAngleY = (-(float)Math.PI / 4);

            final ModelRenderer topFin = new ModelRenderer(this, 20, 11);
            topFin.addBox(0, -4, 0, 0, 4, 6, scale);
            topFin.setRotationPoint(0, 16, -3);

            final ModelRenderer bottomFin = new ModelRenderer(this, 20, 21);
            bottomFin.addBox(0, 0, 0, 0, 4, 6, scale);
            bottomFin.setRotationPoint(0, 22, -3);

            tail = new ModelRenderer(this, 21, 16);
            tail.addBox(0, -3, 0, 0, 6, 5, scale);
            tail.setRotationPoint(0, 19, 3);
        }
    }
}
