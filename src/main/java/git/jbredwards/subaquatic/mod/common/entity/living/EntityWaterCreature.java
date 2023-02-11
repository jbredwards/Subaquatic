package git.jbredwards.subaquatic.mod.common.entity.living;

import git.jbredwards.fluidlogged_api.api.util.FluidloggedUtils;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 *
 * @author jbred
 *
 */
public abstract class EntityWaterCreature extends EntityCreature implements IAnimals
{
    public EntityWaterCreature(World worldIn) { super(worldIn); }

    @Override
    public boolean canBreatheUnderwater() { return true; }

    @Override
    public boolean getCanSpawnHere() { return true; }

    @Override
    public boolean isNotColliding() { return world.checkNoEntityCollision(getEntityBoundingBox(), this); }

    @Override
    protected int getExperiencePoints(@Nonnull EntityPlayer player) { return world.rand.nextInt(3) + 1; }

    @Override
    public int getTalkInterval() { return 120; }

    @Override
    public void onEntityUpdate() {
        final int air = getAir();
        super.onEntityUpdate();

        if(isEntityAlive() && !isInWater()) {
            setAir(air - 1);
            if(getAir() == -20) {
                setAir(0);
                attackEntityFrom(DamageSource.DROWN, 2);
            }
        }

        else setAir(300);
    }

    @Override
    public float getBlockPathWeight(@Nonnull BlockPos pos) {
        return FluidloggedUtils.getFluidOrReal(world, pos).getMaterial() == Material.WATER ? world.getLightBrightness(pos) + 10 : super.getBlockPathWeight(pos);
    }

    @Override
    public boolean isPushedByWater() { return false; }

    @Override
    public boolean canBeLeashedTo(@Nonnull EntityPlayer player) { return false; }
}
