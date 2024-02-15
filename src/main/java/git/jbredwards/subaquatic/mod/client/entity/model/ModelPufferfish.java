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
public abstract class ModelPufferfish extends ModelFishBase
{
    @Nonnull
    protected ModelRenderer rightFin, leftFin;

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, @Nonnull Entity entityIn) {
        rightFin.rotateAngleZ = -0.2f + 0.4f * MathHelper.sin(ageInTicks * 0.2f);
        leftFin.rotateAngleZ = 0.2f - 0.4f * MathHelper.sin(ageInTicks * 0.2f);
    }

    public static class Small extends ModelPufferfish
    {
        public Small() {
            final ModelRenderer body = new ModelRenderer(this, 0, 27);
            body.addBox(-1.5f, -2, -1.5f, 3, 2, 3);
            body.setRotationPoint(0, 23, 0);

            final ModelRenderer rightEye = new ModelRenderer(this, 24, 6);
            rightEye.addBox(-1.5f, 0, -1.5f, 1, 1, 1);
            rightEye.setRotationPoint(0, 20, 0);

            final ModelRenderer leftEye = new ModelRenderer(this, 28, 6);
            leftEye.addBox(0.5f, 0, -1.5f, 1, 1, 1);
            leftEye.setRotationPoint(0, 20, 0);

            final ModelRenderer backFin = new ModelRenderer(this, -3, 0);
            backFin.addBox(-1.5f, 0, 0, 3, 0, 3);
            backFin.setRotationPoint(0, 22, 1.5f);

            rightFin = new ModelRenderer(this, 25, 0);
            rightFin.addBox(-1, 0, 0, 1, 0, 2);
            rightFin.setRotationPoint(-1.5f, 22, -1.5f);

            leftFin = new ModelRenderer(this, 25, 0);
            leftFin.addBox(0, 0, 0, 1, 0, 2);
            leftFin.setRotationPoint(1.5f, 22, -1.5f);
        }
    }

    public static class Medium extends ModelPufferfish
    {
        public Medium() {
            final ModelRenderer body = new ModelRenderer(this, 12, 22);
            body.addBox(-2.5f, -5, -2.5f, 5, 5, 5);
            body.setRotationPoint(0, 22, 0);

            final ModelRenderer topFrontFin = new ModelRenderer(this, 15, 16);
            topFrontFin.addBox(-2.5f, -1, 0, 5, 1, 1);
            topFrontFin.setRotationPoint(0, 17, -2.5f);
            topFrontFin.rotateAngleX = ((float)Math.PI / 4);

            final ModelRenderer topBackFin = new ModelRenderer(this, 10, 16);
            topBackFin.addBox(-2.5f, -1, -1, 5, 1, 1);
            topBackFin.setRotationPoint(0, 17, 2.5f);
            topBackFin.rotateAngleX = (-(float)Math.PI / 4f);

            final ModelRenderer rightFrontFin = new ModelRenderer(this, 8, 16);
            rightFrontFin.addBox(-1, -5, 0, 1, 5, 1);
            rightFrontFin.setRotationPoint(-2.5f, 22, -2.5f);
            rightFrontFin.rotateAngleY = (-(float)Math.PI / 4);

            final ModelRenderer rightBackFin = new ModelRenderer(this, 8, 16);
            rightBackFin.addBox(-1, -5, 0, 1, 5, 1);
            rightBackFin.setRotationPoint(-2.5f, 22, 2.5f);
            rightBackFin.rotateAngleY = ((float)Math.PI / 4);

            final ModelRenderer leftBackFin = new ModelRenderer(this, 4, 16);
            leftBackFin.addBox(0, -5, 0, 1, 5, 1);
            leftBackFin.setRotationPoint(2.5f, 22, 2.5f);
            leftBackFin.rotateAngleY = (-(float)Math.PI / 4);

            final ModelRenderer leftFrontFin = new ModelRenderer(this, 0, 16);
            leftFrontFin.addBox(0, -5, 0, 1, 5, 1);
            leftFrontFin.setRotationPoint(2.5f, 22, -2.5f);
            leftFrontFin.rotateAngleY = ((float)Math.PI / 4);

            final ModelRenderer bottomBackFin = new ModelRenderer(this, 8, 22);
            bottomBackFin.addBox(0, 0, 0, 1, 1, 1);
            bottomBackFin.setRotationPoint(0.5f, 22, 2.5f);
            bottomBackFin.rotateAngleX = ((float)Math.PI / 4);

            final ModelRenderer bottomFrontFin = new ModelRenderer(this, 17, 21);
            bottomFrontFin.addBox(-2.5f, 0, 0, 5, 1, 1);
            bottomFrontFin.setRotationPoint(0, 22, -2.5f);
            bottomFrontFin.rotateAngleX = (-(float)Math.PI / 4);

            rightFin = new ModelRenderer(this, 24, 0);
            rightFin.addBox(-2, 0, 0, 2, 0, 2);
            rightFin.setRotationPoint(-2.5f, 17, -1.5f);

            leftFin = new ModelRenderer(this, 24, 3);
            leftFin.addBox(0, 0, 0, 2, 0, 2);
            leftFin.setRotationPoint(2.5f, 17, -1.5f);
        }
    }

    public static class Large extends ModelPufferfish
    {
        public Large() {
            final ModelRenderer body = new ModelRenderer(this, 0, 0);
            body.addBox(-4, -8, -4, 8, 8, 8);
            body.setRotationPoint(0, 22, 0);

            final ModelRenderer topFrontFin = new ModelRenderer(this, 15, 17);
            topFrontFin.addBox(-4, -1, 0, 8, 1, 0);
            topFrontFin.setRotationPoint(0, 14, -4);
            topFrontFin.rotateAngleX = ((float)Math.PI / 4);

            final ModelRenderer topMiddleFin = new ModelRenderer(this, 14, 16);
            topMiddleFin.addBox(-4, -1, 0, 8, 1, 1);
            topMiddleFin.setRotationPoint(0, 14, 0);

            final ModelRenderer topBackFin = new ModelRenderer(this, 23, 18);
            topBackFin.addBox(-4, -1, 0, 8, 1, 0);
            topBackFin.setRotationPoint(0, 14, 4);
            topBackFin.rotateAngleX = (-(float)Math.PI / 4);

            final ModelRenderer rightFrontFin = new ModelRenderer(this, 5, 17);
            rightFrontFin.addBox(-1, -8, 0, 1, 8, 0);
            rightFrontFin.setRotationPoint(-4, 22, -4);
            rightFrontFin.rotateAngleY = (-(float)Math.PI / 4);

            final ModelRenderer leftFrontFin = new ModelRenderer(this, 1, 17);
            leftFrontFin.addBox(0, -8, 0, 1, 8, 0);
            leftFrontFin.setRotationPoint(4, 22, -4);
            leftFrontFin.rotateAngleY = ((float)Math.PI / 4);

            final ModelRenderer bottomFrontFin = new ModelRenderer(this, 15, 20);
            bottomFrontFin.addBox(-4, 0, 0, 8, 1, 0);
            bottomFrontFin.setRotationPoint(0, 22, -4);
            bottomFrontFin.rotateAngleX = (-(float)Math.PI / 4);

            final ModelRenderer bottomMiddleFin = new ModelRenderer(this, 15, 20);
            bottomMiddleFin.addBox(-4, 0, 0, 8, 1, 0);
            bottomMiddleFin.setRotationPoint(0, 22, 0);

            final ModelRenderer bottomBackFin = new ModelRenderer(this, 15, 20);
            bottomBackFin.addBox(-4, 0, 0, 8, 1, 0);
            bottomBackFin.setRotationPoint(0, 22, 4);
            bottomBackFin.rotateAngleX = ((float)Math.PI / 4);

            final ModelRenderer rightBackFin = new ModelRenderer(this, 9, 17);
            rightBackFin.addBox(-1, -8, 0, 1, 8, 0);
            rightBackFin.setRotationPoint(-4, 22, 4);
            rightBackFin.rotateAngleY = ((float)Math.PI / 4);

            final ModelRenderer leftBackFin = new ModelRenderer(this, 9, 17);
            leftBackFin.addBox(0, -8, 0, 1, 8, 0);
            leftBackFin.setRotationPoint(4, 22, 4);
            leftBackFin.rotateAngleY = (-(float)Math.PI / 4);

            rightFin = new ModelRenderer(this, 24, 0);
            rightFin.addBox(-2, 0, -1, 2, 1, 2);
            rightFin.setRotationPoint(-4, 15, -2);

            leftFin = new ModelRenderer(this, 24, 3);
            leftFin.addBox(0, 0, -1, 2, 1, 2);
            leftFin.setRotationPoint(4, 15, -2);
        }
    }
}
