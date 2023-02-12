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
public class ModelCod extends ModelFishBase
{
    @Nonnull
    protected final ModelRenderer tailFin;
    public ModelCod() {
        textureWidth = 32;
        textureHeight = 32;

        final ModelRenderer body = new ModelRenderer(this, 0, 0);
        body.addBox(-1, -2, 0, 2, 4, 7);
        body.setRotationPoint(0, 22, 0);

        final ModelRenderer head = new ModelRenderer(this, 11, 0);
        head.addBox(-1, -2, -3, 2, 4, 3);
        head.setRotationPoint(0, 22, 0);

        final ModelRenderer nose = new ModelRenderer(this, 0, 0);
        nose.addBox(-1, -2, -1, 2, 3, 1);
        nose.setRotationPoint(0, 22, -3);

        final ModelRenderer rightFin = new ModelRenderer(this, 22, 1);
        rightFin.addBox(-2, 0, -1, 2, 0, 2);
        rightFin.setRotationPoint(-1, 23, 0);
        rightFin.rotateAngleZ = (-(float)Math.PI / 4);

        final ModelRenderer leftFin = new ModelRenderer(this, 22, 4);
        leftFin.addBox(0, 0, -1, 2, 0, 2);
        leftFin.setRotationPoint(1, 23, 0);
        leftFin.rotateAngleZ = ((float)Math.PI / 4);

        final ModelRenderer topFin = new ModelRenderer(this, 20, -6);
        topFin.addBox(0, -1, -1, 0, 1, 6);
        topFin.setRotationPoint(0, 20, 0);

        tailFin = new ModelRenderer(this, 22, 3);
        tailFin.addBox(0, -2, 0, 0, 4, 4);
        tailFin.setRotationPoint(0, 22, 7);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, @Nonnull Entity entityIn) {
        final float magnitude = entityIn.isInWater() ? 1 : 1.5f;
        tailFin.rotateAngleY = -magnitude * 0.45f * MathHelper.sin(0.6f * ageInTicks);
    }
}
