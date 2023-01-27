package git.jbredwards.subaquatic.mod.client.entity.model;

import net.minecraft.client.model.ModelBase;
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
public class ModelSalmon extends ModelBase
{
    @Nonnull
    protected final ModelRenderer bodyFront, bodyBack, head, topFrontFin, topBackFin, backFin, rightFin, leftFin;
    public ModelSalmon() {
        textureWidth = 32;
        textureHeight = 32;

        bodyFront = new ModelRenderer(this, 0, 0);
        bodyFront.addBox(-1.5f, -2.5f, 0, 3, 5, 8);
        bodyFront.setRotationPoint(0, 20, 0);

        bodyBack = new ModelRenderer(this, 0, 13);
        bodyBack.addBox(-1.5f, -2.5f, 0, 3, 5, 8);
        bodyBack.setRotationPoint(0, 20, 8);

        head = new ModelRenderer(this, 22, 0);
        head.addBox(-1, -2, -3, 2, 4, 3);
        head.setRotationPoint(0, 20, 0);

        backFin = new ModelRenderer(this, 20, 10);
        backFin.addBox(0, -2.5f, 0, 0, 5, 6);
        backFin.setRotationPoint(0, 0, 8);

        topFrontFin = new ModelRenderer(this, 2, 1);
        topFrontFin.addBox(0, 0, 0, 0, 2, 3);
        topFrontFin.setRotationPoint(0, -4.5f, 5);

        topBackFin = new ModelRenderer(this, 0, 2);
        topBackFin.addBox(0, 0, 0, 0, 2, 4);
        topBackFin.setRotationPoint(0, -4.5f, -1);

        rightFin = new ModelRenderer(this, -4, 0);
        rightFin.addBox(-2, 0, 0, 2, 0, 2);
        rightFin.setRotationPoint(-1.5f, 21.5f, 0);
        rightFin.rotateAngleZ = (float)(-Math.PI / 4);

        leftFin = new ModelRenderer(this, -4, 0);
        leftFin.addBox(0, 0, 0, 2, 0, 2);
        leftFin.setRotationPoint(1.5f, 21.5f, 0);
        leftFin.rotateAngleZ = (float)(Math.PI / 4);

        bodyFront.addChild(topFrontFin);
        bodyBack.addChild(topBackFin);
        bodyBack.addChild(backFin);
    }

    @Override
    public void render(@Nonnull Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        bodyFront.render(scale);
        bodyBack.render(scale);
        head.render(scale);

        rightFin.render(scale);
        leftFin.render(scale);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, @Nonnull Entity entityIn) {
        float magnitude = 1;
        float frequency = 1;
        if(!entityIn.isInWater()) {
            magnitude = 1.3f;
            frequency = 1.7f;
        }

        bodyBack.rotateAngleY = -magnitude * 0.25f * MathHelper.sin(frequency * 0.6f * ageInTicks);
    }
}
